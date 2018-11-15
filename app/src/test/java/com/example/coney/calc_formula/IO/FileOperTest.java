package com.example.coney.calc_formula.IO;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.File;

/**
 * Created by coney on 2018/11/13.
 */

public class FileOperTest {
    @Test
    public void writeIntoFileTest(){
        FileOper oper = new FileOper();
        SAXReader reader = new SAXReader();
        try {
            FileOper.document = reader.read(new File("c://tmp//sheet1.xml"));
            oper.writeIntoFile(new File("c://tmp//newSheet.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
}
