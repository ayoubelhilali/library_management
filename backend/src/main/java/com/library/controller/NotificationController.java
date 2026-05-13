package com.library.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.model.Notification;
import com.library.service.NotificationService;
import com.library.utils.LocalDateAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class NotificationController implements HttpHandler {

    private final NotificationService notificationService =
            new NotificationService();

    private final Gson gson =
            new GsonBuilder()
                    .registerTypeAdapter(
                            LocalDate.class,
                            new LocalDateAdapter()
                    )
                    .create();

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        try {

            String method =
                    exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

                sendResponse(exchange, 204, "");

                return;
            }

            switch (method) {

                case "GET" -> handleGet(exchange);

                case "POST" -> handlePost(exchange);

                case "PUT" -> handlePut(exchange);

                case "DELETE" -> handleDelete(exchange);

                default ->
                        sendResponse(
                                exchange,
                                405,
                                "{\"error\":\"Method not allowed\"}"
                        );
            }

        } catch (IllegalArgumentException e) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"" + e.getMessage() + "\"}"
            );

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"error\":\"Internal server error\"}"
            );
        }
    }


    private void handleGet(HttpExchange exchange)
            throws IOException {

        String query =
                exchange.getRequestURI().getQuery();

        // GET BY ID
        if (query != null && query.startsWith("id=")) {

            int id =
                    Integer.parseInt(query.substring(3));

            Notification notification =
                    notificationService
                            .getNotificationById(id);

            if (notification == null) {

                sendResponse(
                        exchange,
                        404,
                        "{\"error\":\"Notification not found\"}"
                );

                return;
            }

            sendResponse(
                    exchange,
                    200,
                    gson.toJson(notification)
            );

            return;
        }

        // GET BY MEMBER ID
        if (query != null &&
                query.startsWith("memberId=")) {

            int memberId =
                    Integer.parseInt(query.substring(9));

            List<Notification> notifications =
                    notificationService
                            .getNotificationsByMemberId(
                                    memberId
                            );

            sendResponse(
                    exchange,
                    200,
                    gson.toJson(notifications)
            );

            return;
        }

        // GET ALL
        List<Notification> notifications =
                notificationService
                        .getAllNotifications();

        sendResponse(
                exchange,
                200,
                gson.toJson(notifications)
        );
    }

    // ========================= POST =========================

    private void handlePost(HttpExchange exchange)
            throws IOException {

        String body = readBody(exchange);

        Notification notification =
                gson.fromJson(body, Notification.class);

        boolean created =
                notificationService
                        .addNotification(notification);

        if (created) {

            sendResponse(
                    exchange,
                    201,
                    "{\"message\":\"Notification added successfully\"}"
            );

        } else {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Notification not added\"}"
            );
        }
    }

    // ========================= PUT =========================

    private void handlePut(HttpExchange exchange)
            throws IOException {

        String query =
                exchange.getRequestURI().getQuery();

        // MARK AS READ
        if (query != null &&
                query.startsWith("markAsRead=")) {

            int id =
                    Integer.parseInt(
                            query.substring(11)
                    );

            boolean updated =
                    notificationService.markAsRead(id);

            if (updated) {

                sendResponse(
                        exchange,
                        200,
                        "{\"message\":\"Notification marked as read\"}"
                );

            } else {

                sendResponse(
                        exchange,
                        404,
                        "{\"error\":\"Notification not found\"}"
                );
            }

            return;
        }

        // UPDATE FULL NOTIFICATION
        String body = readBody(exchange);

        Notification notification =
                gson.fromJson(body, Notification.class);

        boolean updated =
                notificationService
                        .updateNotification(notification);

        if (updated) {

            sendResponse(
                    exchange,
                    200,
                    "{\"message\":\"Notification updated successfully\"}"
            );

        } else {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Notification not updated\"}"
            );
        }
    }

    // ========================= DELETE =========================

    private void handleDelete(HttpExchange exchange)
            throws IOException {

        String query =
                exchange.getRequestURI().getQuery();

        if (query == null ||
                !query.startsWith("id=")) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Missing notification id\"}"
            );

            return;
        }

        int id =
                Integer.parseInt(query.substring(3));

        boolean deleted =
                notificationService
                        .deleteNotification(id);

        if (deleted) {

            sendResponse(
                    exchange,
                    200,
                    "{\"message\":\"Notification deleted successfully\"}"
            );

        } else {

            sendResponse(
                    exchange,
                    404,
                    "{\"error\":\"Notification not found\"}"
            );
        }
    }

    // ========================= HELPERS =========================

    private String readBody(HttpExchange exchange)
            throws IOException {

        InputStream inputStream =
                exchange.getRequestBody();

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                inputStream,
                                StandardCharsets.UTF_8
                        )
                );

        StringBuilder body =
                new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {

            body.append(line);
        }

        return body.toString();
    }

    // private void sendResponse(
    //         HttpExchange exchange,
    //         int statusCode,
    //         String response
    // ) throws IOException {

    //     byte[] responseBytes =
    //             response.getBytes(
    //                     StandardCharsets.UTF_8
    //             );

    //     exchange.getResponseHeaders()
    //             .set(
    //                     "Content-Type",
    //                     "application/json"
    //             );

    //     exchange.sendResponseHeaders(
    //             statusCode,
    //             responseBytes.length
    //     );

    //     OutputStream outputStream =
    //             exchange.getResponseBody();

    //     outputStream.write(responseBytes);

    //     outputStream.close();
    // }


    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");

        // CORS
        exchange.getResponseHeaders()
                .set("Access-Control-Allow-Origin", "*");

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS"
                );

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Headers",
                        "Content-Type"
                );

        byte[] responseBytes =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length
        );

        OutputStream outputStream =
                exchange.getResponseBody();

        outputStream.write(responseBytes);

        outputStream.close();
    }




}