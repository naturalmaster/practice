package com.example.coney.calc_formula.dataManage;

import android.util.Log;

import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.ColAttri;
import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.formula.CalFormula;
import com.example.coney.calc_formula.formula.Calculator;
import com.example.coney.calc_formula.mainView.PaintData;

import java.util.HashMap;

/**
 *
 * @author coney
 * @date 2018/11/12
 */

public class DataHelper {
    public static final String TAG = "dataHelper";
    /**
     * 根据屏幕坐标获得对应的单元格的数据对象
     * @param x
     * @param y
     * @return
     */
    public Cell getCellByXY(float x,float y,PaintData paintData){
        Integer rowId = yIndexToRowId(y,paintData);
        String colStr = xIndexToColId(x,paintData);
        return getCell(rowId,colStr,paintData.getPresentSheet());
    }

    public Cell getCell(int rowId,String colStr,PaintData paintData){
      return getCell(rowId,colStr,paintData.getPresentSheet());
    }
    /**
     * 根据行号，列值返回Unit数据对象
     * @param rowId 行号
     * @param colStr 列值
     * @return Unit单元格对象
     */
    private Cell getCell(Integer rowId, String colStr,Sheet sheet){
        HashMap<Integer,Row> rows = sheet.getRows();
        if (!rows.containsKey(rowId)){
            return null;
        }
        HashMap<String,Cell> cellMap = rows.get(rowId).getUnitMap();
        if (cellMap == null){
            return null;

        }
        return cellMap.get(colStr+rowId);
    }

    /**
     * 根据x值屏幕坐标返回对应的列号
     * @param x x坐标
     * @return  列号
     */
    public String xIndexToColId(float x, PaintData paintData){
        x -= paintData.getDefColWidth();
        HashMap<String,ColAttri> colAttriHashMap = paintData.getPresentSheet().getColAttriMap();
        int colNum = 0;
        float xOffset = paintData.getHorizontalOffset();
        float rawX = 0;
        float presentColWidth;
        do {
            colNum++;
            if (colAttriHashMap.get(DataHelper.numToColStr(colNum)) == null){
                presentColWidth = paintData.getDefColWidth();
            }else{
                presentColWidth = colAttriHashMap.get(DataHelper.numToColStr(colNum)).getColWidth();
            }
            rawX += presentColWidth;
        }while (rawX < xOffset + x);
        return DataHelper.numToColStr(colNum);

    }

    /**
     * 根据y值屏幕坐标返回对应的行号
     * @param y y坐标
     * @return  以Integer对象形式返回行号
     */
    public Integer yIndexToRowId(float y,PaintData paintData){
        HashMap<Integer,Row> rowHashMap = paintData.getPresentSheet().getRows();
        y -= paintData.getDefRowHeight();
        float rawY = 0;
        int rowNum = 0;
        float yOffset = paintData.getVerticalOffset();
        float presentRowHeight;
        do {
            rowNum++;
            if (rowHashMap.get(rowNum) == null){
                presentRowHeight = paintData.getDefRowHeight();
            }else{
                presentRowHeight = rowHashMap.get(rowNum).getRowHeight();
            }
            rawY += presentRowHeight;
        }while (rawY < yOffset+y);
        return rowNum;
    }

    /**
     * 将列号字符转化为第几列
     * @param colStr
     * @return
     */
    public static int colStrToNum(String colStr){
        if (colStr == null || colStr.length() == 0){
            return -1;
        }
        int res = 0;
        int a = 1;
        for (int i=colStr.length()-1;i>=0;i--){
            res = res + (colStr.charAt(0) - 'A' + 1) * a;
            a *= 26;
        }
        return res;
    }

