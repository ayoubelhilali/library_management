package com.library.patterns.adapter;

import com.library.model.Notification;

/**
 * Interface NotificationChannel - Interface commune pour envoyer des notifications
 * Cette interface définit le contrat que doit respecter tout service de notification
 */
public interface NotificationChannel {

    /**
     * Envoyer une notification
     * @param notification La notification à envoyer
     * @return true si l'envoi est réussi, false sinon
     */
    boolean sendNotification(Notification notification);

    /**
     * Vérifier si le canal est disponible
     * @return true si le canal peut envoyer des notifications
     */
    boolean isChannelAvailable();

    /**
     * Obtenir le type de canal
     * @return Le type de canal (ex: EMAIL, SMS, PUSH)
     */
    String getChannelType();
}
