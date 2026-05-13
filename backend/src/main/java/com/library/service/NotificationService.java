package com.library.service;

import com.library.dao.NotificationDAO;
import com.library.model.Notification;

import java.util.List;

public class NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationService() {

        this.notificationDAO =
                new NotificationDAO();
    }


    public boolean addNotification(
            Notification notification
    ) {

        validateNotification(notification);

        return notificationDAO
                .addNotification(notification);
    }


    public List<Notification> getAllNotifications() {

        return notificationDAO
                .getAllNotifications();
    }

    public Notification getNotificationById(int id) {

        if (id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid notification id"
            );
        }

        return notificationDAO
                .getNotificationById(id);
    }

    public List<Notification> getNotificationsByMemberId(
            int memberId
    ) {

        if (memberId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid member id"
            );
        }

        return notificationDAO
                .getNotificationsByMemberId(memberId);
    }


    public boolean updateNotification(
            Notification notification
    ) {

        if (notification.getNotificationId() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid notification id"
            );
        }

        validateNotification(notification);

        return notificationDAO
                .updateNotification(notification);
    }

    public boolean markAsRead(int id) {

        if (id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid notification id"
            );
        }

        return notificationDAO
                .markAsRead(id);
    }


    public boolean deleteNotification(int id) {

        if (id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid notification id"
            );
        }

        return notificationDAO
                .deleteNotification(id);
    }


    private void validateNotification(
            Notification notification
    ) {

        if (notification == null) {

            throw new IllegalArgumentException(
                    "Notification is null"
            );
        }

        if (notification.getMemberId() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid member id"
            );
        }

        if (notification.getMessage() == null ||
                notification.getMessage().isBlank()) {

            throw new IllegalArgumentException(
                    "Message is required"
            );
        }

        if (notification.getSendDate() == null) {

            throw new IllegalArgumentException(
                    "Send date is required"
            );
        }
    }


        public boolean markAllAsRead(int memberId) {

                return notificationDAO.markAllAsRead(memberId);
        }
}