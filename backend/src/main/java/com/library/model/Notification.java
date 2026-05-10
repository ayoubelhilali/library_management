package com.library.model ;

import java.time.LocalDate;

public class Notification {

    private int notificationId ;
    private Member member ;
    private String message ;
    private LocalDate sendDate ;
    private boolean read ;

    public Notification() {
    }

    public Notification(int notificationId, Member member, String message, LocalDate sendDate, boolean read) {
        this.notificationId = notificationId ;
        this.member = member ;
        this.message = message ;
        this.sendDate = sendDate ;
        this.read = read ;
    }

    // Getters
    public int getNotificationId() {
        return notificationId ;
    }

    public Member getMember() {
        return member ;
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

    public void setMember(Member member) {
        this.member = member ;
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
                ", member=" + member +
                ", message='" + message + '\'' +
                ", sendDate=" + sendDate +
                ", read=" + read +
                '}';
    }
}
