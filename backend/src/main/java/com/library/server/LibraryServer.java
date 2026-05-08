package com.library.server;

import com.library.controller.BookController;
import com.library.controller.BorrowController;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class LibraryServer {

    public static void start() throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/books", new BookController());
        server.createContext("/api/borrows", new BorrowController());
        server.createContext("/api/borrow", new BorrowController());
        server.createContext("/api/return", new BorrowController());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:8080");
    }
}