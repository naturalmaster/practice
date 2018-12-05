package com.example.coney.calc_formula.mainView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Region;

import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.ColAttri;
import com.example.coney.calc_formula.dataManage.data.Row;

import java.util.HashMap;

/**
 * @author s_Hfj
 * @date 2019.12.3
 */

public class PaintUtils {
    static final Paint mPaint_normalLine = new Paint();
    static final Paint mPaint_text = new Paint();
    static final Paint mPaint_selcText = new Paint();
    static final Paint mPaint_selectedLine = new Paint();

    static {
        paintInit();
    }
    private static void paintInit(){
        mPaint_selectedLine.setColor(Color.argb(255,6,159,117));
        mPaint_selectedLine.setStrokeWidth(8.5f);
        mPaint_text.setTextSize(55);
        mPaint_selcText.setColor(Color.argb(255,6,159,117));
        mPaint_selcText.setTextSize(55);
    }

    private static void drwaGribCol(Canvas canvas,PaintData paintData){
        HashMap<String,ColAttri> colAttriMap;
        float rowHeaderHeight = paintData.getDefRowHeight();
        float defColWidth = paintData.getDefColWidth();

        int width = PaintData.getTableEndP().x;
        int height = PaintData.getTableEndP().y;
        float hOffset = paintData.getHorizonalOffset();
        colAttriMap = paintData.getPresentSheet().getColAttriMap();
        //画原始竖线
        canvas.drawLine(defColWidth,0,defColWidth,height,mPaint_normalLine);
        canvas.save();
        canvas.translate(defColWidth,0);
        float rawX = 0;
        int colNum = 1;
        float presentColWidth = 0;
        while(rawX<=hOffset+width){
            if (colAttriMap.get(DataHelper.numToColStr(colNum))!=null){
                presentColWidth = colAttriMap.get(DataHelper.numToColStr(colNum)).getColWidth();
                rawX +=presentColWidth;
            }
            else{
                presentColWidth = defColWidth;
                rawX+=defColWidth;
            }

            if (rawX>hOffset){
                canvas.drawLine(rawX-hOffset,0,rawX-hOffset,height,mPaint_normalLine);
                drawText(rawX-hOffset-presentColWidth/2,rowHeaderHeight/2,DataHelper.numToColStr(colNum) ,canvas);
            }
            colNum++;
        }
        canvas.restore();
/*
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
        }*/
    }


    //横线的绘制
    private static void drawGribRow(Canvas canvas,PaintData paintData){
        HashMap<Integer,Row> rows = paintData.getPresentSheet().getRows();
        float defRowHeight = paintData.getDefRowHeight();
        float colHeaderWidth = paintData.getDefColWidth();
        int width = PaintData.getTableEndP().x;
        int height = PaintData.getTableEndP().y;
        float vOffset = paintData.getVerticalOffset();
        //画原始横线
        canvas.drawLine(0,defRowHeight,width,defRowHeight,mPaint_normalLine);
        canvas.save();
        canvas.translate(0,defRowHeight);
        float rawY = 0;
        int rowNum = 1;
        float presentRowWidth = 0;
        while(rawY<=vOffset+height){
            if (rows.get(rowNum)!=null){
                presentRowWidth = rows.get(rowNum).getRowHeight();
                rawY +=presentRowWidth;
            }
            else{
                presentRowWidth = defRowHeight;
                rawY+=defRowHeight;
            }

            if (rawY>vOffset){
                canvas.drawLine(0,rawY-vOffset,width,rawY-vOffset,mPaint_normalLine);
                drawText(colHeaderWidth/2,rawY - vOffset - presentRowWidth/2,""+rowNum +"",canvas);
            }
            rowNum++;
        }
        canvas.restore();
    }

