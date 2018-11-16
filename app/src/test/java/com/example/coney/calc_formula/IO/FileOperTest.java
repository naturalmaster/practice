package com.example.coney.calc_formula.IO;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by coney on 2018/11/13.
 */

public class FileOperTest {
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
