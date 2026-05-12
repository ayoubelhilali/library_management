package com.library.model;

import com.library.model.enums.userRole;

public class Admin extends User {


    public Admin(){}


    public Admin(int id,String username, String email,String phone, String password) {
        super(id, username,email, phone, password) ;
    }
    


    public userRole getRole(){

        return userRole.ADMIN ;

    }
}
