package com.example.coney.calc_formula.dataManage.data;


import java.util.HashMap;

/**
 * Created by coney on 2018/11/12.
 */

public class Sheet {
    private String rangeStr;
    private HashMap<Integer,Row> rows;
    private HashMap<String,ColAttri> colAttriMap;
    private int sheetId;

    private float defRowHeight = 100;
    private float defColWidth = 200;


    public int getSheetId() {
        return sheetId;
    }

    public void setSheetId(int sheetId) {
        this.sheetId = sheetId;
    }


    public Sheet(String rangeStr, HashMap<Integer, Row> rows,HashMap<String,ColAttri> colAttrMap) {
        this.rangeStr = rangeStr;
        this.rows = rows;
    }
    public Sheet(String rangeStr, HashMap<Integer, Row> rows) {
        this(rangeStr,rows,new HashMap<String, ColAttri>());
    }


    public HashMap<Integer, Row> getRows() {
        return rows;
    }

    public void setRows(HashMap<Integer, Row> rows) {
        this.rows = rows;
    }

    public String getRangeStr() {
        return rangeStr;
    }

    public void setRangeStr(String rangeStr) {
        this.rangeStr = rangeStr;
    }



    public float getDefRowHeight() {
        return defRowHeight;
    }

    public void setDefRowHeight(float defRowHeight) {
        this.defRowHeight = defRowHeight;
    }

    public float getDefColWidth() {
        return defColWidth;
    }

    public void setDefColWidth(float defColWidth) {
        this.defColWidth = defColWidth;
    }
}
