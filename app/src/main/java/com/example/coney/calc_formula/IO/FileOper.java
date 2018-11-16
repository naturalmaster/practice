package com.example.coney.calc_formula.IO;

import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.ColAttri;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.Row;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by coney on 2018/11/12.
 */

public class FileOper implements IXMLFileOper{
    public static String FILE_URL;
    public static Document document;

    @Override
    public void loadFile(String filePath) throws FileNotFoundException {
        loadFile(new File(filePath));
    }

    @Override
    public void loadFile(File file) throws FileNotFoundException {
        loadFile(new FileInputStream(file));
    }

    @Override
    public void loadFile(InputStream is){
//        initTableInfoFromFile(is);
    }

    public Book loadFile_(InputStream inputStream){
        Book book = new Book("assets");
        Sheet sheet = loadFromXML(inputStream);
        book.getSheets().put(1,sheet);
        return book;
    }

    public Sheet loadFromXML(InputStream is){
        Sheet sheet;
        HashMap<Integer,Row> rows = new HashMap<>();
        String  rangeSb = null;
        List<Element> rowList = null;
        SAXReader reader = new SAXReader();
        Element root;
        Document document = null;
        try {
            document = reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
            /*文件错误*/
        }
        FileOper.document = document;
        root = document.getRootElement();
        rangeSb = root.element("dimension").attributeValue("ref");
        rowList = root.element("sheetData").elements("row");


        //初始化列宽数据
        HashMap<String,ColAttri> colAttriHashMap = new HashMap<>();
        Element colElement = root.element("cols");
        if (colElement!=null){
            List<Element> colAttris = root.element("cols").elements("col");
                for (Iterator<Element> iterator = colAttris.iterator();iterator.hasNext();){
                    Element mElement = iterator.next();
                    int min;
                    int max;
                    float width;
                    min = Integer.parseInt(mElement.attributeValue("min"));
                    max = Integer.parseInt(mElement.attributeValue("max"));
                    width = Float.parseFloat(mElement.attributeValue("width"));
                    ColAttri colAttri = new ColAttri();
                    colAttri.setColWidth(width);
                    for (int i = min;i<=max;i++){
                        colAttri.setColStr(new DataHelper().numToColStr(i));
                        colAttriHashMap.put(colAttri.getColStr(),colAttri);
                    }
                }
        }

        //初始化rows数据结构，存放行的数据结构
        for (Iterator<Element> iterator = rowList.iterator(); iterator.hasNext();){
            Row row = new Row();
            Element rowElement = iterator.next();
            int rowId = Integer.parseInt(rowElement.attributeValue("r"));
            row.setRowId(rowId);
            row.setUnitMap(getRowUnits(rowElement));
            String rowHeight = rowElement.attributeValue("ht");
            if (rowHeight!=null){
                row.setRowHeight(Float.parseFloat(rowHeight));
            }
            rows.put(rowId,row);
        }

        sheet =  new Sheet(rangeSb.toString(),rows,colAttriHashMap);
        sheet.setSheetId(1);
        return sheet;
    }
    @Override
    public boolean saveFile(Book book) {
        return false;
    }


    /**
     * 根据XML文件流初始化TableInfo对象
     */
//    public void initTableInfoFromFile(InputStream is) {
//        HashMap<Integer,Row> rows = new HashMap<>();
//        String  rangeSb = null;
//        List<Element> rowList = null;
//        SAXReader reader = new SAXReader();
//        Element root;
//        try {
//            Document document = reader.read(is);
//            FileOper.document = document;
//            root = document.getRootElement();
//            rangeSb = root.element("dimension").attributeValue("ref");
//            rowList = root.element("sheetData").elements("row");
//        } catch (DocumentException e) {
//            e.printStackTrace();
//
//            /*文件错误*/
//        }
//
//        for (Iterator<Element> iterator = rowList.iterator(); iterator.hasNext();){
//            Row row = new Row();
//            Element rowElement = iterator.next();
//            int rowId = Integer.parseInt(rowElement.attributeValue("r"));
//            row.setRowId(rowId);
//            row.setUnitMap(getRowUnits(rowElement));
//            rows.put(rowId,row);
//        }
//        Sheet.setInstance(rangeSb,rows);
//    }

//    public List<Element> getRowList(InputStream is, StringBuilder rangeSb){
//        List<Element> rowList = null;
//        SAXReader reader = new SAXReader();
//        try {
//            Document document = reader.read(is);
//            FileOper.document = document;
//            Element root = document.getRootElement();
//            //表示的是有数据的格子范围，是一个矩形如，B4:E8
//            rangeSb.append(root.element("dimension").attributeValue("ref"));
//            rowList = root.element("sheetData").elements("row");
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//        return rowList;
//    }

    private HashMap<String,Cell> getRowUnits(Element rowElement){
        HashMap<String,Cell> unitsHashMap = new HashMap<>();
        List<Element> cellList = rowElement.elements("c");
        for (Iterator<Element> iter = cellList.iterator();iter.hasNext();){
            Element cell = iter.next();
            Cell cell1 = new Cell();
            cell1.setId(cell.attributeValue("r"));
            cell1.setValue(cell.element("v").getText());
            if (cell.element("f")!=null){
                cell1.setFormula(cell.element("f").getText());
            }
            unitsHashMap.put(cell1.getId(),cell1);
        }

        return unitsHashMap;
    }



    public void writeIntoFile(File file){
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        try {
            XMLWriter writer = new XMLWriter(new FileOutputStream(file),format);
            writer.write(document);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
