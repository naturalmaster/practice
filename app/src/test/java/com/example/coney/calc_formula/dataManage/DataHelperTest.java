package com.example.coney.calc_formula.dataManage;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.utils.CalcUtils;
import com.example.coney.calc_formula.view.PaintData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

import javax.crypto.spec.DHGenParameterSpec;

import static org.junit.Assert.*;

/**
 * Created by coney on 2018/12/13.
 */
public class DataHelperTest {
    private PaintData paintData;
    private DataHelper helper;
    @Before
    public void setUp() throws Exception {
        String filePath = "app/src/main/assets/sheet1_test2.xml";
        Sheet sheet = new FileOper().loadFromXMLStream(new FileInputStream(filePath));
        Book book = new Book("UNIT_TEST");
        book.getSheets().put(1,sheet);
        paintData = new PaintData(1,book);
        helper = new DataHelper(paintData);
    }

<<<<<<< HEAD
    @After
    public void tearDown() throws Exception {

=======
    @Test
    public void colStrToNumTest(){
        helper = new DataHelper();
        int res = helper.colStrToNum("AA");
        System.out.println("result: "+res);
>>>>>>> tmp
    }

    @Test
    public void getCell() throws Exception {
       Cell cell =  helper.getCell("B3");
        Assert.assertEquals("56",cell.getValue());
    }

    @Test
    public void convertRangeStr() throws Exception {
        String rangeStr = "Ba2:bd9";
        int[] rangeArray = new int[2];
        String[] colRangeArray = new String[2];

        DataHelper.convertRangeStr(rangeStr,rangeArray,colRangeArray);
        Assert.assertArrayEquals(new int[]{2,9},rangeArray);
        Assert.assertArrayEquals(new String[]{"BA","BD"},colRangeArray);

    }

    @Test
    public void getColStrByStr() throws Exception {
        String id = "B17";
        String expected = "B";
        Assert.assertEquals(expected,DataHelper.getColStrByStr(id));
    }

    @Test
    public void getRowIdByStr() throws Exception {
        String id = "B17";
        int expected = 17;
        Assert.assertEquals(expected,(((Integer) DataHelper.getRowIdByStr(id)).intValue()));
    }

    @Test
    public void colStrToNum() throws Exception {
       String colStr = "A";
       Assert.assertEquals(1,DataHelper.colStrToNum(colStr));
       colStr = "AB";
       Assert.assertEquals(28, DataHelper.colStrToNum(colStr));
       colStr = "dc";
       Assert.assertEquals(107, DataHelper.colStrToNum(colStr));
    }

    @Test
    public void numToColStr() throws Exception {
        int num = 1;
        Assert.assertEquals("A",DataHelper.numToColStr(num));

        num = 26;
        Assert.assertEquals("Z",DataHelper.numToColStr(num));

        num = 82;
        Assert.assertEquals("CD",DataHelper.numToColStr(num));
        num = 27;
        Assert.assertEquals("AA",DataHelper.numToColStr(num));
    }
}