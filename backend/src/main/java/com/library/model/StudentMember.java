package com.library.model ;

import com.library.model.enums.MemberType;

public class StudentMember extends Member {

    private String university;
    private String studentCardNumber;


    public StudentMember(
            int id,
            String name,
            String email,
            String phone,
            String university,
            String studentCardNumber
    ) {
        super(id, name, email, phone);
        this.university = university;
        this.studentCardNumber = studentCardNumber;
    }


    public String getUniversity() {
        return university;
    }

    public String getStudentCardNumber() {
        return studentCardNumber;
    }


    public void setUniversity(String university) {
        this.university = university;
    }

    public void setStudentCardNumber(String studentCardNumber) {
        this.studentCardNumber = studentCardNumber;
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