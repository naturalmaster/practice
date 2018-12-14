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
    /**
     * 从文件路径打开文件，并载入到内存，返回Book对象
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    Book loadFile(String filePath) throws FileNotFoundException;

    /**
     * 从文件载入一个xml数据，并返回book对象
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    Book loadFile(File file) throws FileNotFoundException ;

    /**
     * 直接从xml文件加载一个新的sheet进入book
     * @param file
     * @param book
     * @throws FileNotFoundException
     */
    boolean loadFile(File file,Book book,int sheetId) throws FileNotFoundException ;

    /**
     * 从输入流载入xml数据，并返回book对象
     * @param fis
     * @return
     */
    Book loadFile(InputStream fis);

    /**
     * 将一个sheet文件存位一个xml文件。
     * @param sheet sheet数据对象
     * @param sheetName 表格名字
     * @throws IOException
     */
    void saveSheetToFile(Sheet sheet, String sheetName) throws IOException;
    /**
     * 保存文件，保存文件内部通过dom4j通过对象生成xml文件并进行保存
     * @param book
     * @return
     */
    /**
     * 将Book保存为数个Sheet(xml文件)
     * @param book
     * @return
     */
    boolean saveFile(Book book);


    /**
     * 将book以fileName名字保存
     * @param book
     * @param fileName
     * @return
     */
    boolean saveFile(Book book,String fileName);

}
