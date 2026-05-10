package com.library.model ;

import com.library.model.enums.MemberType;

public class StudentMember extends Member {

 


    public StudentMember(){
        
    }


    public StudentMember(
            int id,
            String name,
            String email,
            String phone

    ) {

        super(id, name, email, phone);

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