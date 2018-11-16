package com.example.coney.calc_formula.dataManage;

import com.example.coney.calc_formula.dataManage.data.Cell;
import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Sheet;
import com.example.coney.calc_formula.mainView.PaintData;

import java.util.HashMap;

/**
 * Created by coney on 2018/11/12.
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

    /**
     * 根据行号，列值返回Unit数据对象
     * @param rowId 行号
     * @param colStr 列值
     * @return Unit单元格对象
     */
    private Cell getCell(Integer rowId, String colStr,Sheet sheet){
        HashMap<Integer,Row> rows = sheet.getRows();
        if (!rows.containsKey(rowId))
            return null;
        HashMap<String,Cell> cellMap = rows.get(rowId).getUnitMap();
        if (cellMap == null)
            return null;
        return cellMap.get(colStr+rowId);
    }

    /**
     * 根据x值屏幕坐标返回对应的列号
     * @param x x坐标
     * @return  列号
     */
    public String xIndexToColId(float x, PaintData paintData){
        float rowWidth = paintData.getPresentSheet().getDefColWidth();
        float xOffset = paintData.getHorizonalOffset();
//        int tmpNum = (x+xOffset%rowWidth-2*rowWidth)/rowWidth+xOffset/rowWidth;
        float tmpNum = ((x-rowWidth+xOffset)/rowWidth);
        return ""+((char)('A'+tmpNum));

    }

    /**
     * 根据y值屏幕坐标返回对应的行号
     * @param y y坐标
     * @return  以Integer对象形式返回行号
     */
    public Integer yIndexToRowId(float y,PaintData paintData){
        float rowHeight = paintData.getPresentSheet().getDefRowHeight();
        float yOffset = paintData.getVerticalOffset();
        float tmpNum = (((y-rowHeight+yOffset)/rowHeight)+1);
        return new Integer((int) tmpNum);
    }

    /**
     * 将列号字符转化为第几列
     * @param colStr
     * @return
     */
    public int colStrToNum(String colStr){
        if (colStr==null || colStr.length()==0){
            return -1;
        }
        int res = 0;
        int a =1;
        for (int i=colStr.length()-1;i>=0;i--){
            res=res+(colStr.charAt(0)-'A'+1)*a;
            a*=26;
        }
        return res;
    }

    public String numToColStr(int colId){
        if (colId==0) return "";
        int tmp = colId%26;
        if (tmp == 0)
        return numToColStr(colId/26-1)+'Z';
        return numToColStr(colId/26)+(char)('A'+colId%26-1);
    }




}
