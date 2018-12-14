package com.example.coney.calc_formula.formula;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.mainView.PaintData;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by coney on 2018/12/6.
 */

public class CalUtilsTest {
    @Test
    public void expressToSufixTest(){
        String express = "(1+5*((2+1)+1)))";

        float result = Calculator.calculate(express);
        System.out.println(express);
        System.out.println("expect result:" + (1+5*((2+1)+1)));
        System.out.println("result:" + result);
    }

    @Test
    public void suffixTest(){
        String expres = "1+1.2+3";
//        String expres = "14";
        LinkedList<String> str = Calculator.expressToSuffix(expres);
        while (!str.isEmpty()){
            String a = str.pollFirst();
            System.out.print(""+ a + ",");
        }
    }

    @Test
    public void calSingleParaTest(){
        String f = "SUM(1,2,B5)";
        ArrayList<String> parm = new ArrayList<>();
        char[] fArray = f.toCharArray();
        int index = 0;
        String tmpStr;
        //找到第一个左括号
        while(index < fArray.length && fArray[index++] != '(');
        int i = index;
        while(index < fArray.length){
            if (fArray[index] == ',' || fArray[index] == ')'){
                tmpStr = f.substring(i,index);
                parm.add(tmpStr);
                i = index +1;
            }
            index++;
        }
        for (int j =0;j<3;j++){
            System.out.print(parm.get(j) + " ");
        }
    }
    @Test
    public void calTransFormTest(){

        Sheet sheet = null;
        try {
            sheet = new FileOper().loadFromXMLStream(new FileInputStream("c://tmp//sheet1_document.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Book book = new Book("test");
        book.getSheets().put(1,sheet);
        PaintData paintData = new PaintData(1,book);

        CalFormula calFormula = new CalFormula(paintData);
        String result = calFormula.trans_formula("D9 + 5+Sum(1,2,A10) + 1");
        System.out.println(result);

    }
    @Test
    public void getTypeTest(){
        String cal = "Sum(1,2,3)";
        System.out.println(CalUtils.getCalcStrType(cal));
    }
    @Test
    public void calSingleTest(){
        CalUtils utils = new CalUtils();

        Sheet sheet = null;
        try {
            sheet = new FileOper().loadFromXMLStream(new FileInputStream("c://tmp//sheet1_document.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Book book = new Book("test");
        book.getSheets().put(1,sheet);
        PaintData paintData = new PaintData(1,book);

        String calStr = "SUM(1, 2  ,     A10) ";
        String result = utils.calSingleFormula(calStr,paintData);
        System.out.print("" + result);
    }
    @Test
    public void calulateTest(){
        CalUtils utils = new CalUtils();

        Sheet sheet = null;
        try {
            sheet = new FileOper().loadFromXMLStream(new FileInputStream("c://tmp//sheet1_document.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Book book = new Book("test");
        book.getSheets().put(1,sheet);
        PaintData paintData = new PaintData(1,book);
        CalFormula calFormula = new CalFormula(paintData);

        String calStr = "13";
        String aStr = "SUM(1.5,2.56,10) ";
        String a = String.valueOf(Calculator.calculate(aStr,paintData));
        System.out.println(a + "*****");

    }
    @Test
    public void calculateTest(){
        String calStr = "SUM(1,2,10) ";
        String str = "13.0+1";
        float a = Calculator.calculate(str);
        System.out.println("***" + a);
    }
    @Test
    public void DataHelperTest(){
        DataHelper helper = new DataHelper();
        String id = "c10";
        String col = DataHelper.getColStrByStr(id);
        int row = DataHelper.getRowIdByStr(id);
        System.out.println(row);
        System.out.println(col);
    }
    @Test
    public void Test(){
        String calStr = "SUM(1,2,C10) ";
        System.out.println(calStr.trim());
        System.out.print(calStr);
    }
}
