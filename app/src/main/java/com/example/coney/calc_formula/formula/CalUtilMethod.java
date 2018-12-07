package com.example.coney.calc_formula.formula;

import com.example.coney.calc_formula.mainView.PaintData;

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
    public static String calSumStr(ArrayList<String> strParam, PaintData paintData){
        float result = 0;
        CalUtils utils = new CalUtils();
        Iterator<String> iterator = strParam.iterator();
        while (iterator.hasNext()){
            String tmpStr = iterator.next();
            tmpStr = utils.trans_formula_without_oper(tmpStr,paintData);
            result += Float.parseFloat(tmpStr);
        }
        return result + "";
    }

    public static boolean isOperator(char ch){
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/'){
            return true;
        }
        return false;
    }
}
