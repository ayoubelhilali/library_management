package com.library.server;

import com.library.controller.AuthController;
import com.library.controller.BookController;
import com.library.controller.BorrowController;
import com.library.controller.MemberController;
import com.library.controller.ReservationController;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class LibraryServer {

    public static void start() throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/register", new AuthController());
        server.createContext("/api/login", new AuthController());
        server.createContext("/api/logout", new AuthController());

        server.createContext("/api/books", new BookController());

        server.createContext("/api/borrows", new BorrowController());
        server.createContext("/api/borrow", new BorrowController());
        server.createContext("/api/return", new BorrowController());

        server.createContext("/api/members", new MemberController());
        server.createContext("/api/reservations",new ReservationController()) ;

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:8080");
    }
}