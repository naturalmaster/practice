package com.example.coney.calc_formula.mainView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.MyApplication;
import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.ColAttri;
import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author s_hfj
 */

public class PaintBoard extends View {
    private EditText inputText;
    private GestureDetector mDetector;

    private Book book;
    private int selectSheetId = 1;
    private Context mContext;

    private PaintData paintData;
    private OnSelcRecChangedListener selcRecChangedListener;

    public PaintBoard(Context context) {
        this(context,null);
    }


    public PaintBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        PaintUtils.drawGrib(canvas,paintData);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)){
            return true;
        }
        return super.onTouchEvent(event);
    }


    private void init(){
        //初始化屏幕参数
        initScreenAttri();
        //设置监听器
        initListener();
//        Book book = new Book("default");
//        Sheet sheet = new Sheet("A1:A1",new HashMap<Integer, Row>(),new HashMap<String, ColAttri>());
//        sheet.setSheetId(0);
//        book.getSheets().put(0,sheet);
//        paintData = new PaintData(0,book);

        //加载xml文件，暂时使用assets文件夹
        try {
            book = new FileOper().loadFile_assets(MyApplication.getmContext().getAssets().open("sheet1.xml"));
            Log.d("book_null","******************");
//            book = new FileOper().loadFile_assets(new FileInputStream(new File(FileOper.FILE_DIR+"/sheet1.xml")));
//            book = new FileOper().loadFile(new File(FileOper.FILE_DIR+"/sheet1.xml"));
            Log.d("book_null",(book == null) + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        paintData = new PaintData(selectSheetId,book);
        selcRecChangedListener = new SelcMonitor(paintData);
    }

//    public void loadFile(String fileName){
//        //加载xml文件，暂时使用assets文件夹
//        try {
////            book = new FileOper().loadFile_assets(MyApplication.getmContext().getAssets().open("sheet1.xml"));
//            Log.d("book_null","******************");
//            book = new FileOper().loadFile_assets(new FileInputStream(new File(FileOper.FILE_DIR,fileName)));
////            book = new FileOper().loadFile(new File(FileOper.FILE_DIR+"/sheet1.xml"));
//            Log.d("book_null",(book == null) + "");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        paintData = new PaintData(selectSheetId,book);
//    }
    private void initListener(){
        //手势监听
        GestureListener gestureListener = new GestureListener();
        mDetector = new GestureDetector(MyApplication.getmContext(),gestureListener);
        mDetector.setOnDoubleTapListener(gestureListener);

    }

    private void initScreenAttri(){
        Point startP = new Point();
        Point endP = new Point();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(endP);
        startP.set(0,((Activity)mContext).findViewById(Window.ID_ANDROID_CONTENT).getTop());

        PaintData.setTableStartP(startP);
        PaintData.setTableEndP(endP);
    }


    public PaintData getPaintData(){
        return this.paintData;
    }
    public void setInputText(EditText mInputText){
        this.inputText = mInputText;
        //EditText监听
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText.setCursorVisible(true);
                inputText.setSelection(inputText.getText().length());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputText.getVisibility() == View.GONE){
                return super.onKeyUp(keyCode,event);
            }
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(),0);
            inputText.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyUp(keyCode,event);
    }

    class GestureListener implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{
        DataHelper helper = new DataHelper();
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputText.setVisibility(View.VISIBLE);
            inputText.setCursorVisible(true);
            inputMethodManager.showSoftInput(inputText,0);
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {

            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int oldRow = paintData.getSelectedRowId();
            String oldCol = paintData.getSelectedColStr();
            int selcRow = helper.yIndexToRowId(e.getY(),paintData);
            String selcCol = helper.xIndexToColId(e.getX(),paintData);
            if (oldRow!=selcRow || !oldCol.equals(selcCol)){
                selcRecChangedListener.onSelcRecChanged(oldRow,oldCol, String.valueOf(inputText.getText()));
            }
            paintData.setSelectedRowId(selcRow);
            paintData.setSelectedColStr(selcCol);
            Cell cell = helper.getCell(selcRow,selcCol,paintData);
            inputText.setCursorVisible(false);
            if (cell!=null){
                inputText.setText(cell.getValue());
            }else{
                inputText.setText("");

            }
            inputText.setSelection(inputText.getText().length());
            postInvalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            paintData.setVerticalOffset(distanceY+paintData.getVerticalOffset());
            paintData.setHorizonalOffset(distanceX+paintData.getHorizonalOffset());
            invalidate();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }


}
