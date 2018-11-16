package com.example.coney.calc_formula.mainView;

import org.junit.Test;

/**
 * Created by coney on 2018/11/15.
 */

public class PaintDataTest {
    @Test
    public void rangStrTest(){
        String rangeStr = "B123:E17";
        int startRow = 0,endRow;
        String startCol,endCol;

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

        System.out.println("startR:endR "+startRow+":"+endRow );
        System.out.println("startC:endC "+startCol+"+"+endCol );
    }
}
