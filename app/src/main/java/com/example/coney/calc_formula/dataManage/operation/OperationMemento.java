package com.example.coney.calc_formula.dataManage.operation;

import com.example.coney.calc_formula.dataManage.operation.type.Operation;

import java.util.ArrayList;

/**
 *
 * @author coney
 * @date 2018/12/10
 */

public class OperationMemento {
    private ArrayList<Operation> operationList = new ArrayList<>(10);
    private int currentIndex = -1;
    private int boundIndex = 0;

    private OperationListener operationListener;


    public OperationMemento() {
    }

    /**
     * 每次操作都会进入以数组方式的循环队列
     * @param operation 本次操作
     */
    public void doSomeThing(Operation operation){
        disableRedo();
        operationList.add(++currentIndex,operation);
        boundIndex++;
        if (operationListener != null){
            operationListener.onUndoStatusChanged(true);
        }
    }
    /**
     * 将还原的栈清空
     */
    public void disableRedo() {
        boundIndex = currentIndex;
        if (operationListener != null){
            operationListener.onRedoStatusChanged(false);

        }
    }
    /**
     * 撤销
     */
    public Operation undo(){
       if (currentIndex < 0 ){
           return null;
       }
       if (operationListener != null){
           operationListener.onRedoStatusChanged(true);
       }
        if (currentIndex - 1 < 0){
            operationListener.onUndoStatusChanged(false);
        }
       return operationList.get(currentIndex--);
    }
    /**
     * 还原
     * @return
     */
    public Operation redo(){
        if ( boundIndex <= currentIndex ){
            return null;
        }
        //如果还原后，没有其他可还原的状态，那么就改变状态
        if (currentIndex +1 <= boundIndex && operationListener != null){
            operationListener.onRedoStatusChanged(false);
        }
        return operationList.get(++currentIndex);
    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }
}
