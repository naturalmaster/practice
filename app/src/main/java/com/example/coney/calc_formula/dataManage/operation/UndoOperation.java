package com.example.coney.calc_formula.dataManage.operation;

import android.util.Log;

import com.example.coney.calc_formula.dataManage.operation.type.DataState;
import com.example.coney.calc_formula.dataManage.operation.type.Operation;
import com.example.coney.calc_formula.dataManage.operation.type.OperationType;
import com.example.coney.calc_formula.utils.CellDataUtil;
import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.view.PaintData;

/**
 * 撤销动作
 * @author coney
 * @date 2018/12/11
 */

public class UndoOperation {
    private PaintData paintData;

    public UndoOperation(PaintData paintData) {
        this.paintData = paintData;
    }

    public boolean undo(OperationMemento memento){
        Operation operation = memento.undo();
        if (operation == null){
            return false;
        }
        switch (operation.getOperationType()){
            case OperationType.ALTER_DATA:
                Log.d("UndoOperation::","uedo in ALTER_DATA type");
                dataUndo((DataState) operation);
                break;
            default:
                break;
        }
        return true;
    }


    /**
     * 关于数据的撤销操作
     * @param dataState
     */
    private void dataUndo(DataState dataState){
        Cell lastCell = dataState.cell;

        DataHelper helper = new DataHelper(paintData);
        int rowId = lastCell.getRowId();
        String colId = lastCell.getColId();
        Cell cellToUndo = helper.getCell(rowId,colId);
        CellDataUtil.updateCell(cellToUndo,lastCell,paintData);

        paintData.setHorizontalOffset(dataState.getxOffset());
        paintData.setVerticalOffset(dataState.getyOffset());
        paintData.setSelectedRowId(rowId);
        paintData.setSelectedColStr(colId);
    }

}
