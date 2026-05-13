package com.library.patterns.observer;

/**
 * Classe ReturnEvent - Subject concret pour les événements de retour de livre
 * Notifie les observers quand un livre est retourné
 */
public class ReturnEvent extends Subject {

    private int borrowId;
    private int memberId;
    private String bookTitle;

    public ReturnEvent(int borrowId, int memberId, String bookTitle) {
        super();
        this.borrowId = borrowId;
        this.memberId = memberId;
        this.bookTitle = bookTitle;
    }

    public void returnBook() {
        this.notificationMessage = "You have successfully returned the book: " + bookTitle;
        notifyObservers();
    }

    public int getBorrowId() {
        return borrowId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
