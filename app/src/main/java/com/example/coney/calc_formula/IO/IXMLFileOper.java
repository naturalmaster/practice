package com.example.coney.calc_formula.IO;

import com.example.coney.calc_formula.dataManage.data.Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by coney on 2018/11/12.
 */

public interface IXMLFileOper {
    void loadFile(String filePath) throws FileNotFoundException;

    void loadFile(File file) throws FileNotFoundException ;

    void loadFile(InputStream fis);

    /**
     * 保存文件，保存文件内部通过dom4j通过对象生成xml文件并进行保存
     * @param book
     * @return
     */
    boolean saveFile(Book book);

}
