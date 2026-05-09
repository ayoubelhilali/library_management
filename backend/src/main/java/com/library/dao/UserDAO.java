package com.library.dao;

import com.library.model.User;
import com.library.patterns.singleton.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection connection;
    public UserDAO() {
        connection= DBConnection.getInstance().getConnection();
    }
    public boolean createUser(User user){
        String query= """
                INSERT INTO users(username,password,role,member_id)
                VALUES (?,?,?,?)
                """;
        try (PreparedStatement ps =
                     connection.prepareStatement(query)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            if (user.getMemberId() != null) {
                ps.setInt(4, user.getMemberId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // FIND USER BY USERNAME
    public User findByUsername(String username) {

        String query = """
                SELECT * FROM users
                WHERE username = ?
                """;
        try (PreparedStatement ps =
                     connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    int memberId = rs.getInt("member_id");

                    if (!rs.wasNull()) {
                        user.setMemberId(memberId);
                    }

                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
