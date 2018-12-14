package com.example.coney.calc_formula.view;

import android.util.Log;

import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.operation.type.DataState;
import com.example.coney.calc_formula.dataManage.operation.OperationMemento;
import com.example.coney.calc_formula.utils.CellDataUtil;

/**
 *
 * @author coney
 * @date 2018/12/3
 */

public class SelcMonitor implements OnSelcRecChangedListener {
    private PaintData paintData;
    private OperationMemento memento;


    public SelcMonitor(PaintData paintData, OperationMemento memento) {
        this.paintData = paintData;
        this.memento = memento;
    }

    /**
     * 当选中框改变时，如果数据有修改，会对数据进行内核上的修改
     * @param oldRow
     * @param oldCol
     * @param newValue
     */
    @Override
    public void onSelcRecChanged(int oldRow, String oldCol, String newValue) {
        DataHelper helper = new DataHelper(paintData);
        Cell cell = helper.getCell(oldRow,oldCol);
        Cell oldCell;
        if ( cell != null){
            oldCell = CellDataUtil.copyCell(cell);
        }else {
            oldCell = new Cell(oldCol+oldRow,"");
        }
        boolean altered = helper.updateCell(oldRow, oldCol, newValue);
        if (altered){
            DataState dataState = new DataState(oldCell,paintData.getHorizontalOffset(),paintData.getVerticalOffset());
            memento.doSomeThing(dataState);
        }
    }
}
