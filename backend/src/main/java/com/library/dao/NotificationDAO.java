package com.library.dao;

import com.library.model.Notification;
import com.library.patterns.singleton.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    private final Connection connection;

    public NotificationDAO() {
        connection = DBConnection
                .getInstance()
                .getConnection();
    }

   

    public boolean addNotification(Notification notification) {

        String sql = """
                INSERT INTO notifications
                (member_id, message, send_date, is_read)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, notification.getMemberId());

            ps.setString(2, notification.getMessage());

            ps.setDate(
                    3,
                    Date.valueOf(notification.getSendDate())
            );

            ps.setBoolean(4, notification.isRead());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    

    public List<Notification> getAllNotifications() {

        List<Notification> notifications =
                new ArrayList<>();

        String sql = """
                SELECT * FROM notifications
                """;

        try (PreparedStatement ps =
                     connection.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Notification notification = new Notification();

                notification.setNotificationId(rs.getInt("notification_id"));

                notification.setMemberId(rs.getInt("member_id"));

                notification.setMessage(rs.getString("message"));

                notification.setSendDate( rs.getDate("send_date").toLocalDate());

                notification.setRead(rs.getBoolean("is_read"));

                notifications.add(notification);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    
    public Notification getNotificationById(int id) {

        String sql = """
                SELECT * FROM notifications
                WHERE notification_id = ?
                """;

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Notification notification = new Notification();

                    notification.setNotificationId(rs.getInt("notification_id"));

                    notification.setMemberId(rs.getInt("member_id"));

                    notification.setMessage(
                            rs.getString("message")
                    );

                    notification.setSendDate(
                            rs.getDate("send_date")
                                    .toLocalDate()
                    );

                    notification.setRead(
                            rs.getBoolean("is_read")
                    );

                    return notification;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

   
    public List<Notification> getNotificationsByMemberId(
            int memberId
    ) {

        List<Notification> notifications =
                new ArrayList<>();

        String sql = """
                SELECT * FROM notifications
                WHERE member_id = ?
                """;

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, memberId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Notification notification =
                            new Notification();

                    notification.setNotificationId(
                            rs.getInt("notification_id")
                    );

                    notification.setMemberId(
                            rs.getInt("member_id")
                    );

                    notification.setMessage(
                            rs.getString("message")
                    );

                    notification.setSendDate(
                            rs.getDate("send_date")
                                    .toLocalDate()
                    );

                    notification.setRead(
                            rs.getBoolean("is_read")
                    );

                    notifications.add(notification);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    // UPDATE
    public boolean updateNotification(
            Notification notification
    ) {

        String sql = """
                UPDATE notifications
                SET member_id = ?,
                    message = ?,
                    send_date = ?,
                    is_read = ?
                WHERE notification_id = ?
                """;

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, notification.getMemberId());

            ps.setString(2, notification.getMessage());

            ps.setDate(
                    3,
                    Date.valueOf(notification.getSendDate())
            );

            ps.setBoolean(4, notification.isRead());

            ps.setInt(
                    5,
                    notification.getNotificationId()
            );

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // MARK AS READ
    public boolean markAsRead(int id) {

        String sql = """
                UPDATE notifications
                SET is_read = true
                WHERE notification_id = ?
                """;

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // DELETE
    public boolean deleteNotification(int id) {

        String sql = """
                DELETE FROM notifications
                WHERE notification_id = ?
                """;

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}