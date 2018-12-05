package com.example.coney.calc_formula.mainView;


import android.graphics.Point;

import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.Sheet;

import java.util.Arrays;


/**
 *
 * @author coney
 * @date 2018/11/15
 * 与绘制、渲染有关的数据
 */

public class PaintData {
    /**
     * 工作簿数据对象
     */
    private Book book;
    /**
     * 表格数量
     */
    private static int sheetCnt = 1;
    /**
     * 表格绘制行高列宽
     */
    private float defRowHeight = 80;
    private float defColWidth = 200;

    /**
     * 行偏移
     */
    private float[] verticalOffset = new float[sheetCnt+1];
    /**
     *  列偏移
     */
    private float[] horizonalOffset = new float[sheetCnt+1];

    /**
     * 表格ID
     */
    private int selectedSheetId = 1;


    /**
     * 屏幕的左上角，和右下角两个点
     */
    private static Point tableStartP;
    private static Point tableEndP;

    private String[] selectedColStr = new String[sheetCnt+1];
    private int[] selectedRowId = new int[sheetCnt+1];

    /**
     * 表格的边界数据
     */


    /**
     * 边界字符串
     */
    private String rangeStr;

    private String startCol;
    private String endCol;
    private int startRow;
    private int endRow;

    public PaintData(int selectedSheetId,Book book) {
        this.selectedSheetId = selectedSheetId;
        this.book = book;

        setRangeStr(getPresentSheet().getRangeStr());
        Arrays.fill(selectedColStr,"I");
        Arrays.fill(selectedRowId,10);
    }
    public float getDefRowHeight() {
        return defRowHeight;
    }

    public float getDefColWidth() {
        return defColWidth;
    }

    public static Point getTableStartP() {
        return tableStartP;
    }

    public static void setTableStartP(Point tableStartP) {
        PaintData.tableStartP = tableStartP;
    }

    public static Point getTableEndP() {
        return tableEndP;
    }

    public static void setTableEndP(Point tableEndP) {
        PaintData.tableEndP = tableEndP;
    }

    public String getSelectedColStr() {
        return selectedColStr[selectedSheetId];
    }

    public void setSelectedSheetId(int selectedSheetId) {
        this.selectedSheetId = selectedSheetId;
    }

    public Sheet getPresentSheet() {
        if (book.getSheets() == null){
            return  null;
        }
        return book.getSheets().get(selectedSheetId);
    }

    public void setSelectedColStr(String selectedColStr) {
        this.selectedColStr[selectedSheetId] = selectedColStr;
    }

    public int getSelectedRowId() {
        return selectedRowId[selectedSheetId];
    }

    public void setSelectedRowId(int selectedRowId) {
        this.selectedRowId[selectedSheetId] = selectedRowId;
    }

    public static int getSheetCnt() {
        return sheetCnt;
    }

    public int getSheetId() {
        return selectedSheetId;
    }

    public String getStartCol() {
        return startCol;
    }

    public String getEndCol() {
        return endCol;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setRangeStr(String rangeStr) {
        this.rangeStr = rangeStr;
        /*下面是利用rangeStr解析出四个边界数据，startCol,endCol,startRow,endRow*/
        int first = -1;
        int tail;
        //寻找第一个不是数字的
        while(!Character.isDigit(rangeStr.charAt(++first)));
        tail =first;
        this.startCol = rangeStr.substring(0,first);
        //遇到冒号就停止
        while (rangeStr.charAt(++tail) != ':');
        this.startRow = Integer.valueOf(rangeStr.substring(first,tail));

        first = tail+1;
        while (++tail<rangeStr.length() && !Character.isDigit(rangeStr.charAt(tail)));
        this.endCol = rangeStr.substring(first,tail);

        this.endRow = Integer.valueOf(rangeStr.substring(tail, rangeStr.length()));
    }


    public float getVerticalOffset() {
        return verticalOffset[selectedSheetId];
    }

    public void setVerticalOffset(float verticalOffset) {
        this.verticalOffset[selectedSheetId] = verticalOffset;
        if(verticalOffset<0) {
            this.verticalOffset[selectedSheetId] = 0;
        }
    }

    public float getHorizonalOffset() {
        return horizonalOffset[selectedSheetId];
    }

    public void setHorizonalOffset(float horizonalOffset) {
        this.horizonalOffset[selectedSheetId] = horizonalOffset;
        if (horizonalOffset < 0) {
            this.horizonalOffset[selectedSheetId] = 0;
        }
    }
}
