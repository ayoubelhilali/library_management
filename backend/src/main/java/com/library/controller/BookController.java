package com.library.controller;

import com.google.gson.Gson;
import com.library.model.Book;
import com.library.service.BookService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BookController implements HttpHandler {

    private final BookService bookService = new BookService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }
            switch (method) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                case "PUT" -> handlePut(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }

        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();

        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Book book = bookService.getBookById(id);

            if (book == null) {
                sendResponse(exchange, 404, "{\"error\":\"Book not found\"}");
                return;
            }

            sendResponse(exchange, 200, gson.toJson(book));
            return;
        }

        if (query != null && query.startsWith("search=")) {
            String keyword = query.substring(7);
            List<Book> books = bookService.searchBooks(keyword);
            sendResponse(exchange, 200, gson.toJson(books));
            return;
        }

        List<Book> books = bookService.getAllBooks();
        sendResponse(exchange, 200, gson.toJson(books));
    }

    private void handlePost(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();

        // DUPLICATE BOOK
        if (path.equals("/api/books/duplicate")) {
            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.startsWith("id=")) {
                sendResponse(exchange, 400,
                        "{\"error\":\"Book id is required\"}");
                return;
            }
            int id = Integer.parseInt(query.substring(3));
            Book duplicatedBook = bookService.duplicateBook(id);
            sendResponse(exchange, 200,
                    gson.toJson(duplicatedBook));

            return;
        }
        // NORMAL ADD BOOK
        try {
            String body = readBody(exchange);
            if (body == null || body.isBlank()) {
                sendResponse(exchange, 400,
                        "{\"error\":\"Body is empty\"}");
                return;
            }
            Book book = gson.fromJson(body, Book.class);
            boolean created = bookService.addBook(book);
            if (created) {
                sendResponse(exchange, 201,
                        "{\"message\":\"Book added successfully\"}");
            } else {
                sendResponse(exchange, 400,
                        "{\"error\":\"Book not added\"}");
            }
        } catch (com.google.gson.JsonSyntaxException e) {

            sendResponse(exchange, 400,
                    "{\"error\":\"Invalid JSON\"}");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {

        try{
            String body = readBody(exchange);
            Book book = gson.fromJson(body, Book.class);

            boolean updated = bookService.updateBook(book);

            if (updated) {
                sendResponse(exchange, 200, "{\"message\":\"Book updated successfully\"}");
            } else {
                sendResponse(exchange, 400, "{\"error\":\"Book not updated\"}");
            }
        }catch (com.google.gson.JsonSyntaxException e ){
            sendResponse(exchange, 400, "{\"error\":\"Invalid JSON format\"}");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.startsWith("id=")) {
            sendResponse(exchange, 400, "{\"error\":\"Book id is required\"}");
            return;
        }

        int id = Integer.parseInt(query.substring(3));
        boolean deleted = bookService.deleteBook(id);

        if (deleted) {
            sendResponse(exchange, 200, "{\"message\":\"Book deleted successfully\"}");
        } else {
            sendResponse(exchange, 400, "{\"error\":\"Book not deleted\"}");
        }
    }

    private String readBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(statusCode, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}