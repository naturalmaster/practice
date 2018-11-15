package com.example.coney.calc_formula.dataManage;

import java.io.File;
import java.io.InputStream;

/**
 * Created by coney on 2018/11/12.
 */

public interface IXMLParser {
    void initTableInfoFromFile(File file);
    void initTableInfoFromFile(InputStream fis);

}
