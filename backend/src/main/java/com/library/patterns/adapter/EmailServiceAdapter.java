package com.library.patterns.adapter;

import com.library.model.Notification;

/**
 * Classe EmailServiceAdapter - Classe adapter qui adapte EmailService à l'interface NotificationChannel
 * Cet adapter convertit l'interface de EmailService pour qu'elle soit compatible avec NotificationChannel
 */
public class EmailServiceAdapter implements NotificationChannel {

    private EmailService emailService;
    private boolean isAvailable;

    public EmailServiceAdapter() {
        this.emailService = new EmailService();
        this.isAvailable = true;
    }

    /**
     * Adapter la méthode sendNotification au format attendu par EmailService
     */
    @Override
    public boolean sendNotification(Notification notification) {

        if (notification == null) {
            System.err.println("Notification cannot be null");
            return false;
        }

        if (notification.getMember() == null) {
            System.err.println("Member information is missing");
            return false;
        }

        String recipient = notification.getMember().getEmail();
        String subject = "Notification from Library Management System";
        String body = buildEmailBody(notification);

        // Adapter la méthode emailService.sendEmail() pour utiliser l'interface NotificationChannel
        return emailService.sendEmail(recipient, subject, body);
    }

    /**
     * Construire le corps de l'email à partir de la notification
     */
    private String buildEmailBody(Notification notification) {
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(notification.getMember().getUsername()).append(",\n\n");
        body.append("You have a new notification:\n\n");
        body.append(notification.getMessage()).append("\n\n");
        body.append("Sent on: ").append(notification.getSendDate()).append("\n\n");
        body.append("Best regards,\n");
        body.append("Library Management System");

        return body.toString();
    }

    /**
     * Vérifier si le service d'email est disponible
     */
    @Override
    public boolean isChannelAvailable() {
        return isAvailable;
    }

    /**
     * Obtenir le type de canal
     */
    @Override
    public String getChannelType() {
        return "EMAIL";
    }

    /**
     * Définir la disponibilité du canal
     */
    public void setChannelAvailable(boolean available) {
        this.isAvailable = available;
    }

    /**
     * Envoyer une notification en masse via l'adapter
     */
    public boolean sendBulkNotifications(Notification[] notifications) {

        if (notifications == null || notifications.length == 0) {
            System.err.println("No notifications to send");
            return false;
        }

        boolean allSent = true;

        for (Notification notification : notifications) {
            if (!sendNotification(notification)) {
                allSent = false;
            }
        }

        return allSent;
    }

    /**
     * Obtenir l'instance du service d'email adapté
     */
    public EmailService getEmailService() {
        return emailService;
    }
}
