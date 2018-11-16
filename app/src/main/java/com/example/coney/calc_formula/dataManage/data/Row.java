package com.example.coney.calc_formula.dataManage.data;

import java.util.HashMap;

/**
 * Created by coney on 2018/11/12.
 */

public class Row {

//     一个类，代表单行
    //<K,V>-><colNum,unit>，以列ID为键，单元格为值
    private HashMap<String,Cell> unitMap;

    private int rowId;
    private float rowHeight=100;

    public HashMap<String, Cell> getUnitMap() {
        return unitMap;
    }

    public void setUnitMap(HashMap<String, Cell> unitMap) {
        this.unitMap = unitMap;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }


    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("rowId"+rowId+"\n");
        for (HashMap.Entry<String,Cell> entry:unitMap.entrySet()){
            Cell cell = entry.getValue();
            sb.append("unit key = " + entry.getKey() +"        value = "+cell.getValue()+"");
            sb.append("  f:"+(entry.getValue().isHasFormula()?cell.getFormula():" ")+"\n");

        }
        return sb.toString();
    }
}
