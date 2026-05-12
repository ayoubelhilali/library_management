package com.library.patterns.state;

import com.library.model.Book;
import com.library.model.enums.BookStatus;

public class ReservedState implements BookState {

    @Override
    public void borrow(Book book) {
        book.setStatus(BookStatus.BORROWED);
    }

    @Override
    public void returnBook(Book book) {
        book.setStatus(BookStatus.AVAILABLE);
    }

    @Override
    public void reserve(Book book) {
        throw new IllegalStateException("Book is already reserved");
    }
}