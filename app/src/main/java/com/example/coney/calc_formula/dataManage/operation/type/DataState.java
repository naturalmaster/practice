package com.example.coney.calc_formula.dataManage.operation.type;

import com.example.coney.calc_formula.dataManage.data.Cell;

/**
 *
 * @author coney
 * @date 2018/12/10
 */

public class DataState extends Operation {
    /**
     * 单元格对象
     */
    public Cell cell;
    public DataState(float xOffset,float yOffset) {
        super(com.example.coney.calc_formula.dataManage.operation.type.OperationType.ALTER_DATA,xOffset,yOffset);
    }

    public DataState(Cell cell,float xOffset,float yOffset) {
        this(xOffset,yOffset);
        this.cell = cell;
    }
}
