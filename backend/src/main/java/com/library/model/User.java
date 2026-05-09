package com.library.model;

public class User {

    private int id;

    private String username;
    private String password;

    // ADMIN / ADHERENT
    private String role;

    private Integer memberId;

    public User() {
    }

    public User(int id, String username, String password, String role, Integer memberId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.memberId = memberId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}