    /**
     * 将列的数字转换为列的id
     * 如 20就转化为对应的‘T’
     *    27转化为AA
     * @param colId 列号
     * @return
     */
    public static String numToColStr(int colId){
        if (colId == 0) {
            return "";
        }
        int tmp = colId%26;
        if (tmp == 0){
            return numToColStr(colId/26-1) + 'Z';
        }
        return numToColStr(colId/26) + (char)('A' + colId%26 - 1);
    }

//    public boolean updateCell(PaintData paintData,int rowId,String colId,String value){
//        value = value.trim();
//
//        boolean isFormula = (value.charAt(0) == '=');
//        if ( rowId == 0 || colId == null ) {
//            return false;
//        }
//        HashMap<Integer,Row> rows = paintData.getPresentSheet().getRows();
//        Row row = rows.get(rowId);
//        Cell cell;
//        //如果此行数据为空，直接新建Cell元素即可完成数据的读写
//        if (row == null){
//            row = new Row();
//            row.setUnitMap(new HashMap<String, Cell>());
//            row.setRowId(rowId);
//            cell = new Cell();
//            if (isFormula){
//                cell.setFormula(value);
//                String formulaValue = String.valueOf(Calculator.calculate(value,paintData));
//                cell.setValue(formulaValue);
//            }else {
//                cell.setValue(value);
//            }
//            cell.setId( colId + rowId );
//            row.getUnitMap().put(cell.getId(),cell);
//            rows.put(rowId,row);
//        }else {
//            //行数据不为空，但是要判断是否含有此单元格数据
//            cell = row.getUnitMap().get( colId + rowId );
//            if (cell == null){
//                cell = new Cell();
//                cell.setId(colId+rowId);
//                if (isFormula){
//                    cell.setFormula(value);
//                    String formulaValue = String.valueOf(Calculator.calculate(value,paintData));
//                    cell.setValue(formulaValue);
//                }else {
//                    cell.setValue(value);
//                }
//                row.getUnitMap().put(colId+rowId,cell);
//            }else {
//                if (cell.getValue() == value || cell.getFormula() == value){
//                    //什么也不做
//                }else {
//                    cell.setValue(value);
//                }
//            }
//        }
//        return true;
//    }
    public boolean updateCell(PaintData paintData,int rowId,String colId,String value){
        if ( rowId == 0 || colId == null ) {
            return false;
        }
        boolean isFormula = false;
        if (value.length() != 0 && value != ""){
            isFormula = (value.charAt(0) == '＝'|| value.charAt(0) == '=');
            if (isFormula){
                value = value.substring(1);
                Log.d("DataHelper:031","201");
            }
        }
        Log.d("DataHelper:031","12301 "+value);

        value = value.trim();

        HashMap<Integer,Row> rows = paintData.getPresentSheet().getRows();
        Row row = rows.get(rowId);
        Cell cell;
        //如果此行数据为空，直接新建Cell元素即可完成数据的读写
        if (row == null){
            row = new Row();
            row.setUnitMap(new HashMap<String, Cell>());
            row.setRowId(rowId);
            cell = new Cell();
            rows.put(rowId,row);
        }else {
            cell = row.getUnitMap().get( colId + rowId );
            if (cell == null){
                cell = new Cell();
            }
        }
        cell.setId(colId + rowId);
        if (isFormula){
            cell.setFormula(value);
            String forValue = String.valueOf(Calculator.calculate(value,paintData));
            cell.setValue(forValue);
        }else {
            cell.setValue(value);
        }
        row.getUnitMap().put(cell.getId(),cell);
        return true;
    }


