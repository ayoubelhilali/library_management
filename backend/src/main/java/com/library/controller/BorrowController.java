package com.library.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.service.BorrowService;
import com.library.utils.LocalDateAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class BorrowController implements HttpHandler {

    private final BorrowService borrowService = new BorrowService();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if (path.equals("/api/borrows") && method.equals("GET")) {
                sendResponse(exchange, 200, gson.toJson(borrowService.getAllBorrows()));
                return;
            }

            if (path.equals("/api/borrow") && method.equals("POST")) {
                handleBorrow(exchange);
                return;
            }

            if (path.equals("/api/return") && method.equals("POST")) {
                handleReturn(exchange);
                return;
            }
            sendResponse(exchange, 404, "{\"error\":\"Endpoint not found\"}");

        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
        }
    }

    private void handleBorrow(HttpExchange exchange) throws IOException {

        String body = readBody(exchange);

        if (body == null || body.isBlank()) {
            sendResponse(exchange, 400,
                    "{\"error\":\"Request body is empty\"}");
            return;
        }
        try {
            BorrowRequest request =
                    gson.fromJson(body, BorrowRequest.class);
            boolean result = borrowService.borrowBook(
                    request.bookId,
                    request.memberId
            );
            if (result) {
                sendResponse(exchange, 201,
                        "{\"message\":\"Book borrowed successfully\"}");
            } else {
                sendResponse(exchange, 400,
                        "{\"error\":\"Borrow failed\"}");
            }
        } catch (com.google.gson.JsonSyntaxException e) {
            sendResponse(exchange, 400,
                    "{\"error\":\"Invalid JSON format\"}");
        }
    }

    private void handleReturn(HttpExchange exchange) throws IOException {

        String body = readBody(exchange);

        ReturnRequest request = gson.fromJson(body, ReturnRequest.class);

        boolean result = borrowService.returnBook(request.borrowId);

        if (result) {
            sendResponse(exchange, 200, "{\"message\":\"Book returned successfully\"}");
        } else {
            sendResponse(exchange, 400, "{\"error\":\"Return failed\"}");
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

    private static class BorrowRequest {
        int bookId;
        int memberId;
    }

    private static class ReturnRequest {
        int borrowId;
    }
}