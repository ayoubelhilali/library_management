package com.library.patterns.state;

import com.library.model.Book;

public class ReservedState implements BookState {

    @Override
    public void borrow(Book book) {
        book.setStatus("BORROWED");
    }

    @Override
    public void returnBook(Book book) {
        book.setStatus("AVAILABLE");
    }

    @Override
    public void reserve(Book book) {
        throw new IllegalStateException("Book is already reserved");
    }
}