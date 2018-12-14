package com.example.coney.calc_formula.utils;

import android.util.Log;

import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.formula.CalUtilMethod;
import com.example.coney.calc_formula.formula.FormulaType;
import com.example.coney.calc_formula.view.PaintData;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author coney
 * @date 2018/12/6
 */

public class CalcUtils {
    private PaintData paintData;

    public CalcUtils(PaintData paintData) {
        this.paintData = paintData;
    }
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
        //i指向括号后的第一个字符
        int i = index;
        while(index < fArray.length){
            //再次碰到左括号，表示公式嵌套
            if(fArray[index] == '('){
                int bracketCnt = 1;
                while (++index < fArray.length && bracketCnt >0){
                    if (fArray[index] == '('){
                        bracketCnt++;
                    }else if (fArray[index] == ')'){
                        bracketCnt--;
                    }
                }
                tmpStr = f.substring(i,index);
                tmpStr = transFormulaWithoutOper(tmpStr,paintData);
                if (tmpStr != null){
                    parm.add(tmpStr);
                }
                i = index +1;
            }else if (fArray[index] == ',' || fArray[index] == ')'){
                tmpStr = f.substring(i,index).trim();
                if (containMultiCell(tmpStr)){
                    fetchMultiCell(parm,tmpStr,paintData);
                    tmpStr = null;
                }else {
                    tmpStr = transFormulaWithoutOper(tmpStr,paintData);
                }
                if (tmpStr != null){
                    parm.add(tmpStr);
                }
                i = index + 1;
            }
            index++;
        }
        switch (CalcUtils.getCalcStrType(f)){
            case SUM:
                return CalUtilMethod.sumCaltor(parm,paintData);
            case MAX:
                return CalUtilMethod.maxCaltor(parm,paintData);
            default:
                break;
        }
        return "0";
    }
    /**
     * 将公式，表格ID，和普通数字分类转化为对应结果字符串
     * 比如： 15  ==>15
     *        B4 ==> 20  (假设内核数据B4为20)
     *        Sum(B2:B5) ==>转化为 B2+B3+B4+B5的结果
     * @param formulaStr  待转化的字符
     * @return 1、B2:B5含有冒号的情况会直接返回字符串，其他返回结果，是一个数字结果
     */
    public String transFormulaWithoutOper(String formulaStr, PaintData paintData){
        String result = null;
        String formula = formulaStr.trim();
        if (formula.length() == 0 || formula == null){
            return "";
        }
        if (Character.isDigit(formula.charAt(0))){
            result = formula;
        }else if (formula.charAt(formula.length()-1) == ')'){
            //公式
            result = calSingleFormula(formula,paintData);
        }else {
                //普通的或连续单元格，可直接读内核数据
                int rowId = DataHelper.getRowIdByStr(formula);
                String colId = DataHelper.getColStrByStr(formula);
                DataHelper helper = new DataHelper(paintData);
                Cell cell = helper.getCell(rowId,colId);
                if (cell != null){
                    result = cell.getValue();
                }else {
                    result = null;
                }
            }
        return result;
    }
