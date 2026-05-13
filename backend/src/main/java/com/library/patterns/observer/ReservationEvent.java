package com.library.patterns.observer;

/**
 * Classe ReservationEvent - Subject concret pour les événements de réservation
 * Notifie les observers quand un livre est réservé
 */
public class ReservationEvent extends Subject {

    private int reservationId;
    private int memberId;
    private String bookTitle;

    public ReservationEvent(int reservationId, int memberId, String bookTitle) {
        super();
        this.reservationId = reservationId;
        this.memberId = memberId;
        this.bookTitle = bookTitle;
    }

    public void createReservation() {
        this.notificationMessage = "You have successfully reserved the book: " + bookTitle;
        notifyObservers();
    }

    public void bookAvailable() {
        this.notificationMessage = "The book '" + bookTitle + "' you reserved is now available for pickup!";
        notifyObservers();
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
