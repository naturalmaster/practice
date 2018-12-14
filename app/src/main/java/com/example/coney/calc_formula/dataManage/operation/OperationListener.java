package com.example.coney.calc_formula.dataManage.operation;

/**
 *
 * @author coney
 * @date 2018/12/13
 */

public interface OperationListener {
    /** 还原状态变化时调用
     * @param enabled true表示可还原，反之表示不可还原
     */
    void onRedoStatusChanged(boolean enabled);

    /**
     * 撤销状态变化时调用
     * @param enabled true表示可撤销，否则为不可撤销
     */
    void onUndoStatusChanged(boolean enabled);
}
