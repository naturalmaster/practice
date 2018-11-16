package com.example.coney.calc_formula.dataManage;

import com.example.coney.calc_formula.dataManage.data.Row;

import java.util.HashMap;

/**
 * Created by coney on 2018/11/12.
 */

public class TableInfo {
    private static TableInfo instance;
    private String rangeStr;
    private HashMap<Integer,Row> rows;

    private TableInfo() {
    }

    private TableInfo(String rangeStr, HashMap<Integer, Row> rows) {
        this.rangeStr = rangeStr;
        this.rows = rows;
    }

    protected static void setInstance(String rangeStr, HashMap<Integer, Row> rows){
        TableInfo.instance = new TableInfo(rangeStr,rows);
    }

    public static TableInfo getInstance(){
        if (instance!=null)
            return instance;
        return new TableInfo("-1",null);
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

    public void setRowAndColSpc(int rowSpace, int colSpace){
        Table.setRowSpace(rowSpace);
        Table.setColSpace(colSpace);
    }

}