//
//    private void fetchMultiCell(ArrayList<String> parm,String mutilStr,PaintData paintData){
//        mutilStr = mutilStr.trim();
//        int[] rowBounds = new int[2];
//        String[] colBounds = new String[2];
//        DataHelper.convertRangeStr(mutilStr,rowBounds,colBounds);
//        int[] intColBounds = new int[2];
//        intColBounds[0] = DataHelper.colStrToNum(colBounds[0]);
//        intColBounds[1] = DataHelper.colStrToNum(colBounds[1]);
//        for (int col = intColBounds[0];col <= intColBounds[1];col++){
//            String colStr = DataHelper.numToColStr(col);
//            for (int row = rowBounds[0]; row <= rowBounds[1];row++){
//                Cell cell = new DataHelper(paintData).getCell(row,colStr);
//                if (cell != null && cell.getValue() != ""){
//                    parm.add(cell.getValue());
//                }
//            }
//        }
//        return;
//    }

    /**
     * 处理mutilStr中含有B2:B6这类型的字符串
     * @param parm 待放入的ArrayList
     * @param mutilStr 待处理字符串形式: ${colStr}${rowInt}:${colStr}${rowInt}
     * @param paintData 内核数据
     */
    private void fetchMultiCell(ArrayList<String> parm,String mutilStr,PaintData paintData){
        mutilStr = mutilStr.trim().toUpperCase();
        String[] paramArray = parseLastParam(mutilStr);
        DataHelper helper = new DataHelper(paintData);
        for (String cellId : paramArray){
                Cell cell = helper.getCell(cellId);
                //并且还不能是非数字
                if (cell != null && cell.getValue() != "" && isIntOrDouble(cell.getValue())){
                    parm.add(cell.getValue());
                }
        }
        return;
    }

    /**
     * 判断Str是否为B2:C5这种形式的参数
     * @param str 带判断的字符串
     * @return 是则返回true，否则返回false
     */
    public static  boolean containMultiCell(String str) {
        char[] strArray = str.toCharArray();
        int len = str.length();
        int cnt = 0;
        int mid = str.length()/2;
        while(true){
            if ((mid + cnt) < len && strArray[mid+cnt] == ':'){
                return true;
            }
            if ((mid - cnt) >= 0 && strArray[mid-cnt] == ':'){
                return true;
            }

            if ((mid + cnt) >=len && (mid - cnt) <0){
                return  false;
            }
            cnt++;
        }
    }

    /**
     * 将全角的括号以及都好转化为英文半角的括号，逗号
     * @param str
     */
    public static String fullCharToHalf(String str){
        StringBuilder sb = new StringBuilder(str.length());
        char[] chArray = str.toCharArray();
        for (int i = 0;i < chArray.length;i++){
            char tmpChar = chArray[i];
            if (tmpChar == '，'){
                sb.append(',');
            }else if (tmpChar == '（'){
                sb.append('(');
            }else if (tmpChar == '）'){
                sb.append(')');
            }else if (tmpChar == '＝'){
                sb.append('=');
            }else if (tmpChar == '；'){
                sb.append(';');
            }else  if (tmpChar == '：'){
                sb.append(":");
            }else {
                sb.append(tmpChar);
            }
        }
        return sb.toString();
    }

    /**
     * 返回公式的类型
     * @param calStr 公式
     * @return 返回FormulaType Enum 类型
     */
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

    public static boolean isIntOrDouble(String str){
        String regex = "^\\d+(\\.\\d+)?";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }
    /**
     * 判断是否为浮点数，包括double和float
     * @param str 传入的字符串
     * @return 是浮点数返回true,否则返回false
     */
    public static boolean isDouble(String str) {
        String regex = "^\\d+(\\.\\d+)+";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }
    /**
     * 判断公式是否合法
     * @param str
     * @return
     */
    public static boolean isLegalStr(String str){
        //操作符正则表达式
        String oper = "\\s*[\\+\\-\\*/%]\\s*" ;
        //表格id或直接数字正则表达式
        String gribId = "\\s*(([a-zA-Z]*)\\d+|" +
                //支持小数
                "\\d+\\.\\d)\\s*";
        //表格ID/数字/公式的表达式匹配
        String para = "\\s*(" + gribId + "|" + "[a-zA-Z]*\\([\\.\\:\\,\\d]*\\))\\s*";
        //匹配公式的表达式
        String formulaRegix = "\\s*[a-zA-Z]+\\s*\\(" + "(" + para + "(\\,|\\)|\\:))+";
//        String totalRegex = "^(" + para + "|" + gribId + ")"
//                + "\\s*"+"(" + oper  +"(" + para + "|" + gribId + ")"+ ")+";
        String param = "("+para+"|"+formulaRegix+")";
        //总的表达式
        String totalRegex = "^" + param
                + "\\s*"+"(" + oper + param + ")*";
        Pattern pattern = Pattern.compile(totalRegex);

        return pattern.matcher(str).matches();

    }

    /**将B2:B5这样的字符串转换为B2,B3,B4,B5
     * @param param 待转换的字符串
     * @return
     */
    public static String[] parseLastParam(String param){
        int[] rowRange = new int[2];
        String[] colRange = new String[2];
        DataHelper.convertRangeStr(param,rowRange,colRange);
        int[] colIntRange = {DataHelper.colStrToNum(colRange[0]),
                DataHelper.colStrToNum(colRange[1])};

        int rangNum = rowRange[1] - rowRange[0]+1;
        rangNum += (colIntRange[1] - colIntRange[0])*rangNum;
        String[] result = new String[rangNum];
        int index = 0;
        for (int colIndex = colIntRange[0];colIndex <= colIntRange[1];colIndex++){
            String colStrIndex = DataHelper.numToColStr(colIndex);
            for (int rowIndex = rowRange[0];rowIndex <= rowRange[1];rowIndex++){
                result[index++] = colStrIndex + rowIndex;
            }
        }
        return result;
    }
}
