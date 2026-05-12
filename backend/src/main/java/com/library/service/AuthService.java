package com.library.service;

import com.library.dao.UserDAO;
import com.library.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    // 🟢 REGISTER
    public boolean register(User user) {

        validateUser(user);

        User existingUser =
                userDAO.findByUsername(user.getUsername());

        if (existingUser != null) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        // Hash password
        String hashedPassword =
                BCrypt.hashpw(
                        user.getPassword(),
                        BCrypt.gensalt()
                );

        user.setPassword(hashedPassword);
        return userDAO.createUser(user);
    }

    // LOGIN
    public User login(String username, String password) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required"
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Password is required"
            );
        }

        User user =
                userDAO.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException(
                    "Invalid username or password"
            );
        }

        boolean passwordMatches =
                BCrypt.checkpw(
                        password,
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new IllegalArgumentException(
                    "Invalid username or password"
            );
        }

        return user;
    }

    // VALIDATION
    private void validateUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User cannot be null"
            );
        }

        if (user.getUsername() == null
                || user.getUsername().isBlank()) {

            throw new IllegalArgumentException(
                    "Username is required"
            );
        }

        if (user.getPassword() == null
                || user.getPassword().length() < 6) {

            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters"
            );
        }

        // if (user.getRole() == null
        //         || user.getRole().isBlank()) {

        //     throw new IllegalArgumentException(
        //             "Role is required"
        //     );
        // }


        if (user.getRole() == null) {

            throw new IllegalArgumentException(
                    "Role is required"
            );
        }


    }
}