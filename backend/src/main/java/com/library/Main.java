package com.library;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.patterns.singleton.DBConnection;

public class Main {
    public static void main(String[] args) {
        DBConnection db = DBConnection.getInstance();
        BookDAO dao = new BookDAO();
        dao.getAllBooks().forEach(book ->
                System.out.println(book.getTitle())
        );
        Book book=dao.getBookById(1);
        book.setTitle("clean code updated");
        dao.updateBook(book);
        System.out.println(book.getId()+"=> "+book.getTitle());
        dao.searchBooks("Software")
                .forEach(elem ->
                        System.out.println(elem.getTitle())
                );
    }
}