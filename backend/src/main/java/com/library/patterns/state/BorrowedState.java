package com.library.patterns.state;

import com.library.model.Book;
import com.library.model.enums.BookStatus;

public class BorrowedState implements BookState {

    @Override
    public void borrow(Book book) {
        throw new IllegalStateException("Book is already borrowed");
    }

    @Override
    public void returnBook(Book book) {
        book.setStatus(BookStatus.AVAILABLE);
    }

    @Override
    public void reserve(Book book) {
        book.setStatus(BookStatus.RESERVED);
    }
}