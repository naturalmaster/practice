package com.example.coney.calc_formula.dataManage;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.coney.calc_formula.MyApplication;
import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.ColAttri;
import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.utils.CalcUtils;
import com.example.coney.calc_formula.formula.Calculator;
import com.example.coney.calc_formula.view.PaintData;

import java.util.HashMap;

/**
 *
 * @author coney
 * @date 2018/11/12
 */

public class DataHelper {
    public static final String TAG = "dataHelper";

    private PaintData paintData;

    public DataHelper(PaintData paintData) {
        this.paintData = paintData;
    }

    /**
     * 根据屏幕坐标获得对应的单元格的数据对象
     * @param x
     * @param y
     * @return
     */
    public Cell getCellByXY(float x, float y){
        Integer rowId = yIndexToRowId(y);
        String colStr = xIndexToColId(x);
        return getCell(rowId,colStr,paintData.getPresentSheet());
    }

    public Cell getCell(String id){
        id = id.toUpperCase();
        int rowId = getRowIdByStr(id);
        String colId = getColStrByStr(id);
        return getCell(rowId,colId);
    }
    public Cell getCell(int rowId, String colStr){
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
     *由范围字符转化为两个区间数组
     * @param rangeStr 范围字符串
     * @param row int型数组，有两个值，下标为0，1分别对应行开始，行结束
     * @param col String数组，下标为0、1分别对应列的开始和结束
     */
    public static void convertRangeStr(String rangeStr,int[] row,String[] col){
        rangeStr = rangeStr.toUpperCase();
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
    /**
     * 将列号字符转化为第几列
     * @param colStr
     * @return
     */
    public static int colStrToNum(String colStr){

        if (colStr == null || colStr.length() == 0){
            return -1;
        }
        colStr = colStr.trim().toUpperCase();
        int res = 0;
        int a = 1;
        for (int i=colStr.length()-1;i>=0;i--){
            res = res + (colStr.charAt(i) - 'A' + 1) * a;
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
    /**
     * 根据x值屏幕坐标返回对应的列号
     * @param x x坐标
     * @return  列号
     */
    public String xIndexToColId(float x){
        x -= this.paintData.getDefColWidth();
        HashMap<String,ColAttri> colAttriHashMap = paintData.getPresentSheet().getColAttriMap();
        int colNum = 0;
        float xOffset = this.paintData.getHorizontalOffset();
        float rawX = 0;
        float presentColWidth;
        do {
            colNum++;
            if (colAttriHashMap.get(DataHelper.numToColStr(colNum)) == null){
                presentColWidth = this.paintData.getDefColWidth();
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
    public Integer yIndexToRowId(float y){
        HashMap<Integer,Row> rowHashMap = paintData.getPresentSheet().getRows();
        y -= this.paintData.getDefRowHeight();
        float rawY = 0;
        int rowNum = 0;
        float yOffset = this.paintData.getVerticalOffset();
        float presentRowHeight;
        do {
            rowNum++;
            if (rowHashMap.get(rowNum) == null){
                presentRowHeight = this.paintData.getDefRowHeight();
            }else{
                presentRowHeight = rowHashMap.get(rowNum).getRowHeight();
            }
            rawY += presentRowHeight;
        }while (rawY < yOffset+y);
        return rowNum;
    }



    public boolean updateCell(int rowId, String colId, String value){
        if ( rowId == 0 || colId == null ) {
            return false;
        }
        boolean isNullValue = false;
        boolean isFormula = false;
        if (value == null || value .equals("")  || value.length() == 0){
            isNullValue = true;
        }
        if (!isNullValue){
            isFormula = (value.charAt(0) == '＝'|| value.charAt(0) == '=');
            if (isFormula){
                value = CalcUtils.fullCharToHalf(value);
                value = value.substring(1);
            }
        }
        value = value.trim();
        HashMap<Integer,Row> rows = paintData.getPresentSheet().getRows();
        Row row = rows.get(rowId);
        Cell cell;
        //如果此行数据为空，直接新建Cell元素即可完成数据的读写
        if (row == null){
            if (isNullValue){
                return false;
            }
            row = new Row();
            row.setUnitMap(new HashMap<String, Cell>());
            row.setRowId(rowId);
            cell = new Cell();
            rows.put(rowId,row);
        }else {
            cell = row.getUnitMap().get( colId + rowId );
            if (cell == null){
                if (isNullValue){
                    return false;
                }else {
                    cell = new Cell();
                }
            }else {
                //新的值和内核的值相同，没有进行修改，没必要更新
                if (cell.getValue().trim().equals(value)){
                    return false;
                }else if (isNullValue && cell.getValue().trim() == ""){
                    return false;
                }
            }
        }
        cell.setId(colId + rowId);
        if (isFormula){
            cell.setFormula(value);
            cell.setHasFormula(true);
            String forValue = String.valueOf(Calculator.calculate(value,paintData));
            cell.setValue(forValue);
        }else {
            cell.setHasFormula(false);
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
    public float getXByCol(String colStr){
        HashMap<String,ColAttri> colAttriHashMap = paintData.getPresentSheet().getColAttriMap();
        float xOffset = paintData.getHorizontalOffset();
        DataHelper helper = new DataHelper(paintData);
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
    public float getYByRow(Integer rowId){
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
     * @return
     */
    public float getRowHeightByRowId(int rowId){
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

    /**
     * 根据列的ID值获取对应列宽
     * @param colId 列ID
     * @return 返回列宽
     */
    public float getColWidthByColId(String colId){
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


}
