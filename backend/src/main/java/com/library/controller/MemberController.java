package com.library.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.library.model.Member;
import com.library.model.StudentMember;
import com.library.model.TeacherMember;
import com.library.model.enums.MemberType;
import com.library.service.MemberService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MemberController implements HttpHandler {

    private final MemberService memberService = new MemberService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            switch (method) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                // case "PUT" -> handlePut(exchange);
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
            Member member = memberService.getMemberById(id);

            if (member == null) {
                sendResponse(exchange, 404, "{\"error\":\"Member not found\"}");
                return;
            }

            sendResponse(exchange, 200, gson.toJson(toMemberResponse(member)));
            return;
        }

        List<Member> members = memberService.getAllMembers();

        List<MemberResponse> response = members
                .stream()
                .map(this::toMemberResponse)
                .toList();

        sendResponse(exchange, 200, gson.toJson(response));
    }
    private void handlePost(HttpExchange exchange) throws IOException {

        String body = readBody(exchange);

        JsonObject jsonObject =
            gson.fromJson(body, JsonObject.class);

        

        Member member ;

        String typeString =
            jsonObject.get("memberType").getAsString();

        MemberType type =
            MemberType.valueOf(typeString);
        
        if(type == MemberType.STUDENT) member =  gson.fromJson(body, StudentMember.class);
        else if(type == MemberType.TEACHER) member = gson.fromJson(body, TeacherMember.class) ;
        else{

            sendResponse(
                exchange,
                400,
                "{\"error\":\"Invalid member type\"}"
            );

            return ;
        }

        boolean created = memberService.addMember(member);

        if (created) {
            sendResponse(exchange, 201, "{\"message\":\"Member added successfully\"}");
        } else {
            sendResponse(exchange, 400, "{\"error\":\"Member not added\"}");
        }
    }

    // private void handlePut(HttpExchange exchange) throws IOException {

    //     String body = readBody(exchange);
    //     Member member = gson.fromJson(body, Member.class);

    //     boolean updated = memberService.updateMember(member);

    //     if (updated) {
    //         sendResponse(exchange, 200, "{\"message\":\"Member updated successfully\"}");
    //     } else {
    //         sendResponse(exchange, 400, "{\"error\":\"Member not updated\"}");
    //     }
    // }

    private void handleDelete(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.startsWith("id=")) {
            sendResponse(exchange, 400, "{\"error\":\"Missing member id\"}");
            return;
        }

        int id = Integer.parseInt(query.substring(3));
        boolean deleted = memberService.deleteMember(id);

        if (deleted) {
            sendResponse(exchange, 200, "{\"message\":\"Member deleted successfully\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Member not found\"}");
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

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );

        exchange.getResponseHeaders().set(
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
    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getPhone(),
                member.getMemberType().toString()
        );
    }
    private static class MemberResponse {
        int id;
        String username;
        String email;
        String phone;
        String memberType;

        MemberResponse(
                int id,
                String username,
                String email,
                String phone,
                String memberType
        ) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.memberType = memberType;
        }
    }
}
