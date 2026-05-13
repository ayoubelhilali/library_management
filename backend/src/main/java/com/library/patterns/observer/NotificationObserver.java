package com.library.patterns.observer ;

import com.library.model.Member;
import com.library.model.Notification;
import java.time.LocalDate;

public class NotificationObserver implements Observer {

    private Member member ;
    private Subject subject ;

    public NotificationObserver(Member member, Subject subject) {
        this.member = member ;
        this.subject = subject ;
        // Auto-register when observer is created
        subject.attach(this) ;
    }

    @Override
    public void update() {
        notifyMember() ;
    }

    public void notifyMember() {
        Notification notification = new Notification() ;
        notification.setMemberId(member.getId()) ;
        notification.setMessage(subject.getNotificationMessage()) ;
        notification.setSendDate(LocalDate.now()) ;
        notification.setRead(false) ;
        
        sendNotification(notification) ;
    }

    private void sendNotification(Notification notification) {
        // Log notification or send via email
        System.out.println("Notification sent to member: " + member.getEmail()) ;
        System.out.println("Message: " + notification.getMessage()) ;
        System.out.println("Send Date: " + notification.getSendDate()) ;
    }

    public Member getMember() {
        return member ;
    }

    public void setMember(Member member) {
        this.member = member ;
    }
}
