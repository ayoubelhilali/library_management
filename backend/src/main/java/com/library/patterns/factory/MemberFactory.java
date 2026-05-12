package com.library.patterns.factory ;

import com.library.model.Member;
import com.library.model.StudentMember;
import com.library.model.TeacherMember;
import com.library.model.enums.MemberType;

public class MemberFactory {

    public static Member createMember(
                int id,
                String username, 
                String email,
                String phone, 
                String password,
                MemberType type
    ) {


        if(type == MemberType.STUDENT){

                return new StudentMember(
                        id,
                        username,
                        email,
                        phone,
                        password
                );

        }else{

                return new TeacherMember( 
                        id,
                        username,
                        email,
                        phone,
                        password
                );

        }

    }


}