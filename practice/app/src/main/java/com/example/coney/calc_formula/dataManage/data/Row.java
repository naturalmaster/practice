package com.example.coney.calc_formula.dataManage.data;

import java.util.HashMap;

/**
 * Created by coney on 2018/11/12.
 */

public class Row {
    /**
     * 一个类，代表单行
     */
    //<K,V>-><colNum,unit>，以列ID为键，单元格为值
    private HashMap<String,Unit> unitMap;

    private int rowId;

    public HashMap<String, Unit> getUnitMap() {
        return unitMap;
    }

    public void setUnitMap(HashMap<String, Unit> unitMap) {
        this.unitMap = unitMap;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("rowId"+rowId+"\n");
        for (HashMap.Entry<String,Unit> entry:unitMap.entrySet()){
            Unit unit = entry.getValue();
            sb.append("unit key = " + entry.getKey() +"        value = "+unit.getValue()+"");
            sb.append("  f:"+(entry.getValue().isHasFormula()?unit.getFormula():" ")+"\n");

        }
        return sb.toString();
    }
}
