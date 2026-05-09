package com.library.patterns.state;

import com.library.model.Book;

public interface BookState {
    void borrow(Book book);
    void returnBook(Book book);
    void reserve(Book book);
}