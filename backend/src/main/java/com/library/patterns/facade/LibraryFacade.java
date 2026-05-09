package com.library.patterns.facade;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.service.BookService;
import com.library.service.BorrowService;

import java.util.List;

public class LibraryFacade {

    private final BookService bookService;
    private final BorrowService borrowService;

    public LibraryFacade() {
        this.bookService = new BookService();
        this.borrowService = new BorrowService();
    }

    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    public Book getBookById(int id) {
        return bookService.getBookById(id);
    }

    public List<Borrow> getAllBorrows() {
        return borrowService.getAllBorrows();
    }

    public Borrow getBorrowById(int id) {
        return borrowService.getBorrowById(id);
    }

    public boolean borrowBook(int bookId, int memberId) {
        return borrowService.borrowBook(bookId, memberId);
    }

    public boolean returnBook(int borrowId) {
        return borrowService.returnBook(borrowId);
    }
}