package com.library;

import com.library.patterns.singleton.DBConnection;

public class Main {
    public static void main(String[] args) {
        DBConnection db = DBConnection.getInstance();
        System.out.println(db.getConnection());
    }
}