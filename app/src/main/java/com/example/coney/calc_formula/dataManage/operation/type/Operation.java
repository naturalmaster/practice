package com.example.coney.calc_formula.dataManage.operation.type;

/**
 *
 * @author coney
 * @date 2018/12/10
 */

public class Operation {
    /**
     * 操作类型
     */
    private int OperationType;
    /**
     * 水平、垂直偏移量
     */
    private float xOffset;
    private float yOffset;

    public Operation(int operationType, float xOffset, float yOffset) {
        OperationType = operationType;
    }

    public int getOperationType() {
        return OperationType;
    }

    public float getxOffset() {
        return xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }
}
