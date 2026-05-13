package com.library;

import org.mindrot.jbcrypt.BCrypt;

public class GenerateAdminPassword {
    public static void main(String[] args) {
        String password = "admin123";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashed);
    }
}
