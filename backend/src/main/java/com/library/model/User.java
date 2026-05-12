package com.library.model;

import com.library.model.enums.userRole;

public abstract class User {

    private int id;
    private String username ;
    private String email ;
    private String phone ;
    private String password;

    // ADMIN / ADHERENT
    // private String role;

    // private Integer memberId;

    public User() {
    }

    public User(int id,String username,String email,String phone, String password) {
        this.id = id;
        this.username = username ;
        this.password = password;
        this.email = email ;
        this.phone = phone ;
        // this.role = role;
        // this.memberId = memberId;
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


    // public String getRole() {
    //     return role;
    // }

    // public void setRole(String role) {
    //     this.role = role;
    // }


    // public Integer getMemberId() {
    //     return memberId;
    // }

    // public void setMemberId(Integer memberId) {
    //     this.memberId = memberId;
    // }



    public String getEmail(){
        return email ;
    }

    public void setEmail(String email){
        this.email = email ;
    }


    public String getPhone(){
        return phone ;
    }

    public void setPhone(String phone){
        this.phone = phone ;
    }




    public abstract userRole getRole() ;



}