package com.library.model ;

import com.library.model.enums.MemberType;

public class TeacherMember extends Member {

    private String department;
    private String employeeNumber;


    public TeacherMember(
            int id,
            String name,
            String email,
            String phone,
            String department,
            String employeeNumber
    ) {
        super(id, name, email, phone);
        this.department = department;
        this.employeeNumber = employeeNumber;
    }



    public String getDepartment() {
        return department;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
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