    private static void drawData(Canvas canvas,PaintData paintData){
        DataHelper helper = new DataHelper();

        Point screenStartPoint = PaintData.getTableStartP();
        Point screenEndPoint = PaintData.getTableEndP();
        float yOffset = paintData.getVerticalOffset();
        float xOffset = paintData.getHorizonalOffset();
        float rowHeight = paintData.getDefRowHeight();
        float colWidth = paintData.getDefColWidth();


        int rowStart = paintData.getStartRow();
        int rowEnd = paintData.getEndRow();

        int colNumStart = DataHelper.colStrToNum(paintData.getStartCol());
        int colNumEnd = DataHelper.colStrToNum(paintData.getEndCol());

        HashMap<Integer,Row> rowHashMap = paintData.getPresentSheet().getRows();
        HashMap<String,ColAttri> colAttriHashMap = paintData.getPresentSheet().getColAttriMap();
        /**
         * 用于数据绘制的变量
         */
        int rawY =  0;
        int rowNum = 1;

        canvas.save();
        canvas.translate(colWidth,rowHeight);
        while(rawY<=screenEndPoint.y+yOffset){
            if (rowHashMap.get(rowNum) == null ){
                rawY += rowHeight;
                rowNum++;
                continue;
            }
            float presentRowHeight = rowHashMap.get(rowNum).getRowHeight();
            int rawX = 0;
            int colNum =1;
            while (rawY>yOffset - presentRowHeight && rawX<=screenEndPoint.x+xOffset &&rowNum>=rowStart && rowNum<=rowEnd){
                Cell cell = helper.getCell(rowNum,DataHelper.numToColStr(colNum),paintData);
                float presentColWidth;

                if (colAttriHashMap.get(DataHelper.numToColStr(colNum)) != null){
                    presentColWidth = colAttriHashMap.get(DataHelper.numToColStr(colNum)).getColWidth();
                }else {
                    presentColWidth = colWidth;
                }

                //有数值,在列的范围内
               if (cell != null &&
                       colNum>=colNumStart && colNum<=colNumEnd){
                   if (rawX>xOffset-presentColWidth){
                       drawText(rawX-xOffset+presentColWidth/2,rawY-yOffset+presentRowHeight/2,cell.getValue(),canvas);
                   }
               }
                rawX += presentColWidth;
                colNum++;
            }
            rawY += rowHashMap.get(rowNum).getRowHeight();
            rowNum++;
        }
        canvas.restore();
    }
    /**
     * 给定左上角的顶点，绘制选中框
     * @param canvas
     * @param paintData
     */
    public static void drawSelectedRec(Canvas canvas,PaintData paintData){
        String colStr = paintData.getSelectedColStr();
        DataHelper helper = new DataHelper();
        int rowId = paintData.getSelectedRowId();
        float x1,y1;
        float rowHeight = paintData.getPresentSheet().getRows().get(rowId)==null?paintData.getDefRowHeight():paintData.getPresentSheet().getRows().get(rowId).getRowHeight();
        float colWidth = paintData.getPresentSheet().getColAttriMap().get(colStr)==null?paintData.getDefColWidth():paintData.getPresentSheet().getColAttriMap().get(colStr).getColWidth();

        x1 = helper.getXByCol(colStr,paintData);
        y1 = helper.getYByRow(rowId,paintData);

        float[] points ={
                x1,y1,x1+colWidth,y1,
                x1+colWidth,y1,x1+colWidth,y1+rowHeight,
                x1,y1,x1,y1+rowHeight,
                x1,y1+rowHeight,x1+colWidth,y1+rowHeight
        };
        canvas.drawLines(points,0,16,mPaint_selectedLine);
        canvas.restore();
        canvas.save();
        canvas.clipRect(0,0,paintData.getDefColWidth(), paintData.getDefRowHeight(), Region.Op.DIFFERENCE);
        drawSelcText(paintData.getDefColWidth()/2,y1+rowHeight/2,rowId+"",canvas);
        drawSelcText(x1+colWidth/2,paintData.getDefRowHeight()/2,colStr,canvas);

    }

    public static void drawGrib(Canvas canvas,PaintData paintData){
        paintInit();
        canvas.save();
        canvas.clipRect(PaintData.getTableStartP().x,PaintData.getTableStartP().y,
                paintData.getDefColWidth(),paintData.getDefRowHeight(),
                Region.Op.DIFFERENCE);
        drwaGribCol(canvas,paintData);
        drawGribRow(canvas,paintData);
        canvas.restore();

        canvas.save();
        canvas.clipRect(PaintData.getTableStartP().x+paintData.getDefColWidth(),
                PaintData.getTableStartP().y+paintData.getDefRowHeight(),
                PaintData.getTableEndP().x,PaintData.getTableEndP().y,Region.Op.INTERSECT);
        drawData(canvas,paintData);
        drawSelectedRec(canvas,paintData);
        canvas.restore();
    }
    /**
     * 画文字
     */
    public static void drawText(float x,float y,String str,Canvas canvas){
        Paint paint = mPaint_text;
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(str,x,y,paint);
    }
//    public static void drawDataText(float x,float y,float rowHeight,float colWidth,String str,Canvas canvas){
//        drawText(x,y,rowHeight,colWidth,"right",mPaint_text,str,canvas);
//    }
//
//    /**
//     * 可以根据参数，在表格中以不同的对齐方式进行文字的绘制
//     * @param x 表格左上角的x坐标
//     * @param y 表格左上角的y坐标
//     * @param rowHeight 当前表格行高列宽
//     * @param colWidth 当前表格行高列宽
//     * @param alignType 对其方式，可选的有：left,right,center
//     * @param paint 画笔
//     * @param str 绘制具体的文字
//     * @param canvas 画布
//     */
//    public static void drawText(float x,float y,float rowHeight,float colWidth,String alignType,Paint paint,String str,Canvas canvas){
//        switch (alignType){
//            case "left":
//                paint.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText(str,x,y+rowHeight/2,paint);
//                break;
//            case "right":
//                paint.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(str,x+colWidth,y+rowHeight/2,paint);
//                break;
//            case "center":
//                paint.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(str,x+colWidth/2,y+rowHeight/2,paint);
//                break;
//
//                default:
//                    Log.e("PaintUtils:358","alignTypeError");
//                    break;
//        }
//    }

    public static void drawSelcText(float x,float y,String str,Canvas canvas){
        Paint paint = mPaint_selcText;
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(str,x,y,paint);
    }


}
