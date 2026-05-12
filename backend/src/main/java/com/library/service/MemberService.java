package com.library.service;

import com.library.dao.MemberDAO;
import com.library.model.Member;

import java.util.List;

public class MemberService {

    private final MemberDAO memberDAO;

    public MemberService() {
        this.memberDAO = new MemberDAO();
    }

    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    public Member getMemberById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid member id");
        }

        return memberDAO.getMemberById(id);
    }

    public boolean addMember(Member member) {

        validateMember(member);

        return memberDAO.addMember(member);
    }

    // public boolean updateMember(Member member) {

    //     if (member.getId() <= 0) {
    //         throw new IllegalArgumentException("Invalid member id");
    //     }

    //     validateMember(member);

    //     Member existingMember = memberDAO.getMemberById(member.getId());

    //     if (existingMember == null) {
    //         throw new IllegalArgumentException("Member not found");
    //     }

    //     return memberDAO.updateMember(member);
    // }

    public boolean deleteMember(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid member id");
        }

        Member existingMember = memberDAO.getMemberById(id);

        if (existingMember == null) {
            throw new IllegalArgumentException("Member not found");
        }

        return memberDAO.deleteMember(id);
    }

    // public List<Member> searchMembers(String keyword) {

    //     if (keyword == null || keyword.isBlank()) {
    //         return getAllMembers();
    //     }

    //     return memberDAO.searchMembers(keyword.trim());
    // }

    private void validateMember(Member member) {

        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        // if (member.getName() == null || member.getName().isBlank()) {
        //     throw new IllegalArgumentException("Name is required");
        // }

        if (member.getEmail() == null || member.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (member.getPhone() == null || member.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }

        if (!member.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email format is invalid");
        }

        if (member.getMemberType() == null) {
            throw new IllegalArgumentException("Member type is required");
        }
    }
}
