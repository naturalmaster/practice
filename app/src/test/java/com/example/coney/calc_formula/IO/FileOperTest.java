package com.example.coney.calc_formula.IO;

import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Sheet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by coney on 2018/11/13.
 */

public class FileOperTest {
    @Test
<<<<<<< HEAD
    public void loadFileTest() throws FileNotFoundException {
        String filePath = "app/src/main/assets/sheet1_test2.xml";
        String MSG_TAG = "file 读取错误";
        Sheet sheet = new FileOper().loadFromXMLStream(new FileInputStream(filePath));
        String D10Val = sheet.getRows().get(10).getUnitMap().get("D10").getValue();
        Assert.assertEquals("102",D10Val);
       Assert.assertEquals(MSG_TAG,"B3:E17",sheet.getRangeStr());
=======
    public void loadFileTest(){
        Sheet sheet = null;
        try {
            sheet = new FileOper().loadFromXMLStream(new FileInputStream("c://tmp//sheet1_document.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (HashMap.Entry<Integer,Row> entry:sheet.getRows().entrySet()){
            System.out.println("key"+entry.getKey()+" rowHeightValue:"+entry.getValue().getRowHeight());
        }
>>>>>>> tmp
    }

}
