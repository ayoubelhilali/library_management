package com.library.model ;

import com.library.model.enums.MemberType;

public class TeacherMember extends Member {




    public TeacherMember(){}

    public TeacherMember(
        int id,
        String username, 
        String email,
        String phone, 
        String password
  
    ) {

        super(id, username,email, phone, password) ;

    }



    @Override
    public int getMaxBorrowLimit() {
        return 10 ;
    }

    @Override
    public MemberType getMemberType() {
        return MemberType.TEACHER ;
    }

    
    
}
