package com.library.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.model.Reservation;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.enums.ReservationStatus;
import com.library.service.ReservationService;
import com.library.patterns.observer.ReservationEvent;
import com.library.patterns.observer.NotificationObserver;
import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.utils.LocalDateAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class ReservationController implements HttpHandler {

    private final ReservationService reservationService = new ReservationService();
    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

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
            Reservation reservation = reservationService.getReservationById(id);

            if (reservation == null) {
                sendResponse(exchange, 404, "{\"error\":\"Reservation not found\"}");
                return;
            }

            sendResponse(exchange, 200, gson.toJson(reservation));
            return;
        }

        if (query != null && query.startsWith("memberId=")) {
            int memberId = Integer.parseInt(query.substring(9));
            List<Reservation> reservations = reservationService.getReservationsByMemberId(memberId);
            sendResponse(exchange, 200, gson.toJson(reservations));
            return;
        }

        if (query != null && query.startsWith("bookId=")) {
            int bookId = Integer.parseInt(query.substring(7));
            List<Reservation> reservations = reservationService.getReservationsByBookId(bookId);
            sendResponse(exchange, 200, gson.toJson(reservations));
            return;
        }

        if (query != null && query.startsWith("status=")) {
            String statusStr = query.substring(7);
            ReservationStatus status = ReservationStatus.valueOf(statusStr);
            List<Reservation> reservations = reservationService.getReservationsByStatus(status);
            sendResponse(exchange, 200, gson.toJson(reservations));
            return;
        }

        List<Reservation> reservations = reservationService.getAllReservations();
        sendResponse(exchange, 200, gson.toJson(reservations));
    }

    private void handlePost(HttpExchange exchange) throws IOException {

        String body = readBody(exchange);
        Reservation reservation = gson.fromJson(body, Reservation.class);

        boolean created = reservationService.addReservation(reservation);

        if (created) {
            // Apply Observer Pattern - notify member of reservation event
            try {
                Book book = bookDAO.getBookById(reservation.getBookId());
                Member member = memberDAO.getMemberById(reservation.getMemberId());
                
                if (book != null && member != null) {
                    ReservationEvent reservationEvent = new ReservationEvent(reservation.getReservationId(), reservation.getMemberId(), book.getTitle());
                    new NotificationObserver(member, reservationEvent);
                    reservationEvent.createReservation();
                }
            } catch (Exception e) {
                System.err.println("Error notifying member of reservation: " + e.getMessage());
            }
            
            sendResponse(exchange, 201, "{\"message\":\"Reservation created successfully\"}");
        } else {
            sendResponse(exchange, 400, "{\"error\":\"Reservation not created\"}");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {

        String body = readBody(exchange);
        Reservation reservation = gson.fromJson(body, Reservation.class);

        boolean updated = reservationService.updateReservation(reservation);

        if (updated) {
            sendResponse(exchange, 200, "{\"message\":\"Reservation updated successfully\"}");
        } else {
            sendResponse(exchange, 400, "{\"error\":\"Reservation not updated\"}");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.startsWith("id=")) {
            sendResponse(exchange, 400, "{\"error\":\"Missing reservation id\"}");
            return;
        }

        int id = Integer.parseInt(query.substring(3));
        boolean deleted = reservationService.deleteReservation(id);

        if (deleted) {
            sendResponse(exchange, 200, "{\"message\":\"Reservation deleted successfully\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Reservation not found\"}");
        }
    }

    private String readBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder body = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        return body.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {

        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.close();
    }
}
