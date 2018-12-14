package com.example.coney.calc_formula.utils;

import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.view.PaintData;

import java.util.HashMap;

/**
 *
 * @author coney
 * @date 2018/12/11
 */

public class CellDataUtil {

    public static Cell copyCell(Cell cellToCP){
        if (cellToCP == null){
            return null;
        }
        Cell result = new Cell();
        result.setValue(cellToCP.getValue());
        result.setId(cellToCP.getId());
        if (cellToCP.hasFormula()){
            result.setFormula(cellToCP.getFormula());
        }else {
            result.setFormula(null);
        }
        return result;
    }

    public static boolean updateCell(Cell cellToUpdate,Cell lastCell,PaintData paintData){
        if (lastCell == null){
            return false;
        }
        if (cellToUpdate == null){
            return addCellToPaintData(lastCell,paintData);
        }
        cellToUpdate.setValue(lastCell.getValue());
        if (lastCell.hasFormula()){
            cellToUpdate.setFormula(lastCell.getFormula());
        }else {
            cellToUpdate.setFormula(null);
        }
        return true;
    }

    /**
     * 将一个单元数据添加进paintData内核数据中，如果内核含有该数据，则覆盖
     * @param cell
     * @param paintData
     * @return
     */
    public static boolean addCellToPaintData(Cell cell, PaintData paintData){
        if (cell == null){
            return false;
        }
        int rowId = cell.getRowId();
        String colId = cell.getColId();
        HashMap<Integer,Row> rows = paintData.getPresentSheet().getRows();

        Row row = rows.get(rowId);
        if (row == null){
            row = new Row();
            row.setRowId(rowId);
            row.setUnitMap(new HashMap<String, Cell>());
        }
        row.getUnitMap().put(colId+rowId,cell);
        return true;
    }
}
