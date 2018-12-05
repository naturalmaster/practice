package com.example.coney.calc_formula.dataManage.data;


import java.util.HashMap;

/**
 *
 * @author coney
 * @date 2018/11/12
 */

public class Sheet {
    private String rangeStr;
    private HashMap<Integer,Row> rows;
    private HashMap<String,ColAttri> colAttriMap = null;
    private int sheetId;

    private float defRowHeight = 80f;
    private float defColWidth = 200f;


    public int getSheetId() {
        return sheetId;
    }

    public void setSheetId(int sheetId) {
        this.sheetId = sheetId;
    }

    public Sheet(String rangeStr, HashMap<Integer, Row> rows,HashMap<String,ColAttri> colAttrMap) {
        setRangeStr(rangeStr);
        this.rows = rows;
        this.colAttriMap = colAttrMap;
    }

    public HashMap<String, ColAttri> getColAttriMap() {
        return colAttriMap;
    }

    public void setColAttriMap(HashMap<String, ColAttri> colAttriMap) {
        this.colAttriMap = colAttriMap;
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



    public float getRawDefRowHeight() {
        return defRowHeight;
    }

    public void setDefRowHeight(float defRowHeight) {
        this.defRowHeight = defRowHeight;
    }

    public float getRawDefColWidth() {
        return defColWidth;
    }

    public void setDefColWidth(float defColWidth) {
        this.defColWidth = defColWidth;
    }
}
