package com.library.controller;

import com.google.gson.Gson;
import com.library.model.User;
import com.library.service.AuthService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AuthController implements HttpHandler {

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if (path.equals("/api/register") && method.equals("POST")) {
                handleRegister(exchange);
                return;
            }

            if (path.equals("/api/login") && method.equals("POST")) {
                handleLogin(exchange);
                return;
            }

            if (path.equals("/api/logout") && method.equals("POST")) {
                handleLogout(exchange);
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

    private void handleRegister(HttpExchange exchange) throws IOException {
        try {
            String body = readBody(exchange);

            if (body == null || body.isBlank()) {
                sendResponse(exchange, 400, "{\"error\":\"Body is empty\"}");
                return;
            }

            RegisterRequest request = gson.fromJson(body, RegisterRequest.class);

            boolean created = authService.registerMember(
                    request.username,
                    request.email,
                    request.phone,
                    request.password,
                    request.memberType
            );

            if (created) {
                sendResponse(exchange, 201, "{\"message\":\"Member registered successfully\"}");
            } else {
                sendResponse(exchange, 400, "{\"error\":\"Member not registered\"}");
            }

        } catch (com.google.gson.JsonSyntaxException e) {
            sendResponse(exchange, 400, "{\"error\":\"Invalid JSON format\"}");
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        try {
            String body = readBody(exchange);

            if (body == null || body.isBlank()) {
                sendResponse(exchange, 400, "{\"error\":\"Body is empty\"}");
                return;
            }

            LoginRequest request = gson.fromJson(body, LoginRequest.class);

            User user = authService.login(request.username, request.password);

            // String response = String.format(
            //         "{\"message\":\"Login successful\",\"id\":%d,\"username\":\"%s\",\"role\":\"%s\",\"memberId\":%s}",
            //         user.getId(),
            //         user.getUsername(),
            //         user.getRole(),
            //         user.getMemberId() == null ? "null" : user.getMemberId().toString()
            // );
            String response = String.format(
                "{\"message\":\"Login successful\",\"id\":%d,\"username\":\"%s\",\"role\":\"%s\"}",
                user.getId(),
                user.getUsername(),
                user.getRole()
            );

            sendResponse(exchange, 200, response);

        } catch (com.google.gson.JsonSyntaxException e) {
            sendResponse(exchange, 400, "{\"error\":\"Invalid JSON format\"}");
        }
    }

    private void handleLogout(HttpExchange exchange) throws IOException {

        sendResponse(
                exchange,
                200,
                "{\"message\":\"Logout successful\"}"
        );
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

    private static class LoginRequest {
        String username;
        String password;
    }

    private static class RegisterRequest {
        String username;
        String email;
        String phone;
        String password;
        String memberType;
    }
}

