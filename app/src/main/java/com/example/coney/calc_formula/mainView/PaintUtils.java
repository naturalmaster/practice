package com.example.coney.calc_formula.mainView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Region;

import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Cell;

/**
 * Created by coney on 2018/11/12.
 */

public class PaintUtils {
    static final Paint mPaint_normalLine = new Paint();
    static final Paint mPaint_text = new Paint();
    static final Paint mPaint_selectedLine = new Paint();

    private static void paintInit(){

    }

    private static void drawRowHeader(Canvas canvas,PaintData paintData){
        DataHelper helper = new DataHelper();
        float rowSpace = paintData.getPresentSheet().getDefRowHeight();
        float colSpace = paintData.getPresentSheet().getDefColWidth();

        int width = PaintData.getTableEndP().x;
        int height = PaintData.getTableEndP().y;
        float horizonalOffset = paintData.getHorizonalOffset();

        //画原始竖线
        canvas.drawLine(colSpace,0,colSpace,height,mPaint_normalLine);

        //startX为进行竖线表格绘制时的一个指针，指向当前绘制竖线的位置
        float startX;
        float tmp = horizonalOffset%colSpace;
        if (tmp ==0) startX = colSpace;
        else
            startX = colSpace+colSpace-tmp;
        while(startX<=width+colSpace){
            canvas.drawLine(startX,0,startX,height,mPaint_normalLine);
            drawText(startX-colSpace/2,rowSpace/2,""+helper.numToColStr((int) (((startX + horizonalOffset)/colSpace)-1)),canvas);
            startX+=colSpace;
        }
    }

    private static void drawColHeader(Canvas canvas,PaintData paintData){
        float rowSpace = paintData.getPresentSheet().getDefRowHeight();
        float colSpace = paintData.getPresentSheet().getDefColWidth();

        int width = PaintData.getTableEndP().x;
        int height = PaintData.getTableEndP().y;
        float verticalOffset = paintData.getVerticalOffset();

        //画原始横线
        canvas.drawLine(0,rowSpace,width,rowSpace,mPaint_normalLine);

        //startY为进行竖线表格绘制时的一个指针，指向当前绘制横线的位置
        float startY;
        float tmp = verticalOffset%rowSpace;
        if (tmp ==0) startY = rowSpace;
        else
            startY = rowSpace+rowSpace-tmp;

        while(startY<=height){
            canvas.drawLine(0,startY,width,startY,mPaint_normalLine);
            drawText(colSpace/2,startY-rowSpace/2,""+(int)(((startY + verticalOffset)/rowSpace)-1)+"",canvas);
            startY+=rowSpace;
        }
    }

    private static void drawData(Canvas canvas,PaintData paintData){
//        Point screenStartPoint = Table.getTableStartP();
        Point screenStartPoint = PaintData.getTableStartP();
//        Point screenEndPoint = Table.getTableEndP();
        Point screenEndPoint = PaintData.getTableEndP();
        float yOffset = paintData.getVerticalOffset();
        float xOffset = paintData.getHorizonalOffset();
        float rowHeight = paintData.getPresentSheet().getDefRowHeight();
        float rowWidth = paintData.getPresentSheet().getDefColWidth();

        float xIndexStart =  rowWidth - xOffset%rowWidth;
        float yIndex =  rowHeight - yOffset % rowHeight;
        DataHelper helper = new DataHelper();

        int rowStart = 0;
        int rowEnd = 0;
        Integer rowId;
        String range = paintData.getPresentSheet().getRangeStr();

        int j = -1;
        while(!Character.isDigit(range.charAt(++j)));
        int i =j;
        while (range.charAt(++i) == ':'){
            rowStart = Integer.valueOf(range.substring(j,i));
        }
        while (++i<range.length() && !Character.isDigit(range.charAt(i)));
        rowEnd = Integer.valueOf(range.substring(i, range.length()));

        while(yIndex<=screenEndPoint.y+rowHeight){
            rowId = helper.yIndexToRowId(yIndex,paintData);
            if (rowId<rowStart || rowId>rowEnd){
                yIndex+=rowHeight;
                continue;
            }
            float xIndex = xIndexStart;
            while (xIndex<=screenEndPoint.x+rowWidth){
               Cell cell = helper.getCellByXY(xIndex,yIndex,paintData);
               if (cell == null) {
                   xIndex += rowWidth;
                    continue;
               }
                drawText(xIndex+rowWidth/2,yIndex+rowHeight/2,cell.getValue(),canvas);
                xIndex += rowWidth;
            }
            yIndex += rowHeight;
        }

    }

