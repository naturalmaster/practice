package com.example.coney.calc_formula.dataManage.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by coney on 2018/11/15.
 */

public class Book {
    private String FILE_URL;
    private HashMap<Integer,Sheet> sheets;

    public Book(String FILE_URL) {
        this.FILE_URL = FILE_URL;
        sheets = new HashMap<>();
    }

    public String getFILE_URL() {
        return FILE_URL;
    }

    public void setFILE_URL(String FILE_URL) {
        this.FILE_URL = FILE_URL;
    }

    public HashMap<Integer,Sheet> getSheets() {
        return sheets;
    }

}
