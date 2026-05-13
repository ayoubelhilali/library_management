package com.library.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.model.Notification;
import com.library.service.NotificationService;
import com.library.patterns.adapter.EmailServiceAdapter;
import com.library.utils.LocalDateAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class NotificationController implements HttpHandler {

    private final NotificationService notificationService = new NotificationService();
    private final EmailServiceAdapter emailServiceAdapter = new EmailServiceAdapter();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            // =========================
            // ✅ CORS PRE-FLIGHT FIX
            // =========================
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

                addCorsHeaders(exchange);

                exchange.sendResponseHeaders(204, -1);
                return;
            }

            switch (exchange.getRequestMethod()) {

                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                case "PUT" -> handlePut(exchange);
                case "DELETE" -> handleDelete(exchange);

                default -> sendResponse(exchange, 405,
                        "{\"error\":\"Method not allowed\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500,
                    "{\"error\":\"Internal server error\"}");
        }
    }

    // ========================= GET =========================
    private void handleGet(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();

        // GET BY ID
        if (query != null && query.contains("id=")) {

            int id = Integer.parseInt(query.split("id=")[1].split("&")[0]);

            Notification notification = notificationService.getNotificationById(id);

            if (notification == null) {
                sendResponse(exchange, 404,
                        "{\"error\":\"Notification not found\"}");
                return;
            }

            sendResponse(exchange, 200, gson.toJson(notification));
            return;
        }

        // GET BY MEMBER ID
        if (query != null && query.contains("memberId=")) {

            int memberId = Integer.parseInt(query.split("memberId=")[1].split("&")[0]);

            List<Notification> notifications =
                    notificationService.getNotificationsByMemberId(memberId);

            sendResponse(exchange, 200, gson.toJson(notifications));
            return;
        }

        // GET ALL
        List<Notification> all = notificationService.getAllNotifications();
        sendResponse(exchange, 200, gson.toJson(all));
    }

    // ========================= POST =========================
    private void handlePost(HttpExchange exchange) throws IOException {

        String body = readBody(exchange);

        Notification notification = gson.fromJson(body, Notification.class);

        boolean created = notificationService.addNotification(notification);

        if (created) {
            emailServiceAdapter.sendNotificationEmail(notification);

            sendResponse(exchange, 201,
                    "{\"message\":\"Notification created\"}");
        } else {
            sendResponse(exchange, 400,
                    "{\"error\":\"Notification not created\"}");
        }
    }

    // ========================= PUT =========================
    private void handlePut(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();

        // MARK ONE AS READ
        if (query != null && query.contains("markAsRead=")) {

            int id = Integer.parseInt(query.split("markAsRead=")[1].split("&")[0]);

            boolean updated = notificationService.markAsRead(id);

            if (updated) {
                sendResponse(exchange, 200,
                        "{\"message\":\"Notification marked as read\"}");
            } else {
                sendResponse(exchange, 404,
                        "{\"error\":\"Notification not found\"}");
            }
            return;
        }

        // MARK ALL AS READ
        if (query != null && query.contains("markAllAsRead=")) {

            int memberId = Integer.parseInt(query.split("markAllAsRead=")[1].split("&")[0]);

            boolean updated = notificationService.markAllAsRead(memberId);

            if (updated) {
                sendResponse(exchange, 200,
                        "{\"message\":\"All notifications marked as read\"}");
            } else {
                sendResponse(exchange, 404,
                        "{\"error\":\"No notifications found\"}");
            }
            return;
        }
    }

    // ========================= DELETE =========================
    private void handleDelete(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.contains("id=")) {
            sendResponse(exchange, 400,
                    "{\"error\":\"Missing id\"}");
            return;
        }

        int id = Integer.parseInt(query.split("id=")[1]);

        boolean deleted = notificationService.deleteNotification(id);

        if (deleted) {
            sendResponse(exchange, 200,
                    "{\"message\":\"Deleted\"}");
        } else {
            sendResponse(exchange, 404,
                    "{\"error\":\"Not found\"}");
        }
    }

    // ========================= BODY =========================
    private String readBody(HttpExchange exchange) throws IOException {

        InputStream inputStream = exchange.getRequestBody();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );

        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    // ========================= RESPONSE =========================
    private void sendResponse(HttpExchange exchange, int status, String response) throws IOException {

        addCorsHeaders(exchange);

        exchange.getResponseHeaders().set("Content-Type", "application/json");

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(status, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    // ========================= CORS FIX =========================
    private void addCorsHeaders(HttpExchange exchange) {

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}