package com.example.coney.calc_formula.dataManage.data;

import android.util.Log;

import com.example.coney.calc_formula.utils.CalcUtils;

/**
 *
 * @author coney
 * @date 2018/11/12
 */
public class Cell {

    private int rowId;

    private String colId;
    /**
     * 列号加行号组合
     */
    private String id;

    private String value;

    private boolean hasFormula = false;
    /**
     * 公式内容，当hasFormula为false该值为Null或空字符串
     */
    private String formula;

    public Cell() {
    }

    public Cell(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getRowId() {
        return rowId;
    }

    public String getColId() {
        return colId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        int index=0;
        this.id = id;
        while(index++ < id.length()){
            if (!Character.isLetter(id.charAt(index))){
                this.colId = id.substring(0,index);
                this.rowId = Integer.parseInt(id.substring(index,id.length()));
                return;
            }
        }
        //error
        Log.e("dataMange.data.unit","idError");

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        String resValue = value;
        if (CalcUtils.isDouble(resValue)){
            float f = Float.parseFloat(resValue);
            if (f == (int)f){
                resValue = String.valueOf((int)f);
            }
        }
        this.value = resValue;
    }

    public boolean hasFormula() {
        return hasFormula;
    }

    public void setHasFormula(boolean hasFormula) {
        this.hasFormula = hasFormula;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        if (formula == null || formula.equals("")  || formula.length() == 0){
            this.formula = "";
            this.hasFormula = false;
        }else {
            this.hasFormula = true;
            this.formula = formula;
        }

    }

}
