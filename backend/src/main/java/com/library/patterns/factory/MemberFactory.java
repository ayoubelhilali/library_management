package com.library.patterns.factory ;

import com.library.model.Member;
import com.library.model.StudentMember;
import com.library.model.TeacherMember;

public class MemberFactory {

    public static Member createStudentMember(
            int id,
            String name,
            String email,
            String phone,
            String university,
            String studentCardNumber
    ) {

        return new StudentMember(
                id,
                name,
                email,
                phone,
                university,
                studentCardNumber
        );
    }

    public static Member createTeacherMember(
            int id,
            String name,
            String email,
            String phone,
            String department,
            String employeeNumber
    ) {

        return new TeacherMember(
                id,
                name,
                email,
                phone,
                department,
                employeeNumber
        );
    }
}