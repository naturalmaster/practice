package com.example.coney.calc_formula.dataManage.data;

import java.util.HashMap;

/**
 *
 * @author coney
 * @date 2018/11/12
 */

public class Row {

//     一个类，代表单行
    //HashMap<K,V>-><colNum,Cell>，以列ID为键，单元格为值
    private HashMap<String,Cell> unitMap;

    private float defRowHeight = 80;
    private int rowId;
    private float rowHeight=-1;
    private boolean hasRowHeight = false;

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


    public void setDefRowHeight(float defRowHeight) {
        this.defRowHeight = defRowHeight;
    }

    public boolean isHasRowHeight() {
        return hasRowHeight;
    }

    public float getRowHeight() {
        if (rowHeight>0){
            return rowHeight;
        }
        return defRowHeight;
    }

    public void setRowHeight(float rowHeight) {
        hasRowHeight = true;
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
