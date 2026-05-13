package com.library.patterns.adapter;

import com.library.dao.UserDAO;
import com.library.model.Notification;

/**
 * Classe EmailServiceAdapter - Classe adapter qui adapte EmailService pour envoyer des notifications
 * Convertit les notifications en emails via EmailService
 */
public class EmailServiceAdapter {

    private EmailService emailService;
    private UserDAO userDAO;

    public EmailServiceAdapter() {
        this.emailService = new EmailService();
        this.userDAO = new UserDAO();
    }

    /**
     * Adapter la notification pour l'envoyer par email via EmailService
     */
    public boolean sendNotificationEmail(Notification notification) {

        if (notification == null) {
            System.err.println("Notification cannot be null");
            return false;
        }

        if (notification.getMemberId() == 0) {
            System.err.println("Member information is missing");
            return false;
        }

        String recipient = userDAO.findById(notification.getMemberId()).getEmail();
        String subject = "Notification from Library Management System";
        String body = buildEmailBody(notification);

        return emailService.sendEmail(recipient, subject, body);
    }

    /**
     * Construire le corps de l'email à partir de la notification
     */
    private String buildEmailBody(Notification notification) {
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(userDAO.findById(notification.getMemberId()).getUsername()).append(",\n\n");
        body.append("You have a new notification:\n\n");
        body.append(notification.getMessage()).append("\n\n");
        body.append("Sent on: ").append(notification.getSendDate()).append("\n\n");
        body.append("Best regards,\n");
        body.append("Library Management System");

        return body.toString();
    }

    /**
     * Obtenir l'instance du service d'email
     */
    public EmailService getEmailService() {
        return emailService;
    }
}
