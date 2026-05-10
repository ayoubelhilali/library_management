package com.library.dao ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.library.model.Member;
import com.library.model.StudentMember;
import com.library.model.enums.MemberType;
import com.library.patterns.factory.MemberFactory;
import com.library.patterns.singleton.DBConnection;

public class MemberDAO {

    private final Connection connection ;

    public MemberDAO(){
        connection = DBConnection.getInstance().getConnection() ;
    }


    public List<Member> getAllMembers(){

        List<Member> members = new ArrayList<>() ;

        String sql = "SELECT * FROM MEMBERS " ;


        try(
            Statement stmt = connection.createStatement() ;

            ResultSet rs = stmt.executeQuery(sql) ;
        ){

            while (rs.next()){

                MemberType type = MemberType.valueOf(rs.getString("type")) ;

                Member member = MemberFactory.createMember(rs.getInt("id"),
                                                            rs.getString("name"),
                                                            rs.getString("email"),
                                                            rs.getString("phone"),
                                                            type) ;


                members.add(member) ;
                    
                };
            
        }catch(SQLException e){
            e.printStackTrace();
        } 

        return members ;

        
    } 



    public boolean addMember(Member member){

        String sql = "INSERT INTO MEMBERS VALUES (?,?,?,?,?)" ;

        try(PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getMemberType().toString());

            int rows = ps.executeUpdate() ;
            return rows > 0 ;
        }catch(SQLException e){
            e.printStackTrace(); 
        }


        return false ;


    }



    public Member getMemberById(int id) {

        String sql = "SELECT * FROM MEMBERS WHERE id = ?" ;

        try(PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    MemberType type = MemberType.valueOf(rs.getString("type")) ;

                    return MemberFactory.createMember(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            type
                    ) ;
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null ;
    }



    public boolean updateMember(Member member) {

        String sql = "UPDATE MEMBERS SET name = ?, email = ?, phone = ? WHERE id = ?" ;

        try(PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setInt(4, member.getId());

            int rows = ps.executeUpdate() ;

            return rows > 0 ;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false ;
    }



    public List<Member> searchMembers(String keyword) {

        List<Member> members = new ArrayList<>() ;

        String sql = "SELECT * FROM MEMBERS WHERE LOWER(name) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) OR LOWER(phone) LIKE LOWER(?)" ;

        try(PreparedStatement ps = connection.prepareStatement(sql)){

            String searchPattern = "%" + keyword + "%" ;

            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){

                    MemberType type = MemberType.valueOf(rs.getString("type")) ;

                    Member member = MemberFactory.createMember(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            type
                    ) ;

                    members.add(member) ;
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return members ;
    }



    public boolean deleteMember(int id){

        String sql = "DELETE FROM MEMBERS WHERE id = ?" ;

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);

            int rows = ps.executeUpdate() ;

            return rows > 0 ;
        }catch(SQLException e){
            e.printStackTrace(); 
        }

        return false ;
    }



}