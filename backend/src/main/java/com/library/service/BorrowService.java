package com.library.service;

import java.time.LocalDate;
import java.util.List;

import com.library.dao.*;
import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Member;
import com.library.model.Reservation;
import com.library.model.enums.BookStatus;
import com.library.patterns.state.BookState;
import com.library.patterns.state.BookStateFactory;
import com.library.patterns.strategy.*;

public class BorrowService {
    private final BorrowDAO borrowDAO;
    private final BookDAO bookDAO;
    private final ReservationDAO reservationDAO;
    private final MemberDAO memberDAO;

    public BorrowService() {
        this.memberDAO = new  MemberDAO();
        this.reservationDAO = new  ReservationDAO();
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

        Member member = memberDAO.getMemberById(memberId);

        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        int activeBorrows =
                borrowDAO.countActiveBorrowsByMemberId(memberId);

        String memberType = member.getMemberType().toString();
        int maxBorrows;
        int borrowDays;
        if (memberType.equals("STUDENT")) {
            maxBorrows = 3;
            borrowDays = 14;
        } else if (memberType.equals("TEACHER")) {
            maxBorrows = 5;
            borrowDays = 30;
        } else {
            throw new IllegalArgumentException("Invalid member type");
        }
        if (activeBorrows >= maxBorrows) {
            throw new IllegalArgumentException(
                    memberType + " borrow limit reached"
            );
        }
        Borrow activeBorrow =
                borrowDAO.getActiveBorrowByBookId(bookId);

        if (activeBorrow != null) {
            throw new IllegalArgumentException("Book is already borrowed");
        }
        BookState state =
                BookStateFactory.getState(book.getStatus());

        state.borrow(book);

        Borrow borrow = new Borrow();

        borrow.setBookID(bookId);
        borrow.setMemberID(memberId);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setExpectedReturnDate(
                LocalDate.now().plusDays(borrowDays)
        );
        borrow.setActualReturnDate(null);
        boolean created =
                borrowDAO.createBorrow(borrow);
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

        if (!returned) {
            return false;
        }
        Member member =
                memberDAO.getMemberById(
                        borrow.getMemberID()
                );

        PenaltyStrategy strategy;

        if (
                member.getMemberType().toString()
                        .equals("STUDENT")
        ) {

            strategy = new StudentPenaltyStrategy();

        } else if(
                member.getMemberType().toString()
                        .equals("TEACHER")
        ){

            strategy = new TeacherPenaltyStrategy();
        }else {
            strategy =new DefaultPenaltyStrategy();
        }

        PenaltyContext penaltyContext =
                new PenaltyContext(strategy);

        borrow.setActualReturnDate(LocalDate.now());

        double penalty =
                penaltyContext.calculatePenalty(borrow);

        System.out.println(
                "Penalty for member "
                        + member.getUsername()
                        + " = "
                        + penalty
        );

        System.out.println(
                penaltyContext.getStrategyDescription()
        );

        int bookId = borrow.getBookID();

        Book book = bookDAO.getBookById(bookId);

        Reservation firstReservation =
                reservationDAO.getFirstPendingReservationByBookId(bookId);

        if (firstReservation != null) {

            Borrow newBorrow = new Borrow();

            newBorrow.setBookID(bookId);
            newBorrow.setMemberID(firstReservation.getMemberId());
            newBorrow.setBorrowDate(LocalDate.now());
            newBorrow.setExpectedReturnDate(LocalDate.now().plusDays(14));
            newBorrow.setActualReturnDate(null);

            boolean newBorrowCreated = borrowDAO.createBorrow(newBorrow);

            if (!newBorrowCreated) {
                throw new IllegalArgumentException(
                        "Book returned, but failed to assign it to next reservation"
                );
            }

            int removedPosition = firstReservation.getQueuePosition();

            reservationDAO.deleteReservation(
                    firstReservation.getReservationId()
            );

            reservationDAO.decrementQueuePositions(
                    bookId,
                    removedPosition
            );

            book.setStatus(BookStatus.valueOf("BORROWED"));
            bookDAO.updateBook(book);

            return true;
        }

        BookState state = BookStateFactory.getState(book.getStatus());
        state.returnBook(book);
        bookDAO.updateBook(book);

        return true;
    }
}