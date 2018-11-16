package com.example.coney.calc_formula.mainView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.icu.text.LocaleDisplayNames;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.coney.calc_formula.MyApplication;
import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.Table;

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

    private long currentTime;


    public PaintBoard(Context context) {
        super(context);
        init(context);
    }


    public PaintBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        PaintUtils.drawGrib(canvas);
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
                Table.setVerticalOffset( (downY - event.getY()+Table.getVerticalOffset()));
                Table.setHorizonalOffset( (downX-event.getX()+Table.getHorizonalOffset()));
                downX = event.getX();
                downY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                if (System.currentTimeMillis() - currentTime<=100){
                    Table.isSelected = true;
                    Table.setSelectedColStr(helper.xIndexToColId(upX));
                    Table.setSelectedRowId(helper.yIndexToRowId(upY));
                    postInvalidate();
                }
                if (velocityTracker!=null){
                    velocityTracker.clear();
                    //  velocityTracker.recycle();
                }
                break;
        }
        return true;
    }

    private void init(Context context){
        velocityTracker = VelocityTracker.obtain ();

        Point startP = new Point();
        Point endP = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(endP);
        startP.set(0,((Activity)context).findViewById(Window.ID_ANDROID_CONTENT).getTop());

        Table.setTableStartP(startP);
        Table.setTableEndP(endP);

        try {
            new DataHelper().initTableInfoFromFile( MyApplication.getmContext().getAssets().open("sheet1.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
