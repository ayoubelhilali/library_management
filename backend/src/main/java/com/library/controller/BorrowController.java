package com.library.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.patterns.command.BorrowBookCommand;
import com.library.patterns.command.Command;
import com.library.patterns.command.ReturnBookCommand;
import com.library.patterns.facade.LibraryFacade;
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

    private final LibraryFacade libraryFacade = new LibraryFacade();
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
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));

                    sendResponse(exchange, 200,
                            gson.toJson(libraryFacade.getBorrowById(id)));
                    return;
                }
                sendResponse(exchange, 200,
                        gson.toJson(libraryFacade.getAllBorrows()));
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
            Command command=new BorrowBookCommand(libraryFacade, request.bookId, request.memberId);
            boolean result = command.execute();
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

        if (body == null || body.isBlank()) {
            sendResponse(exchange, 400, "{\"error\":\"Request body is empty\"}");
            return;
        }
        try {
            ReturnRequest request = gson.fromJson(body, ReturnRequest.class);

            Command command = new ReturnBookCommand(
                    libraryFacade,
                    request.borrowId
            );
            boolean result = command.execute();
            if (result) {
                sendResponse(exchange, 200, "{\"message\":\"Book returned successfully\"}");
            } else {
                sendResponse(exchange, 400, "{\"error\":\"Return failed\"}");
            }

        } catch (com.google.gson.JsonSyntaxException e) {
            sendResponse(exchange, 400, "{\"error\":\"Invalid JSON format\"}");
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