package com.example.coney.calc_formula.dataManage;

import android.graphics.Point;

/**
 * Created by coney on 2018/11/12.
 */

public class __Deprecated_Table {

    private static Point tableStartP;
    private static Point tableEndP;

    //选中格子的矩形左上角顶点
//    public static boolean isSelected = false;
    private static String selectedColStr = "I";
    private static int selectedRowId = 10;
    //行距
    private static int rowSpace = 100;
    //列距
    private static int colSpace = 200;
//    //行偏移
//    private static float verticalOffset = 0;
//    //列偏移
//    private static float horizonalOffset = 0;


    public static String getSelectedColStr() {
        return selectedColStr;
    }

    public static void setSelectedColStr(String selectedColStr) {
        __Deprecated_Table.selectedColStr = selectedColStr;
    }

    public static int getSelectedRowId() {
        return selectedRowId;
    }

    public static void setSelectedRowId(int selectedRowId) {
        __Deprecated_Table.selectedRowId = selectedRowId;
    }

//    public static float getVerticalOffset() {
//        return verticalOffset;
//    }
//
//    public static void setVerticalOffset(float verticalOffset) {
//        Table.verticalOffset = verticalOffset;
//        if(verticalOffset<0) Table.verticalOffset = 0;
//
//    }
//
//    public static float getHorizonalOffset() {
//        return horizonalOffset;
//    }
//
//    public static void setHorizonalOffset(float horizonalOffset) {
//
//    }


    public static Point getTableStartP() {
        return tableStartP;
    }

    public static void setTableStartP(Point tableStartP) {
        __Deprecated_Table.tableStartP = tableStartP;
    }

    public static Point getTableEndP() {
        return tableEndP;
    }

    public static void setTableEndP(Point tableEndP) {
        __Deprecated_Table.tableEndP = tableEndP;
    }


    public static int getRowSpace() {
        return rowSpace;
    }

    public static void setRowSpace(int rowSpace) {
        __Deprecated_Table.rowSpace = rowSpace;
    }

    public static int getColSpace() {
        return colSpace;
    }

    public static void setColSpace(int colSpace) {
        __Deprecated_Table.colSpace = colSpace;
    }

}
