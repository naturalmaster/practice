package com.example.coney.calc_formula.dataManage.operation.type;

import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.operation.type.Operation;

/**
 *
 * @author coney
 * @date 2018/12/10
 */

public class RowHtState extends Operation {
    /**
     * 行数据对象
     */
    public Row row;

    public RowHtState(float xOffset,float yOffset) {
        super(com.example.coney.calc_formula.dataManage.operation.type.OperationType.ALTER_ROW_HT,xOffset ,xOffset );
    }
}
