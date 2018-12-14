package com.example.coney.calc_formula.dataManage.data;

/**
 *
 * @author coney
 * @date 2018/11/15
 */

public class ColAttri {
    private float defColWidth = 200;
    private boolean bestFit;
    private float colWidth = -1;
    private String colStr;

    public float getColWidth() {
        if (colWidth == -1){
            return  defColWidth;
        }
        return colWidth;
    }


    public void setColWidth(float colWidth) {
        this.colWidth = colWidth;
    }

    public String getColStr() {
        return colStr;
    }

    public void setColStr(String colStr) {
        this.colStr = colStr;
    }
}
