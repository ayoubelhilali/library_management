package com.library;

import com.library.dao.BookDAO;
import com.library.patterns.singleton.DBConnection;

public class Main {
    public static void main(String[] args) {
        DBConnection db = DBConnection.getInstance();
        System.out.println(db.getConnection());
        BookDAO dao = new BookDAO();

        dao.getAllBooks().forEach(book ->
                System.out.println(book.getTitle())
        );
    }
}