package com.example.coney.calc_formula.formula;

import com.example.coney.calc_formula.mainView.PaintData;

import java.util.LinkedList;
import java.util.Stack;

/**
 * 直接对字符串表达式运算的类
 * @author coney
 * @date 2018/12/6
 */

public class Calculator {

    public static float calculate(String str, PaintData paintData){
        CalFormula calFormula = new CalFormula(paintData);
        String express = calFormula.trans_formula(str);
        return calculate(express);
    }
    /**
     * 将字符串内的表达式进行四则运算，返回结果
     * @param expres 字符串表达式
     * @return 返回运算得到的结果
     */
    public static float calculate(String expres){
        LinkedList<String> suffixExpre = expressToSuffix(expres);
        Stack<Float> numStack = new Stack<>();
        while (!suffixExpre.isEmpty()){
            String tmp = suffixExpre.pollFirst();
            if (Character.isDigit(tmp.charAt(0))){
                numStack.push(Float.parseFloat(tmp));
            }else {
                float tmpF;
                switch (tmp){
                    case "+":
                        tmpF = numStack.pop() + numStack.pop();
                        numStack.push(tmpF);
                        break;
                    case "-":
                        tmpF = numStack.pop() - numStack.pop();
                        numStack.push(tmpF);
                        break;
                    case "*":
                        tmpF = numStack.pop() * numStack.pop();
                        numStack.push(tmpF);
                        break;
                    case "/":
                        tmpF = numStack.pop() / numStack.pop();
                        numStack.push(tmpF);
                        break;
                }
            }
        }
        return numStack.pop();
    }

    public static LinkedList<String> expressToSuffix(String express){
        express = express.trim();
        LinkedList<String> suffix = new LinkedList<>();
        Stack<Character> operStack = new Stack<>();
        char[] expressArray = express.trim().toCharArray();
        int index = 0;
        char tmpChar;
        StringBuilder tmp = new StringBuilder();
        while(index<express.length()){
            tmpChar = expressArray[index];
            if (tmpChar == ' '){
                index++;
                continue;
            }
            if(Character.isDigit( tmpChar ) || tmpChar == '.'){
                tmp.append(tmpChar);
            }else {
                //当前字符为运算符，将运算符之前的数字添加到后缀表达式
                if (tmp.length()!=0){
                    suffix.addLast(tmp.toString());
                }
                pushTmpChar(tmpChar,operStack,suffix);
                tmp = new StringBuilder();
            }
            if (index == (express.length() - 1)){
                if (tmp.length() != 0){
                    suffix.addLast(tmp.toString());
                }
                while (!operStack.empty()){
                    if (operStack.peek() == '('){
                        continue;
                    }
                    suffix.addLast(operStack.pop().toString());
                }
            }
            index++;
        }
        return suffix;
    }

    private static void pushTmpChar(char tmpChar,Stack<Character> operStack,LinkedList<String> suffixStack){

        if (operStack.empty()){
            operStack.push(tmpChar);
            return;
        }
        //弹出右括号左括号之前的所有运算符到后缀表达式suffixStack
        if (tmpChar == ')'){
            while (!operStack.empty()){
                if (operStack.peek() == '('){
                    operStack.pop();
                    return;
                }
                suffixStack.addLast(operStack.pop().toString());
            }
            return;
        }

        //优先级比较低的话
        while(true){
            if (!operStack.empty() && operStack.peek() != '(' && priori(tmpChar) <= priori(operStack.peek())){
                suffixStack.addLast(operStack.pop().toString());
            } else {
                operStack.push(tmpChar);
                return;
            }
        }
    }

    private static int priori(char oper){
        switch (oper){
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '(':
            case '（':
                return 9;
            case ')':
            case '）':
                return 0;
            default:
                return -1;

        }
    }
}
