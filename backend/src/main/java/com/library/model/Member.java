package com.library.model ;

import com.library.model.enums.MemberType;
import com.library.model.enums.userRole;

public abstract class Member extends User{





    public Member(){}

    public Member(int id,String username, String email,String phone, String password) {
        super(id, username,email, phone, password) ;
    }


    public abstract MemberType getMemberType() ;


    public abstract int getMaxBorrowLimit();



    // //getters
    // public int getId() {
    //     return id;
    // }

    // public String getName() {
    //     return name;
    // }

    // public String getEmail() {
    //     return email;
    // }

    // public String getPhone() {
    //     return phone;
    // }


    //setters

    // public void setId(int id){
    //     this.id = id ;
    // }
    
    // public void setName(String name) {
    //     this.name = name;
    // }

    // public void setEmail(String email) {
    //     this.email = email;
    // }

    // public void setPhone(String phone) {
    //     this.phone = phone;
    // }




    // @Override
    // public String toString() {
    //     return "Member{" +
    //             "id=" + id +
    //             ", name='" + name + '\'' +
    //             ", email='" + email + '\'' +
    //             ", phone='" + phone + '\'' +
    //             ", type='" + getMemberType() + '\'' +
    //             '}';
    // }


    @Override
    public userRole getRole(){
        return userRole.MEMBER ;
    }



}