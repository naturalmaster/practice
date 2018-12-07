package com.example.coney.calc_formula.formula;

import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.mainView.PaintData;

import java.util.ArrayList;

/**
 *
 * @author coney
 * @date 2018/12/6
 */

public class CalUtils {
    /**
     * 计算公式函数（可嵌套）
     * 如SUM(1,2,3) 返回6
     * SUM(A1,2,50)  返回52+A1
     * @param f 公式字符串
     * @param paintData 内核数据
     * @return 返回的结果是公式计算后的数字结果
     */
    public String calSingleFormula(String f, PaintData paintData){
        //公式的参数变量表
        ArrayList<String> parm = new ArrayList<>();
        char[] fArray = f.trim().toCharArray();
        int index = 0;
        String tmpStr;
        //找到第一个左括号
        while(index < fArray.length && fArray[index++] != '(');
        int i = index;
        while(index < fArray.length){
            if (fArray[index] == ',' || fArray[index] == ')'){
                tmpStr = f.substring(i,index).trim();
                tmpStr = trans_formula_without_oper(tmpStr,paintData);
                if (tmpStr != null){
                    parm.add(tmpStr);
                }
                i = index + 1;
            }
            index++;
        }
        switch (CalUtils.getCalcStrType(f)){
            case SUM:
                return CalUtilMethod.calSumStr(parm,paintData);
        }
        return "0";
    }


    /**
     * 将公式，表格ID，和普通数字分类转化为对应结果字符串
     * 比如： 15  ==>15
     *        B4 ==> 20  (假设内核数据B4为20)
     *        Sum(B2:B5) ==>转化为 B2+B3+B4+B5的结果
     * @param formulaStr  待转化的字符
     * @return 返回转换后的结果，一定是一个数字
     */
    public String trans_formula_without_oper(String formulaStr, PaintData paintData){
        String result;
        String formula = formulaStr.trim();
        if (Character.isDigit(formula.charAt(0))){
            result = formula;
        }else if (formula.charAt(formula.length()-1) == ')'){
            //公式
            result = calSingleFormula(formula,paintData);
        }else {
            //普通的单元格，可直接读内核数据
            int rowId = DataHelper.getRowIdByStr(formula);
            String colId = DataHelper.getColStrByStr(formula);
            DataHelper helper = new DataHelper();
            Cell cell = helper.getCell(rowId,colId,paintData);
            if (cell != null){
                result = cell.getValue();
            }else {
                result = null;
            }
        }
        return result;
    }

    public static FormulaType getCalcStrType(String calStr){
        int index = 0;
        calStr = calStr.toUpperCase();
        String typeString;
        while(index < calStr.length() && calStr.charAt(index) != '('){
            index++;
        }
        typeString = calStr.substring(0,index);
        switch (typeString.toUpperCase()){
            case "SUM":
                return FormulaType.SUM;
            case "MAX":
                return FormulaType.MAX;
            case "MIN":
                return FormulaType.MIN;
            case "AVERAGE":
                return FormulaType.AVERAGE;
            default:
                return FormulaType.UNKNOW;
        }
    }
}
