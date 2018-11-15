package com.example.coney.calc_formula.dataManage;

import android.util.Log;

import com.example.coney.calc_formula.dataManage.data.Row;

import junit.framework.Assert;

import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;

/**
 * Created by coney on 2018/11/12.
 */

public class DataHelperTest {



    @Test
    public void TestDataHelper(){
        DataHelper helper = new DataHelper();
        File file = new File("c://tmp//sheet1.xml");
        helper.initTableInfoFromFile(file);
        TableInfo info = TableInfo.getInstance();
        for (HashMap.Entry<Integer,Row> entry:info.getRows().entrySet()){
            System.out.println("row "+entry.getKey());
            System.out.println(entry.getValue().toString());
        }
        System.out.println("end*********");

    }

    @Test
    public void TestLocToUnitId(){
        int x = 250;
        int y = 250;
        DataHelper helper = new DataHelper();
        String res = helper.xIndexToColId(x);
        Integer resy = helper.yIndexToRowId(y);
        System.out.println("offset xOffst"+Table.getHorizonalOffset()+" Offset yOffset "+Table.getVerticalOffset()+ "\n"+res +resy);
    }

}
