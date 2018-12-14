package com.example.coney.calc_formula.dataManage.operation;

import com.example.coney.calc_formula.dataManage.operation.type.DataState;
import com.example.coney.calc_formula.dataManage.operation.type.Operation;
import com.example.coney.calc_formula.dataManage.operation.type.OperationType;
import com.example.coney.calc_formula.utils.CellDataUtil;
import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.view.PaintData;

/**
 * 还原操作
 * @author coney
 * @date 2018/12/11
 */

public class RedoOperation {
    private PaintData paintData;

    public RedoOperation(PaintData paintData) {
        this.paintData = paintData;
    }

    public boolean redo(OperationMemento memento){
        Operation latestOperation = memento.redo();
        if (latestOperation == null){
            return false;
        }
        switch (latestOperation.getOperationType()){
            case OperationType.ALTER_DATA:
                redoData((DataState) latestOperation);
                break;
            default:
                break;
        }
        return true;
    }

    private void redoData(DataState dataState){
        DataHelper helper = new DataHelper(paintData);
        Cell latestCell = dataState.cell;
        int rowId = latestCell.getRowId();
        String colId = latestCell.getColId();
        Cell currentCell = helper.getCell(rowId,colId);

        CellDataUtil.updateCell(currentCell,latestCell,paintData);
        paintData.setHorizontalOffset(dataState.getxOffset());
        paintData.setVerticalOffset(dataState.getyOffset());
        paintData.setSelectedRowId(rowId);
        paintData.setSelectedColStr(colId);
    }
}
