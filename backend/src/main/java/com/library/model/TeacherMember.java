package com.library.model ;

import com.library.model.enums.MemberType;

public class TeacherMember extends Member {




    public TeacherMember(){}

    public TeacherMember(
            int id,
            String name,
            String email,
            String phone
  
    ) {
        super(id, name, email, phone);

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
