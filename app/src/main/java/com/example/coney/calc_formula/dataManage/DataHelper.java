package com.example.coney.calc_formula.dataManage;

import android.util.Log;

import com.example.coney.calc_formula.IO.FileOper;
import com.example.coney.calc_formula.MyApplication;
import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Unit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMDocument;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by coney on 2018/11/12.
 */

public class DataHelper implements IXMLParser{
    public static final String TAG = "dataHelper";


    /**
     * 根据文件对象，初始化TableInfo数据对象
     * @param file
     */
    @Override
    public void initTableInfoFromFile(File file) {
        try {
            initTableInfoFromFile(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据XML文件流初始化TableInfo对象
     * @param is 表格xml文件流
     */
    @Override
    public void initTableInfoFromFile(InputStream is) {
        HashMap<Integer,Row> rows = new HashMap<>();
        StringBuilder rangeSb =new StringBuilder() ;
        List<Element> rowList = getRowList(is,rangeSb);
        for (Iterator<Element> iterator = rowList.iterator(); iterator.hasNext();){
            Row row = new Row();
            Element rowElement = iterator.next();
            int rowId = Integer.parseInt(rowElement.attributeValue("r"));
            row.setRowId(rowId);
            row.setUnitMap(getRowUnits(rowElement));
            rows.put(rowId,row);
        }
        TableInfo.setInstance(rangeSb.toString(),rows);
    }


    /**
     * 根据屏幕坐标获得对应的单元格的数据对象
     * @param x
     * @param y
     * @return
     */
    public Unit getUnitByXY(float x,float y){
        Integer rowId = yIndexToRowId(y);
        String colStr = xIndexToColId(x);
        return getUnit(rowId,colStr);
    }

    /**
     * 根据行号，列值返回Unit数据对象
     * @param rowId 行号
     * @param colStr 列值
     * @return Unit单元格对象
     */
    private Unit getUnit(Integer rowId,String colStr){
        HashMap<Integer,Row> rows = TableInfo.getInstance().getRows();
        if (!rows.containsKey(rowId))
            return null;
        HashMap<String,Unit> unitMap = rows.get(rowId).getUnitMap();
        if (unitMap == null)
            return null;

        return unitMap.get(colStr+rowId);
    }

    /**
     * 根据x值屏幕坐标返回对应的列号
     * @param x x坐标
     * @return  列号
     */
    public String xIndexToColId(float x){
        int rowWidth = Table.getColSpace();
        float xOffset = Table.getHorizonalOffset();
//        int tmpNum = (x+xOffset%rowWidth-2*rowWidth)/rowWidth+xOffset/rowWidth;
        float tmpNum = ((x-rowWidth+xOffset)/rowWidth);
        return ""+((char)('A'+tmpNum));

    }

    /**
     * 根据y值屏幕坐标返回对应的行号
     * @param y y坐标
     * @return  以Integer对象形式返回行号
     */
    public Integer yIndexToRowId(float y){
        float rowHeight = Table.getRowSpace();
        float yOffset = Table.getVerticalOffset();

//        int tmpNum = (y+yOffset%rowHeight-2*rowHeight)/rowHeight+yOffset/rowHeight+1;
//        int tmpNum = ((y+yOffset%rowHeight-2*rowHeight)+yOffset)/rowHeight;
        float tmpNum = (((y-rowHeight+yOffset)/rowHeight)+1);
        Log.d("action_u","y:"+y+" result:"+tmpNum);
        return new Integer((int) tmpNum);
    }

    /**
     * 将列号字符转化为第几列
     * @param colStr
     * @return
     */
    public int colStrToNum(String colStr){
        return colStr.charAt(0)-'A'+1;
    }

    public List<Element> getRowList(InputStream is, StringBuilder rangeSb){
        List<Element> rowList = null;
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(is);
            FileOper.document = document;
            Element root = document.getRootElement();
            //表示的是有数据的格子范围，是一个矩形如，B4:E8
            rangeSb.append(root.element("dimension").attributeValue("ref"));
            rowList = root.element("sheetData").elements("row");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return rowList;
    }

    private HashMap<String,Unit> getRowUnits(Element rowElement){
        HashMap<String,Unit> unitsHashMap = new HashMap<>();
        List<Element> cellList = rowElement.elements("c");
        for (Iterator<Element> iter = cellList.iterator();iter.hasNext();){
            Element cell = iter.next();
            Unit unit = new Unit();
            unit.setId(cell.attributeValue("r"));
            unit.setValue(cell.element("v").getText());
            if (cell.element("f")!=null){
                unit.setFormula(cell.element("f").getText());
            }
            unitsHashMap.put(unit.getId(),unit);
        }

        return unitsHashMap;
    }
}
