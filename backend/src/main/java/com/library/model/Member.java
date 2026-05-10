package com.library.model ;

import com.library.model.enums.MemberType;

public abstract class Member {
    private int id ;
    private String name ;
    private String email ;
    private String phone ;


    public Member(){}

    public Member(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public abstract MemberType getMemberType() ;


    public abstract int getMaxBorrowLimit();



    //getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }


    //setters

    public void setId(int id){
        this.id = id ;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + getMemberType() + '\'' +
                '}';
    }



}