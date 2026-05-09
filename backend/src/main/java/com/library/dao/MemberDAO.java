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