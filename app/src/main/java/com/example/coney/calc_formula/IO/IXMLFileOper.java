package com.example.coney.calc_formula.IO;

import com.example.coney.calc_formula.dataManage.data.Book;
import com.example.coney.calc_formula.dataManage.data.Sheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author coney
 * @date 2018/11/12
 */

public interface IXMLFileOper {
    Book loadFile(String filePath) throws FileNotFoundException;

    Book loadFile(File file) throws FileNotFoundException ;

    Book loadFile(InputStream fis);
    void saveSheetToFile(Sheet sheet, String sheetName) throws IOException;
    /**
     * 保存文件，保存文件内部通过dom4j通过对象生成xml文件并进行保存
     * @param book
     * @return
     */
    boolean saveFile(Book book);

    boolean saveFile(Book book,String fileName);

}
