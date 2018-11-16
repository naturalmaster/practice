package com.example.coney.calc_formula.mainView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.MyApplication;
import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Book;

import java.io.IOException;

/**
 * Created by coney on 2018/11/12.
 */

public class PaintBoard extends View {
    private VelocityTracker velocityTracker;
    private float downX;
    private float downY;
    private float upX;
    private float upY;
    private Book book;
    private long currentTime;

    private int selectSheetId = 1;
    private Context mContext;

    private PaintData paintData;

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
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DataHelper helper = new DataHelper();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX =  event.getX();
                downY =  event.getY();
                currentTime = System.currentTimeMillis();
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //速度追踪器
//                velocityTracker.computeCurrentVelocity (1000,200.0F);
                if ( Math.abs(downX-event.getX())<8 && Math.abs(downY - event.getY())<8) {
                    break;
                }
                paintData.setVerticalOffset((downY - event.getY()+paintData.getVerticalOffset()));
                paintData.setHorizonalOffset((downX-event.getX()+paintData.getHorizonalOffset()));
                downX = event.getX();
                downY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                if (System.currentTimeMillis() - currentTime<=100){
                    paintData.setSelectedColStr(helper.xIndexToColId(upX,paintData));
                    paintData.setSelectedRowId(helper.yIndexToRowId(upY,paintData));
                    postInvalidate();
                }
                break;
        }
        return true;
    }

    private void init(){
        velocityTracker = VelocityTracker.obtain ();
        initScreenAttri();
        //加载xml文件，暂时使用assets文件夹
        try {
            book = new FileOper().loadFile_(MyApplication.getmContext().getAssets().open("sheet1.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        paintData = new PaintData(selectSheetId,book);
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

}
