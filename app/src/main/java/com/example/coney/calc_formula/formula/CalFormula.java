package com.example.coney.calc_formula.formula;

import com.example.coney.calc_formula.mainView.PaintData;

import java.util.LinkedList;

/**
 *
 * @author coney
 * @date 2018/12/6
 */

public class CalFormula {
    /**
     * 内核数据paintData
     */
    PaintData paintData;
    public CalFormula(PaintData paintData) {
        this.paintData = paintData;
    }

    /**
     * 将字符串表达式包含公式的转化为结果的运算
     * 如 sum(1,2,5) + 5  ===>  8+5;
     *    Max(B2,2,B5)+10  ===>  8+10  (假设B2为8，B5为4)
     * @param formula 待转化的字符串
     * @return 转化后的字符串
     */
    public String trans_formula(String formula){
        CalUtils utils = new CalUtils();
        if (formula == "" || formula.length() == 0 ) {
            return  "";
        }
        formula = formula.trim();
        //第一个表达式
        String firstExpression;
        int index = 0;
        while (index < formula.length() && !CalUtilMethod.isOperator(formula.charAt(index))){
            index++;
        }

        firstExpression = formula.substring(0,index);
        if (index == formula.length()){
            return utils.trans_formula_without_oper(firstExpression,paintData);
        }
        char operation = formula.charAt(index);
        return  utils.trans_formula_without_oper(firstExpression,paintData) + operation + trans_formula(formula.substring(index+1));
    }
}
