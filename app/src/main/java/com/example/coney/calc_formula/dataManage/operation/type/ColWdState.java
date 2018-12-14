package com.example.coney.calc_formula.dataManage.operation.type;

import com.example.coney.calc_formula.dataManage.data.ColAttri;

/**
 * Created by coney on 2018/12/10.
 */

public class ColWdState extends Operation {
    /**
     * 列宽数据对象
     */
    public ColAttri colAttri;

    public ColWdState(float xOffset,float yOffset) {
        super(com.example.coney.calc_formula.dataManage.operation.type.OperationType.ALTER_COL_WD, xOffset, yOffset);
    }
}
