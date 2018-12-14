package com.example.coney.calc_formula.formula;

<<<<<<< HEAD
import android.util.Log;

import com.example.coney.calc_formula.utils.CalcUtils;
import com.example.coney.calc_formula.view.PaintData;
=======
import com.example.coney.calc_formula.mainView.PaintData;
>>>>>>> tmp

import java.util.ArrayList;
import java.util.Iterator;

/**
 *为CalUtils类提供服务一个类，主要有各种函数的计算方法，以及一些工具方法
 * @author coney
 * @date 2018/12/7
 */

public class CalUtilMethod {
    /**
     * 计算类似SUM(A+B+C)的求和公式，以字符串形式返回得到的结果
     * @param strParam
     * @param paintData
     * @return
     */
<<<<<<< HEAD
    public static String sumCaltor(ArrayList<String> strParam, PaintData paintData){
        float result = 0;
        CalcUtils utils = new CalcUtils(paintData);
        Iterator<String> iterator = strParam.iterator();
        while (iterator.hasNext()){
            String tmpStr = iterator.next();
            tmpStr = utils.transFormulaWithoutOper(tmpStr,paintData);
=======
    public static String calSumStr(ArrayList<String> strParam, PaintData paintData){
        float result = 0;
        CalUtils utils = new CalUtils();
        Iterator<String> iterator = strParam.iterator();
        while (iterator.hasNext()){
            String tmpStr = iterator.next();
            tmpStr = utils.trans_formula_without_oper(tmpStr,paintData);
>>>>>>> tmp
            result += Float.parseFloat(tmpStr);
        }
        return result + "";
    }

<<<<<<< HEAD
    public static String maxCaltor(ArrayList<String> strParam,PaintData paintData){
        float maxVal = Float.MIN_VALUE;
        CalcUtils calUtil = new CalcUtils(paintData);
        for (String param:strParam){
            String resultString = calUtil.transFormulaWithoutOper(param,paintData);
            float resultFloat = Float.parseFloat(resultString);
            if (resultFloat>maxVal){
                maxVal = resultFloat;
            }
        }
        return String.valueOf(maxVal);
    }

=======
>>>>>>> tmp
    public static boolean isOperator(char ch){
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/'){
            return true;
        }
        return false;
    }
}
