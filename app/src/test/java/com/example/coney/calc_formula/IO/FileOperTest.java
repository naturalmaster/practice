package com.example.coney.calc_formula.IO;

import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Sheet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
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
    }

//    @Test
//    public void DocumentTest() throws IOException {
//        Sheet sheet = null;
//        FileOper fileOper = new FileOper();
//        try {
//            sheet = fileOper.loadFromXML(new FileInputStream("c://tmp//sheet1.xml"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        sheet.getRangeStr();
//        Document document = fileOper.createDocumentBySheet(sheet);
//        FileWriter output = null;
//        XMLWriter writer = null;
//        OutputFormat format = OutputFormat.createPrettyPrint();
//        try {
//            output = new FileWriter("c://tmp//sheet1_document.xml");
//            writer = new XMLWriter(output,format);
//            writer.write(document);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            output.close();
//            writer.close();
//        }
//    }

    @Test
    public void writeIntoFileTest(){
        FileOper oper = new FileOper();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File("c://tmp//sheet1.xml"));

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();

        Element element = root.element("cols");
        String a = element.attributeValue("asf");
//        List<Element> list = element.elements("col");
        /*for (Iterator<Element> iterator = list.iterator();iterator.hasNext();){
            Element element1 = iterator.next();
            String min = element1.attributeValue("min");
            String max = element1.attributeValue("max");
            String width = element1.attributeValue("width");
            System.out.println("col:1.min "+min);
            System.out.println("col:2.max "+max);
            System.out.println("col:3.width "+width);
        }*/
        System.out.println(" ******* ");
        System.out.println(" element "+(a==null));
//        System.out.println("element"+list==null);


    }
}
