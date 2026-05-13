package com.library.model ;

import java.time.LocalDate;

import com.library.model.enums.ReservationStatus;

public class Reservation {

    private int reservationId ;
    private int bookId ;
    private int memberId ;
    private LocalDate reservationDate ;
    private ReservationStatus status ;
    private int queuePosition;

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    public Reservation() {
    }

    public Reservation(int reservationId, int bookId, int memberId,
                       LocalDate reservationDate, ReservationStatus status) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.reservationDate = reservationDate;
        this.status = status;
    }



    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }



    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }



    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }



    
}