package com.example.coney.calc_formula.dataManage;

import android.util.Log;

import com.example.coney.calc_formula.dataManage.data.Row;
import com.example.coney.calc_formula.dataManage.data.Sheet;

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
    DataHelper helper = new DataHelper();

    @Test
    public void colStrToNumTest(){
        helper = new DataHelper();
        int res = helper.colStrToNum("AA");
        System.out.println("result: "+res);

    }


    @Test
    public void TestDataHelper(){


    }



}
