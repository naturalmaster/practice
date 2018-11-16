package com.example.coney.calc_formula.mainView;

import com.example.coney.calc_formula.dataManage.Table;
import com.example.coney.calc_formula.dataManage.data.Sheet;

import org.junit.Test;

/**
 * Created by coney on 2018/11/13.
 */

public class PaintUtilsTest {

    @Test
    public void selectTest(){
        float result = PaintUtils.selectedXYFromXY(560,'x');
        float rowWidth = Table.getColSpace();
        float xOffset = Sheet.getHorizonalOffset();
        System.out.println("result:"+result);
        System.out.println("xOffset:"+xOffset);
        System.out.println("leftSide:"+(rowWidth - xOffset%rowWidth));

    }
}
