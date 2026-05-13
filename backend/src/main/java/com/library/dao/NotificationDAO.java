package com.library.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.patterns.singleton.DBConnection;

public class NotificationDAO {

    private final Connection connection;

    public NotificationDAO() {
        connection = DBConnection.getInstance().getConnection();
    }

    public boolean addNotification(int memberId, String message, java.time.LocalDate sendDate) {
        String query = """
            INSERT INTO notifications (member_id, message, send_date, is_read)
            VALUES (?, ?, ?, FALSE)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            stmt.setString(2, message);
            stmt.setDate(3, Date.valueOf(sendDate));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}