    /**
     * 根据行号和列号获得对应单元格的矩形左上角坐标
     * @param colStr 列号
     * @return 返回对应的单元格列ID左上角x坐标
     */
    public float getXByCol(String colStr,PaintData paintData){
        HashMap<String,ColAttri> colAttriHashMap = paintData.getPresentSheet().getColAttriMap();
        float xOffset = paintData.getHorizontalOffset();
        DataHelper helper = new DataHelper();
        int finalColNum = DataHelper.colStrToNum(colStr);
        int tmpCol = 1;
        float rawX = 0;
        float presentColWidth;
        while(tmpCol < finalColNum){
            if (colAttriHashMap.get(DataHelper.numToColStr(tmpCol)) == null){
                presentColWidth = paintData.getDefColWidth();
            }else{
                presentColWidth = colAttriHashMap.get(DataHelper.numToColStr(tmpCol)).getColWidth();

            }

            rawX += presentColWidth;
            tmpCol++;
        }

        return rawX - xOffset + paintData.getDefColWidth();
    }
    /**
     * 根据行号和列号获得对应单元格的矩形左上角坐标
     * @param rowId 行号
     * @return 返回对应的单元格行ID左上角y坐标
     */
    public float getYByRow(Integer rowId,PaintData paintData){
        float yOffset = paintData.getVerticalOffset();
        float rawY = 0;
        int tmpRowId = 1;
        float presentRowHeight = 0;
        HashMap<Integer,Row> rowHashMap = paintData.getPresentSheet().getRows();
        while(tmpRowId<rowId){
            if (rowHashMap.get(tmpRowId) == null){
                presentRowHeight = paintData.getDefRowHeight();
            }else{
                presentRowHeight = rowHashMap.get(tmpRowId).getRowHeight();
            }
            rawY += presentRowHeight;
            tmpRowId++;
        }
        return rawY - yOffset + paintData.getDefRowHeight();
    }

    /**
     * 根据Row的ID值获取该行的行高
     * @param rowId
     * @param paintData
     * @return
     */
    public float getRowHeightByRowId(int rowId,PaintData paintData){
        float result;
        HashMap<Integer,Row> rows = paintData.getPresentSheet().getRows();
        Row row = rows.get(rowId);
        if (row == null){
            result = paintData.getDefRowHeight();
        }else {
            result = row.getRowHeight();
        }
        return result;
    }

    public float getColWidthByColId(String colId,PaintData paintData){
        float result;
        HashMap<String,ColAttri> colAttriHashMap = paintData.getPresentSheet().getColAttriMap();
        ColAttri colAttri = colAttriHashMap.get(colId);
        if (colAttri == null){
            result = paintData.getDefColWidth();
        }else {
            result = colAttri.getColWidth();
        }
        return result;
    }


    /**
     *由范围字符转化为两个区间数组
     * @param rangeStr 范围字符串
     * @param row int型数组，有两个值，下标为0，1分别对应行开始，行结束
     * @param col String数组，下标为0、1分别对应列的开始和结束
     */
    public static void convertRangeStr(String rangeStr,int[] row,String[] col){
        int startRow,endRow;
        String startCol,endCol;

        /*下面是利用rangeStr解析出四个边界数据，startCol,endCol,startRow,endRow*/
        int first = -1;
        int tail;
        //寻找第一个不是数字的
        while(!Character.isDigit(rangeStr.charAt(++first)));
        tail =first;
        startCol = rangeStr.substring(0,first);
        //遇到冒号就停止
        while (rangeStr.charAt(++tail) != ':');
        startRow = Integer.valueOf(rangeStr.substring(first,tail));

        first = tail+1;
        while (++tail<rangeStr.length() && !Character.isDigit(rangeStr.charAt(tail)));
        endCol = rangeStr.substring(first,tail);

        endRow = Integer.valueOf(rangeStr.substring(tail, rangeStr.length()));
        row[0] = startRow;
        row[1] = endRow;
        col[0] = startCol;
        col[1] = endCol;
    }

    public static String getColStrByStr(String id){
        String result = "Q";
        for (int i =0;i<id.length();i++){
            if (Character.isDigit(id.charAt(i))){
                return id.substring(0,i);
            }
        }
        return result;
    }
    public static int getRowIdByStr(String id){
        String result = "0";
        for (int i = 0;i<id.length();i++){
            if (Character.isDigit(id.charAt(i))){
                String resStr = id.substring(i);
              return Integer.parseInt(resStr);
            }
        }
        return Integer.parseInt(result);
    }
}
