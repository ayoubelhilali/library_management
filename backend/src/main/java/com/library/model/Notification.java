package com.library.model ;

import java.time.LocalDate;

public class Notification {

    private int notificationId ;
    private int memberId ;
    private String message ;
    private LocalDate sendDate ;
    private boolean read ;

    public Notification() {
    }

    public Notification(int notificationId, int memberId, String message, LocalDate sendDate, boolean read) {
        this.notificationId = notificationId ;
        this.memberId = memberId ;
        this.message = message ;
        this.sendDate = sendDate ;
        this.read = read ;
    }

    // Getters
    public int getNotificationId() {
        return notificationId ;
    }

    public int getMemberId() {
        return memberId ;
    }

    public String getMessage() {
        return message ;
    }

    public LocalDate getSendDate() {
        return sendDate ;
    }

    public boolean isRead() {
        return read ;
    }

    // Setters
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId ;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId ;
    }

    public void setMessage(String message) {
        this.message = message ;
    }

    public void setSendDate(LocalDate sendDate) {
        this.sendDate = sendDate ;
    }

    public void setRead(boolean read) {
        this.read = read ;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", memberId=" + memberId +
                ", message='" + message + '\'' +
                ", sendDate=" + sendDate +
                ", read=" + read +
                '}';
    }
}
