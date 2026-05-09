package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.model.Book;
import com.library.model.Borrow;
import com.library.patterns.command.Command;
import com.library.patterns.state.BookState;
import com.library.patterns.state.BookStateFactory;

import java.time.LocalDate;
import java.util.List;

public class BorrowService {
    private final BorrowDAO borrowDAO;
    private final BookDAO bookDAO;

    public BorrowService() {
        this.borrowDAO = new BorrowDAO();
        this.bookDAO = new BookDAO();
    }

    public List<Borrow> getAllBorrows() {
        return borrowDAO.getAllBorrows();
    }

    public Borrow getBorrowById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid borrow id");
        }

        Borrow borrow = borrowDAO.getBorrowById(id);

        if (borrow == null) {
            throw new IllegalArgumentException("Borrow not found");
        }

        return borrow;
    }

    public boolean borrowBook(int bookId, int memberId) {

        if (bookId <= 0 || memberId <= 0) {
            throw new IllegalArgumentException("Invalid book or member id");
        }
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        Borrow activeBorrow = borrowDAO.getActiveBorrowByBookId(bookId);

        if (activeBorrow != null) {
            throw new IllegalArgumentException("Book is already borrowed");
        }

        BookState state = BookStateFactory.getState(book.getStatus());
        state.borrow(book);

        Borrow borrow = new Borrow();
        borrow.setBookID(bookId);
        borrow.setMemberID(memberId);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setExpectedReturnDate(LocalDate.now().plusDays(14));
        borrow.setActualReturnDate(null);

        boolean created = borrowDAO.createBorrow(borrow);

        if (created) {
            bookDAO.updateBook(book);
        }

        return created;
    }

    public boolean returnBook(int borrowId) {

        if (borrowId <= 0) {
            throw new IllegalArgumentException("Invalid borrow id");
        }

        Borrow borrow = borrowDAO.getBorrowById(borrowId);

        if (borrow == null) {
            throw new IllegalArgumentException("Borrow not found");
        }

        if (borrow.getActualReturnDate() != null) {
            throw new IllegalArgumentException("Book already returned");
        }
        boolean returned = borrowDAO.returnBook(borrowId, LocalDate.now());
        if (returned) {
            Book book = bookDAO.getBookById(borrow.getBookID());
            BookState state = BookStateFactory.getState(book.getStatus());
            state.returnBook(book);
            bookDAO.updateBook(book);
        }
        return returned;
    }
}