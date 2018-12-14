package com.example.coney.calc_formula.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.MyApplication;
import com.example.coney.calc_formula.dataManage.operation.OperationListener;
import com.example.coney.calc_formula.dataManage.operation.OperationMemento;
import com.example.coney.calc_formula.dataManage.operation.RedoOperation;
import com.example.coney.calc_formula.dataManage.operation.UndoOperation;
import com.example.coney.calc_formula.utils.GraphicsUtils;
import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.ColAttri;
import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.utils.CalcUtils;
import com.example.coney.calc_formula.view.activity.MainActivity;

import java.io.File;
import java.util.HashMap;

/**
 * @author s_hfj
 */

public class PaintBoard extends View {
    private EditText inputText;
    private GestureDetector mDetector;
    private Handler mainHandler;

    private Book book;
    private int selectSheetId = 1;
    private OperationMemento operationMemento;
    private RedoOperation redoOperation;
    private UndoOperation undoOperation;
    private Context mContext;

    /**
     * 内核数据paintData
     */
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
        GraphicsUtils.drawGrib(canvas,paintData);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)){
            return true;
        }
        return super.onTouchEvent(event);
    }


    private void init(){
        Book book = new Book("default");
        Sheet sheet = new Sheet("A1:AA99", new HashMap<Integer, Row>(10), new HashMap<String, ColAttri>(5));
        sheet.setSheetId(0);
        book.getSheets().put(0,sheet);
        paintData = new PaintData(0,book);

        operationMemento = new OperationMemento();
        redoOperation = new RedoOperation(paintData);
        undoOperation = new UndoOperation(paintData);
        selcRecChangedListener = new SelcMonitor(paintData,operationMemento);
        //初始化屏幕参数
        initScreenAttri();
        //设置监听器
        initListener();
    }

    public void loadFile(String fileName){
        try {
            Book mBook = paintData.getBook();
            mBook.setFILE_URL(fileName);
            File file = new File(FileOper.FILE_DIR,fileName);
            FileOper fileOper = new FileOper();
            fileOper.loadFile(file,mBook,1);
            selectSheetId = 1;
            paintData.setSelectedSheetId(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initListener(){
        //手势监听
        GestureListener gestureListener = new GestureListener();
        mDetector = new GestureDetector(MyApplication.getmContext(),gestureListener);
        mDetector.setOnDoubleTapListener(gestureListener);
        operationMemento.setOperationListener(new OperationListener() {
            @Override
            public void onRedoStatusChanged(boolean enabled) {
                if (enabled){
                    mainHandler.sendEmptyMessage(MainActivity.ENABLE_REDO_BTN);
                }else {
                    mainHandler.sendEmptyMessage(MainActivity.DISABLE_REDO_BTN);
                }
            }

            @Override
            public void onUndoStatusChanged(boolean enabled) {
                if (enabled){
                    mainHandler.sendEmptyMessage(MainActivity.ENABLE_UNDO_BTN);
                }else {
                    mainHandler.sendEmptyMessage(MainActivity.DISABLE_UNDO_BTN);
                }
            }
        });

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

    public void setMainHandler(Handler handler){
        mainHandler = handler;
    }

    public void redo(){
        if (redoOperation.redo(operationMemento)){
            DataHelper helper = new DataHelper(paintData);
            Cell cell = helper.getCell(paintData.getSelectedRowId(),paintData.getSelectedColStr());
            if(cell != null){
                inputText.setText(cell.getValue());
            }else {
                inputText.setText("");
            }

            invalidate();
        }
    }

    public void undo(){
        if (undoOperation.undo(operationMemento)){
            DataHelper helper = new DataHelper(paintData);
            Cell cell = helper.getCell(paintData.getSelectedRowId(),paintData.getSelectedColStr());
            if(cell != null){
                inputText.setText(cell.getValue());
            }else {
                inputText.setText("");
            }
            invalidate();
        }
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
        DataHelper helper = new DataHelper(paintData);
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
            int selcRow = helper.yIndexToRowId(e.getY());
            String selcCol = helper.xIndexToColId(e.getX());
            if (oldRow != selcRow || !oldCol.equals(selcCol)){
                String newValue = String.valueOf(inputText.getText());
                if (newValue != null && newValue.length() != 0){
                    if (newValue.charAt(0) == '＝' || newValue.charAt(0) == '=' ){
                        newValue = CalcUtils.fullCharToHalf(newValue);
                        newValue = newValue.toUpperCase();
                        if (!CalcUtils.isLegalStr(newValue.substring(1))){
                            Toast.makeText(mContext,"请检查公式，输入合法的公式",Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                }
                selcRecChangedListener.onSelcRecChanged(oldRow,oldCol, newValue);
            }
            paintData.setSelectedRowId(selcRow);
            paintData.setSelectedColStr(selcCol);
            Cell cell = helper.getCell(selcRow,selcCol);
            inputText.setCursorVisible(false);
            if (cell != null){
                if (cell.hasFormula()){
                    inputText.setText("=" + cell.getFormula());
                }else {
                    inputText.setText(cell.getValue());
                }
            }else{
                inputText.setText("");
            }
            inputText.setSelection(inputText.getText().length());
            invalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            paintData.setVerticalOffset(distanceY+paintData.getVerticalOffset());
            paintData.setHorizontalOffset(distanceX+paintData.getHorizontalOffset());
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
