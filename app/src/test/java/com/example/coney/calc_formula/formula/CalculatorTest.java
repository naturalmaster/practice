package com.example.coney.calc_formula.formula;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.view.PaintData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * Created by coney on 2018/12/12.
 */
public class CalculatorTest {
    private PaintData paintData;
    @Before
    public void setUp() throws Exception {
        String filePath = "app/src/main/assets/sheet1_test2.xml";
        Sheet sheet = new FileOper().loadFromXMLStream(new FileInputStream(filePath));
        Book book = new Book("UNIT_TEST");
        book.getSheets().put(1,sheet);
        paintData = new PaintData(1,book);
    }
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void calculate() throws Exception {
        String calString = "Sum(1,5,b2)";
        Assert.assertEquals(6.0,Calculator.calculate(calString,paintData),0.1);
        calString = "Sum(1,5,b2) + 5 + Max(2,5,10)";
        Assert.assertEquals(21.0,Calculator.calculate(calString,paintData),0.1);
        calString = "Max(d4:d7)+2*2+6+10+Sum(1,2,3)";
        Assert.assertEquals(562.0,Calculator.calculate(calString,paintData),0.1);
    }

}