package com.example.coney.calc_formula.formula;

import com.example.coney.calc_formula.utils.CalcUtils;
import com.example.coney.calc_formula.view.PaintData;

/**
 *
 * @author coney
 * @date 2018/12/6
 */

public class FormulaTranser {
    /**
     * 内核数据paintData
     */
    PaintData paintData;
    public FormulaTranser(PaintData paintData) {
        this.paintData = paintData;
    }

    /**
     * 将字符串表达式包含公式的转化为结果的运算
     * 如 sum(1,2,5) + 5  ===>  8+5;
     *    Max(B2,2,B5)+10  ===>  8+10  (假设B2为8，B5为4)
     * @param formula 待转化的字符串
     * @return 转化后的字符串
     */
    public String transFormula(String formula){
        CalcUtils utils = new CalcUtils(paintData);

        if (formula == null || formula == "" || formula.length() == 0 ) {
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
            return utils.transFormulaWithoutOper(firstExpression,paintData);
        }
        char operation = formula.charAt(index);
        return  utils.transFormulaWithoutOper(firstExpression,paintData) + operation + transFormula(formula.substring(index+1));
    }
}
