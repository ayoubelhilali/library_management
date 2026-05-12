package com.library.dao;

import com.library.model.*;
import com.library.model.enums.MemberType;
import com.library.model.enums.userRole;
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
    public boolean createUser(User user) {

        String query = """
                INSERT INTO users(username,email,phone,password,role)
                VALUES (?,?,?,?,?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole().toString());

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

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    int id = rs.getInt("id");
                    String dbUsername = rs.getString("username");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String password = rs.getString("password");
                    userRole role = userRole.valueOf(rs.getString("role"));

                    User user;

                    if (role == userRole.ADMIN) {
                        user = new Admin(id, dbUsername, email, phone, password);
                    } else if (role == userRole.MEMBER) {
                        MemberType memberType = getMemberType(id);

                        if (memberType == MemberType.STUDENT) {

                            user = new StudentMember(
                                    id,
                                    dbUsername,
                                    email,
                                    phone,
                                    password
                            );

                        } else if (memberType == MemberType.TEACHER) {

                            user = new TeacherMember(
                                    id,
                                    dbUsername,
                                    email,
                                    phone,
                                    password
                            );

                        } else {

                            return null;
                        }

                    } else {
                        return null;
                    }

                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int createUserAndReturnId(User user) {

        String query = """
            INSERT INTO users(username,email,phone,password,role)
            VALUES (?,?,?,?,?)
            """;

        try (
                PreparedStatement ps = connection.prepareStatement(
                        query,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole().toString());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // FIND USER BY ID
    public User findById(int id) {

        String query = """
                SELECT * FROM users
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String password = rs.getString("password");
                    userRole role = userRole.valueOf(rs.getString("role"));

                    User user;

                    if (role == userRole.ADMIN) {
                        user = new Admin(userId, username, email, phone, password);
                    } else if (role == userRole.MEMBER) {
                        MemberType memberType = getMemberType(id);

                        if (memberType == MemberType.STUDENT) {

                            user = new StudentMember(
                                    id,
                                    username,
                                    email,
                                    phone,
                                    password
                            );

                        } else if (memberType == MemberType.TEACHER) {

                            user = new TeacherMember(
                                    id,
                                    username,
                                    email,
                                    phone,
                                    password
                            );

                        } else {

                            return null;
                        }

                    } else {
                        return null;
                    }

                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE USER
    public boolean updateUser(User user) {

        String query = """
                UPDATE users 
                SET username = ?, email = ?, phone = ?, password = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getId());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // DELETE USER
    public boolean deleteUser(int id) {

        String query = """
                DELETE FROM users
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }




    private MemberType getMemberType(int userId) {

        String query = """
                SELECT member_type
                FROM members
                WHERE user_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return MemberType.valueOf(
                            rs.getString("member_type")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}

