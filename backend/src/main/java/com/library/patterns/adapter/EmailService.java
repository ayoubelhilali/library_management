package com.library.patterns.adapter;

/**
 * Classe EmailService - Service externe pour envoyer des emails
 * Avec une interface incompatible avec le système de notifications
 */
public class EmailService {

    // Méthode pour envoyer un email simple
    public boolean sendEmail(String recipient, String subject, String body) {
        if (recipient == null || recipient.isEmpty()) {
            System.err.println("Email recipient is required");
            return false;
        }

        if (subject == null || subject.isEmpty()) {
            System.err.println("Email subject is required");
            return false;
        }

        System.out.println("=== Email Service ===");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("Status: EMAIL SENT SUCCESSFULLY");
        System.out.println("=====================");

        return true;
    }

    // Méthode pour envoyer un email avec pièce jointe
    public boolean sendEmailWithAttachment(String recipient, String subject, String body, String attachment) {
        boolean emailSent = sendEmail(recipient, subject, body);

        if (emailSent) {
            System.out.println("Attachment: " + attachment + " attached");
            return true;
        }

        return false;
    }

    // Méthode pour envoyer un email en masse
    public boolean sendBulkEmail(String[] recipients, String subject, String body) {
        if (recipients == null || recipients.length == 0) {
            System.err.println("Recipients list is empty");
            return false;
        }
        System.out.println("Sending bulk email to " + recipients.length + " recipients...");

        for (String recipient : recipients) {
            sendEmail(recipient, subject, body);
        }

        return true;
    }

    // Méthode pour vérifier si un email est valide
    public boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}
