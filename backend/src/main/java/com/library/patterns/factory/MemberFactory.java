package com.library.patterns.factory ;

import com.library.model.Member;
import com.library.model.StudentMember;
import com.library.model.TeacherMember;
import com.library.model.enums.MemberType;

public class MemberFactory {

    public static Member createMember(
            int id,
            String name,
            String email,
            String phone ,
            MemberType type
    ) {


        if(type == MemberType.STUDENT){

                return new StudentMember(
                        id,
                        name,
                        email,
                        phone
                );

        }else{

                return new TeacherMember( 
                        id,
                        name,
                        email,
                        phone
                );

        }

    }


}