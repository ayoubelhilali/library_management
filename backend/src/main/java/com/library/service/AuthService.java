package com.library.service;

import com.library.dao.UserDAO;
import com.library.dao.MemberDAO;
import com.library.model.User;
import com.library.model.Member;
import com.library.model.enums.MemberType;
import com.library.patterns.factory.MemberFactory;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserDAO userDAO;
    private final MemberDAO memberDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.memberDAO = new MemberDAO();
    }

    public boolean registerMember(
            String username,
            String email,
            String phone,
            String password,
            String memberType
    ) {
        validateRegisterData(username, password, memberType);

        User existingUser = userDAO.findByUsername(username);

        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        MemberType type = MemberType.valueOf(memberType);

        Member member = MemberFactory.createMember(
                0,
                username,
                email,
                phone,
                hashedPassword,
                type
        );

        return memberDAO.addMember(member);
    }

    public User login(String username, String password) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userDAO.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        boolean passwordMatches = BCrypt.checkpw(password, user.getPassword());

        if (!passwordMatches) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return user;
    }

    private void validateRegisterData(
            String username,
            String password,
            String memberType
    ) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters"
            );
        }

        if (memberType == null || memberType.isBlank()) {
            throw new IllegalArgumentException("Member type is required");
        }

        if (!memberType.equals("STUDENT") && !memberType.equals("TEACHER")) {
            throw new IllegalArgumentException(
                    "Member type must be STUDENT or TEACHER"
            );
        }
    }
}