    //给定左上角的顶点，绘制选中框
    public static void drawSelectedRec(Canvas canvas,PaintData paintData){
        String colStr = paintData.getSelectedColStr();
        int rowId = paintData.getSelectedRowId();
        float x1,y1;
        float rowHeight = paintData.getPresentSheet().getDefRowHeight();
        float rowWidth = paintData.getPresentSheet().getDefColWidth();
        x1 = getPointById(colStr,paintData);
        y1 = getPointById(rowId,paintData);
        mPaint_selectedLine.setColor(Color.argb(255,6,159,117));
        mPaint_selectedLine.setStrokeWidth(8.5f);
        float[] points ={
                x1,y1,x1+rowWidth,y1,
                x1+rowWidth,y1,x1+rowWidth,y1+rowHeight,
                x1,y1,x1,y1+rowHeight,
                x1,y1+rowHeight,x1+rowWidth,y1+rowHeight
        };
        canvas.drawLines(points,0,16,mPaint_selectedLine);
    }

    /**
     * 根据行号和列号获得对应单元格的矩形左上角坐标
     * @param colStr 列号
     * @return 返回对应的单元格左上角坐标Point对象
     */
    public static float getPointById(String colStr,PaintData paintData){
        float x;

        x = paintData.getPresentSheet().getDefColWidth()*new DataHelper().colStrToNum(colStr)-paintData.getHorizonalOffset();
        return x;
    }
    /**
     * 根据行号和列号获得对应单元格的矩形左上角坐标
     * @param rowId 行号
     * @return 返回对应的单元格左上角坐标Point对象
     */
    public static float getPointById(Integer rowId,PaintData paintData){
        float y;
        y = paintData.getPresentSheet().getDefRowHeight()*rowId-paintData.getVerticalOffset();
        return y;
    }


    /**
     * 根据两个坐标参数，得到该坐标位于的单元的左上角的坐标
     * @param val 坐标值
     * @param type 有两个可选参数，'x','y'分别对应x轴坐标和y轴坐标
     * @return  返回对应小格子的x起始坐标或y起始坐标
     */
    public static float selectedXYFromXY(float val,char type,PaintData paintData){
        float result = 0;
        switch (type){
            case 'x':
                float x = val;
                float rowWidth = paintData.getPresentSheet().getDefColWidth();
                float xOffset = paintData.getHorizonalOffset();
                float leftSide = rowWidth - xOffset%rowWidth;
                if (x<=leftSide+rowWidth){
                    result =  leftSide;
                }else {
                    x = x-leftSide;
                    int tmp = (int) (x/rowWidth);
                    result =  leftSide+tmp*rowWidth;
                }
                break;
            case 'y':
                float y = val;
                float rowHeight = paintData.getPresentSheet().getDefRowHeight();
                float yOffset = paintData.getVerticalOffset();
                float topSide = rowHeight - yOffset%rowHeight;
                if (y<=topSide+rowHeight){
                    result =  topSide;
                }else {
                    y = y-topSide;
                    int tmp = (int) (y/rowHeight);
                    result =  topSide+tmp*rowHeight;
                }
                break;
        }
        return result;

    }

    public static void drawGrib(Canvas canvas,PaintData paintData){

        paintInit();
        canvas.save();
        canvas.clipRect(PaintData.getTableStartP().x,PaintData.getTableStartP().y,
                paintData.getPresentSheet().getDefColWidth(),paintData.getPresentSheet().getDefRowHeight(),
                Region.Op.DIFFERENCE);
        drawRowHeader(canvas,paintData);
        drawColHeader(canvas,paintData);
        canvas.restore();
        canvas.save();
        canvas.clipRect(paintData.getTableStartP().x+paintData.getPresentSheet().getDefColWidth(),
                paintData.getTableStartP().y+paintData.getPresentSheet().getDefRowHeight(),
                paintData.getTableEndP().x,paintData.getTableEndP().y,Region.Op.INTERSECT);
        drawData(canvas,paintData);
        drawSelectedRec(canvas,paintData);
        canvas.restore();
    }


    /**
     * 画文字
     */
    public static void drawText(float x,float y,String str,Canvas canvas){
        Paint paint = mPaint_text;
        paint.setTextSize(59);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(str,x,y,paint);
    }

}
