package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.ReservationDAO;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Reservation;
import com.library.model.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    public Reservation getReservationById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid reservation id");
        }

        return reservationDAO.getReservationById(id);
    }

    public boolean addReservation(Reservation reservation) {

        validateReservation(reservation);

        // Check if book exists
        Book book = bookDAO.getBookById(reservation.getBookId());

        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        // Check if member exists
        Member member = memberDAO.getMemberById(reservation.getMemberId());

        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        // Check if book is available
        if (!"AVAILABLE".equalsIgnoreCase(book.getStatus())) {
            throw new IllegalArgumentException("Book is not available for reservation");
        }

        // Set default status and date
        if (reservation.getStatus() == null) {
            reservation.setStatus(ReservationStatus.PENDING);
        }

        if (reservation.getReservationDate() == null) {
            reservation.setReservationDate(LocalDate.now());
        }

        return reservationDAO.addReservation(reservation);
    }

    public boolean updateReservation(Reservation reservation) {

        if (reservation.getReservationId() <= 0) {
            throw new IllegalArgumentException("Invalid reservation id");
        }

        validateReservation(reservation);

        Reservation existingReservation = reservationDAO.getReservationById(reservation.getReservationId());

        if (existingReservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        return reservationDAO.updateReservation(reservation);
    }

    public boolean deleteReservation(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid reservation id");
        }

        Reservation existingReservation = reservationDAO.getReservationById(id);

        if (existingReservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        return reservationDAO.deleteReservation(id);
    }

    public List<Reservation> getReservationsByMemberId(int memberId) {

        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member id");
        }

        return reservationDAO.getReservationsByMemberId(memberId);
    }

    public List<Reservation> getReservationsByBookId(int bookId) {

        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book id");
        }

        return reservationDAO.getReservationsByBookId(bookId);
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) {

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        return reservationDAO.getReservationsByStatus(status);
    }

    private void validateReservation(Reservation reservation) {

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }

        if (reservation.getBookId() <= 0) {
            throw new IllegalArgumentException("Book id is required and must be valid");
        }

        if (reservation.getMemberId() <= 0) {
            throw new IllegalArgumentException("Member id is required and must be valid");
        }

        if (reservation.getReservationDate() != null && reservation.getReservationDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Reservation date cannot be in the future");
        }
    }
}
