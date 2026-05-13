package com.library.patterns.observer ;

import com.library.model.Member;
import com.library.model.Notification;
import com.library.patterns.adapter.EmailServiceAdapter;
import com.library.dao.NotificationDAO;
import java.time.LocalDate;

public class NotificationObserver implements Observer {

    private Member member ;
    private Subject subject ;
    private EmailServiceAdapter emailAdapter;
    private NotificationDAO notificationDAO;

    public NotificationObserver(Member member, Subject subject) {
        this.member = member ;
        this.subject = subject ;
        this.emailAdapter = new EmailServiceAdapter();
        this.notificationDAO = new NotificationDAO();
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
        try {
            // Save notification to database
            notificationDAO.addNotification(notification);
            
            // Send email notification via adapter
            emailAdapter.sendNotificationEmail(notification);
            
            System.out.println("✓ Notification sent to member: " + member.getEmail());
            System.out.println("  Message: " + notification.getMessage());
            System.out.println("  Send Date: " + notification.getSendDate());
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }

    public Member getMember() {
        return member ;
    }

    public void setMember(Member member) {
        this.member = member ;
    }
}
