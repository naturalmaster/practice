package com.example.coney.calc_formula.utils;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.formula.FormulaType;
import com.example.coney.calc_formula.view.PaintData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * Created by coney on 2018/12/12.
 */
public class CalcUtilsTest {
    private PaintData paintData;
    private CalcUtils calcUtils;

    @Before
    public void setUp() throws Exception {
        String filePath = "app/src/main/assets/sheet1_test2.xml";
        Sheet sheet = new FileOper().loadFromXMLStream(new FileInputStream(filePath));
        Book book = new Book("UNIT_TEST");
        book.getSheets().put(1,sheet);
        paintData = new PaintData(1,book);
        calcUtils = new CalcUtils(paintData);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void calSingleFormulaTest() throws Exception {
        String formula = "SUM(1,2)";
        String result = calcUtils.calSingleFormula(formula,paintData);
        Assert.assertEquals("3.0",result);

        formula = "Sum(E4:E7)";
        Assert.assertEquals("413.0",calcUtils.calSingleFormula(formula,paintData));
        formula = "Max(d4:d7)";
        Assert.assertEquals("562.0",calcUtils.calSingleFormula(formula,paintData));

        formula = "Sum(B2:B5)";
        Assert.assertEquals("61.0",calcUtils.calSingleFormula(formula,paintData));

        formula = "sum(1,5,E4,max(2,b5,29))";
        Assert.assertEquals("79.0",calcUtils.calSingleFormula(formula,paintData));
    }

    @Test
    public void parseLastParamTest(){
        String[] array1 = {"B2","B3","B4","B5"};
        CalcUtils.parseLastParam("B2:B5");
        Assert.assertArrayEquals(array1,CalcUtils.parseLastParam("B2:B5"));
        String[] array2 = {"B2","B3","B4","B5",
                "C2","C3","C4","C5"};
        Assert.assertArrayEquals(array2,CalcUtils.parseLastParam("B2:C5"));
        String[] array3 = {"B2","B3","B4","B5",
                "C2","C3","C4","C5",
                "D2","D3","D4","D5",
                "E2","E3","E4","E5",
                "F2","F3","F4","F5"};
        Assert.assertArrayEquals(array3,CalcUtils.parseLastParam("B2:F5"));
    }
    @Test
    public void transFormulaWithoutOperTest() throws Exception {
        String formula = "Sum(B2:B5)";
        Assert.assertEquals("61.0",calcUtils.transFormulaWithoutOper(formula,paintData));

        formula = "B3";
        Assert.assertEquals("56",calcUtils.transFormulaWithoutOper(formula,paintData));

        formula = "B2";
        Assert.assertNull(calcUtils.transFormulaWithoutOper(formula,paintData));

        formula = "22";
        Assert.assertEquals("22",calcUtils.transFormulaWithoutOper(formula,paintData));
        formula = "254.45";
        Assert.assertEquals("254.45",calcUtils.transFormulaWithoutOper(formula,paintData));





    }

    @Test
    public void fullCharToHalfTest() throws Exception {
        String fullChar = "SUM（15，45，3）";
        Assert.assertEquals("SUM(15,45,3)",CalcUtils.fullCharToHalf(fullChar));

        fullChar = "sum(B2：B5)";
        Assert.assertEquals("sum(B2:B5)",CalcUtils.fullCharToHalf(fullChar));
    }

    @Test
    public void getCalcStrTypeTest() throws Exception {
        String calcStr = "SUM(15,68,5)";
        Assert.assertEquals(FormulaType.SUM,CalcUtils.getCalcStrType(calcStr));

        calcStr = "MAX(15,68,5)";
        Assert.assertEquals(FormulaType.MAX,CalcUtils.getCalcStrType(calcStr));

        calcStr = "AVERAGE(15,68,5)";
        Assert.assertEquals(FormulaType.AVERAGE,CalcUtils.getCalcStrType(calcStr));

        calcStr = "MIN(15,68,5)";
        Assert.assertEquals(FormulaType.MIN,CalcUtils.getCalcStrType(calcStr));
    }

    @Test
    public void isIntOrDoubleTest(){
        String res = "5.2";
        Assert.assertTrue(CalcUtils.isIntOrDouble(res));
        res = "5";
        Assert.assertTrue(CalcUtils.isIntOrDouble(res));


    }
    @Test
    public void isDoubleTest() throws Exception {
        String num = "52.5";
        Assert.assertTrue(CalcUtils.isDouble(num));

        num = "25.0";
        Assert.assertTrue(CalcUtils.isDouble(num));

        num = "5";
        Assert.assertFalse(CalcUtils.isDouble(num));
    }

    @Test
    public void isLegalStrTest() throws Exception {
        String str = "Sum(1,B2,B5,B6)+5+Max(5,6,8)";
        Assert.assertTrue(CalcUtils.isLegalStr(str));

        str = "2.1 +   Sum(1,   B2,B5,B6) +5+Max(5,6,8)";
        Assert.assertTrue(CalcUtils.isLegalStr(str));

        str = "Sum(1,B2,B5,B6)+5+Max(5,6,8)+";
        Assert.assertFalse(CalcUtils.isLegalStr(str));

        str = "Sum(1,B2,B5,B6)+5+Max(5,6,8,黄)+2";
        Assert.assertFalse(CalcUtils.isLegalStr(str));

        str = "asafasf";
        Assert.assertFalse(CalcUtils.isLegalStr(str));

    }

}