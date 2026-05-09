package com.library;

import com.library.server.LibraryServer;

public class Main {

    public static void main(String[] args) {

        try {
            LibraryServer.start();
            System.out.println("=================================");
            System.out.println("✅ Library Server Started");
            System.out.println("🌐 http://localhost:8080");
            System.out.println("=================================");

            System.out.println("📚 Books API:");
            System.out.println("GET  -> http://localhost:8080/api/books");

            System.out.println("\n📖 Borrows API:");
            System.out.println("GET  -> http://localhost:8080/api/borrows");
            System.out.println("POST -> http://localhost:8080/api/borrow");
            System.out.println("POST -> http://localhost:8080/api/return");

            System.out.println("\n Authentification API:");
            System.out.println("POST  -> http://localhost:8080/api/register");
            System.out.println("POST  -> http://localhost:8080/api/login");
            System.out.println("POST  -> http://localhost:8080/api/logout");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}