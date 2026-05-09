package com.library.patterns.state;

import com.library.model.Book;

public class AvailableState implements BookState {

    @Override
    public void borrow(Book book) {
        book.setStatus("BORROWED");
    }

    @Override
    public void returnBook(Book book) {
        throw new IllegalStateException("Book is already available");
    }

    @Override
    public void reserve(Book book) {
        book.setStatus("RESERVED");
    }
}