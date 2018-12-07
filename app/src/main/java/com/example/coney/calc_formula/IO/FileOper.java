package com.example.coney.calc_formula.IO;

import android.os.Environment;
import android.util.Log;

import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.ColAttri;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.Row;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author coney
 * @date 2018/11/12
 */

public class FileOper implements IXMLFileOper{
    public static Document document;
    public static final String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/my_excel";
//    public static final String FILE_DIR = "/my_excel";

    @Override
    public Book loadFile(String filePath) throws FileNotFoundException {
        return loadFile(new File(filePath));
    }

    @Override
    public Book loadFile(File file) throws FileNotFoundException {
        return loadFile(new FileInputStream(file));
    }

    @Override
    public boolean loadFile(File file, Book book,int sheetId) throws FileNotFoundException {
        if (book == null) {
            return  false;
        }
        Sheet sheet = loadFromXMLStream(new FileInputStream(file));
        if (sheet != null){
            Log.d("book_null",(sheet.getRangeStr())+" sheet表为空");
        }
        book.getSheets().put(sheetId,sheet);
        return true;
    }

    @Override
    public Book loadFile(InputStream is){
        Book book = new Book(FILE_DIR);
        Sheet sheet1 = loadFromXMLStream(is);
        book.getSheets().put(1,sheet1);
        return book;
    }

    /**
     * 调试用方法
     */
    public Book loadFileAssets(InputStream inputStream){
        Book book = new Book("assets");
        Sheet sheet = loadFromXMLStream(inputStream);
        book.getSheets().put(1,sheet);
        return book;
    }

    public Sheet loadFromXMLStream(InputStream is){
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
        HashMap<String,ColAttri> colAttriHashMap = new HashMap<>(5);
        Element colElement = root.element("cols");
        if (colElement != null){
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
                        colAttri.setColStr(DataHelper.numToColStr(i));
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
            if (rowHeight != null){
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

    @Override
    public boolean saveFile(Book book, String fileName) {

        return false;
    }
    @Override
    public void saveSheetToFile(Sheet sheet,String sheetName) throws IOException {
        File dir = new File(FILE_DIR);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(FILE_DIR,sheetName + ".xml");
        if (file.exists()){
            file.delete();
        }
        Document document = createDocumentBySheet(sheet);

        OutputFormat format = OutputFormat.createPrettyPrint();
        FileWriter fileWriter = new FileWriter(file);
        XMLWriter writer = new XMLWriter(fileWriter,format);
        writer.write(document);
        fileWriter.close();
        writer.close();
    }

    private Document createDocumentBySheet(Sheet sheet){
        int[] rowRange = new int[2];
        String[] colRange = new String[2];

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("worksheet");

        Element dimension = root.addElement("dimension");
        String rangeStr = sheet.getRangeStr();
        dimension.addAttribute("ref",rangeStr);

        DataHelper.convertRangeStr(rangeStr,rowRange,colRange);
       // cols属性的编写
        HashMap<String, ColAttri> colAttriHashMap = sheet.getColAttriMap();
        if (colAttriHashMap.size() > 0){
            Element cols = root.addElement("cols");
            float width = 0;
            int min = 0,max = 0;
            int lastIndex = DataHelper.colStrToNum(colRange[1]);

            for (int i = DataHelper.colStrToNum(colRange[0]);i <= lastIndex;i++){
                String col = DataHelper.numToColStr(i);
                ColAttri colAttri = colAttriHashMap.get(col);
                if (colAttri == null){
                    continue;
                }
                if (width == 0){
                    min = max = i;
                    width = colAttri.getColWidth();
                }
                if (width  == colAttri.getColWidth()){
                    max = i;
                }else {
                    Element colElement = cols.addElement("col");
                    colElement.addAttribute("min",String.valueOf(min));
                    colElement.addAttribute("max",String.valueOf(max));
                    colElement.addAttribute("width", String.valueOf(width));
                    min = i;
                    max = i;
                    width = colAttri.getColWidth();
                }
                if (i == lastIndex && width != 0){
                    Element colElement = cols.addElement("col");
                    colElement.addAttribute("min",String.valueOf(min));
                    colElement.addAttribute("max",String.valueOf(max));
                    colElement.addAttribute("width", String.valueOf(width));
                }
            }
        }
     //row属性的编写
        Element sheetData = root.addElement("sheetData");
        for (int i = rowRange[0];i <= rowRange[1];i++){
            Row row = sheet.getRows().get(i);
            if (row == null){
                continue;
            }else{
                //遍历row填入c
                Element rowElement = sheetData.addElement("row");
                rowElement.addAttribute("r",String.valueOf(i));
                if (row.isHasRowHeight()){
                    rowElement.addAttribute("ht", String.valueOf(row.getRowHeight()));
                }
                //遍历row里面的cell
                HashMap<String,Cell> cellHashMap = row.getUnitMap();
                for (HashMap.Entry<String,Cell> entry : cellHashMap.entrySet()){
                    Cell cell = entry.getValue();
                    Element cellElement = rowElement.addElement("c");
                    cellElement.addAttribute("r",cell.getId());
                    cellElement.addElement("v").addText(cell.getValue());
                    if (cell.isHasFormula()){
                        cellElement.addElement("f").addText(cell.getFormula());
                    }
                }
            }
        }
        return document;
    }

    private HashMap<String,Cell> getRowUnits(Element rowElement){
        HashMap<String,Cell> unitsHashMap = new HashMap<>();
        List<Element> cellList = rowElement.elements("c");
        for (Iterator<Element> iter = cellList.iterator();iter.hasNext();){
            Element cellElement = iter.next();
            Cell cel1 = new Cell();
            cel1.setId(cellElement.attributeValue("r"));
            cel1.setValue(cellElement.element("v").getText());
            if (cellElement.element("f") != null){
                cel1.setFormula(cellElement.element("f").getText());
            }
            unitsHashMap.put(cel1.getId(),cel1);
        }

        return unitsHashMap;
    }

}
