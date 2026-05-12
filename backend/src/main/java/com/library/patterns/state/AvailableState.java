package com.library.patterns.state;

import com.library.model.Book;
import com.library.model.enums.BookStatus;

public class AvailableState implements BookState {

    @Override
    public void borrow(Book book) {
        book.setStatus(BookStatus.BORROWED);
    }

    @Override
    public void returnBook(Book book) {
        throw new IllegalStateException("Book is already available");
    }

    @Override
    public void reserve(Book book) {
        book.setStatus(BookStatus.RESERVED);
    }
}