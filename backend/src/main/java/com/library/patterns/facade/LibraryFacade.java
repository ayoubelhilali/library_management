package com.library.patterns.facade;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Reservation;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.ReservationService;

import java.util.List;

public class LibraryFacade {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final ReservationService reservationService;

    public LibraryFacade() {
        this.bookService = new BookService();
        this.borrowService = new BorrowService();
        this.reservationService = new ReservationService();
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

    public boolean reserveBook(Reservation reservation) {
        return reservationService.addReservation(reservation);
    }

}