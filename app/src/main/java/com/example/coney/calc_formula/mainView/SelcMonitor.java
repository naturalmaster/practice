package com.example.coney.calc_formula.mainView;

import android.widget.EditText;

import com.example.coney.calc_formula.dataManage.DataHelper;
import com.example.coney.calc_formula.dataManage.data.Cell;

/**
 *
 * @author coney
 * @date 2018/12/3
 */

public class SelcMonitor implements OnSelcRecChangedListener {
    private PaintData paintData;

    public SelcMonitor(PaintData paintData) {
        this.paintData = paintData;
    }

    /**
     * 当选中框改变时，如果数据有修改，会对数据进行内核上的修改
     * @param oldRow
     * @param oldCol
     * @param newValue
     */
    @Override
    public void onSelcRecChanged(int oldRow, String oldCol, String newValue) {
        DataHelper helper = new DataHelper();
        Cell cell = helper.getCell(oldRow,oldCol,paintData);
        //满足条件，单元格需要更新
       if ( cell!=null && cell.getValue()!=newValue){
            helper.updateCell(paintData,oldRow,oldCol,newValue);
        } else {
           helper.updateCell(paintData,oldRow,oldCol,newValue);
       }
    }
}
