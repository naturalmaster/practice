package com.example.coney.calc_formula.view;

/**
 * Created by coney on 2018/11/30.
 */

interface OnSelcRecChangedListener {
    /**
     * 当表格的选中框更改时调用的方法
     * @param oldRow
     * @param oldCol
     * @param newValue
     */
    void onSelcRecChanged(int oldRow,String oldCol,String newValue);
}
