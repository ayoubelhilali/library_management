package com.library.patterns.observer;

/**
 * Classe BorrowEvent - Subject concret pour les événements d'emprunt
 * Notifie les observers quand un livre est emprunté
 */
public class BorrowEvent extends Subject {

    private int bookId;
    private int memberId;
    private String bookTitle;

    public BorrowEvent(int bookId, int memberId, String bookTitle) {
        super();
        this.bookId = bookId;
        this.memberId = memberId;
        this.bookTitle = bookTitle;
    }

    public void borrowBook() {
        this.notificationMessage = "You have successfully borrowed the book: " + bookTitle;
        notifyObservers();
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
