package com.library.model ;

import com.library.model.enums.MemberType;

public class StudentMember extends Member {

 


    public StudentMember(){
        
    }


    public StudentMember(
        int id,
        String username, 
        String email,
        String phone, 
        String password

    ) {

        super(id, username,email, phone, password) ;

    }



    @Override
    public MemberType getMemberType(){
        return MemberType.STUDENT ;
    }


    @Override
    public int getMaxBorrowLimit() {
        return 3;
    }


    


   
}