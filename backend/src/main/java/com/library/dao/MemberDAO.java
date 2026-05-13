package com.library.dao ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.library.model.Member;
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

        String sql = """
                    SELECT *
                    FROM users u
                    JOIN members m
                    ON u.id = m.user_id
                    WHERE u.role = 'MEMBER'
                """;


        try(
            Statement stmt = connection.createStatement() ;

            ResultSet rs = stmt.executeQuery(sql) ;
        ){

            while (rs.next()){

                MemberType type = MemberType.valueOf(rs.getString("member_type")) ;

                Member member = MemberFactory.createMember(rs.getInt("user_id"),
                                                            rs.getString("username"),
                                                            rs.getString("email"),
                                                            rs.getString("phone"),
                                                            rs.getString("password"),
                                                            type) ;


                members.add(member) ;
                    
                };
            
        }catch(SQLException e){
            e.printStackTrace();
        } 

        return members ;

        
    } 



    // public boolean addMember(Member member){

    //     String userSql = """
    //                 INSERT INTO users(username, email, phone, password, role)
    //                 VALUES (?, ?, ?, ?, ?)
    //             """;

    //     String memberSql = """
    //                 INSERT INTO members(user_id, member_type)
    //                 VALUES (?, ?)
    //             """;

    //     try(PreparedStatement ps1 = connection.prepareStatement(userSql)
    //         PreparedStatement ps2 = connection.prepareStatement(memberSql)){

    //         ps1.setString(1, member.getUsername());
    //         ps1.setString(2, member.getEmail());
    //         ps1.setString(3, member.getPhone());
    //         ps1.setString(4, member.getPassword());
    //         ps1.setString(5, member.getMemberType().toString());


    //         ps2.

    //         int rows = ps.executeUpdate() ;
    //         return rows > 0 ;
    //     }catch(SQLException e){
    //         e.printStackTrace(); 
    //     }


    //     return false ;


    // }


    public boolean addMember(Member member){

        String userSql = """
            INSERT INTO users(username, email, phone, password, role)
            VALUES (?, ?, ?, ?, ?)
        """;

        String memberSql = """
            INSERT INTO members(user_id, member_type)
            VALUES (?, ?)
        """;

        try {

            connection.setAutoCommit(false);


            int generatedUserId;

            try (PreparedStatement userPs =
                        connection.prepareStatement(
                                userSql,
                                Statement.RETURN_GENERATED_KEYS
                        )) {

                userPs.setString(1, member.getUsername());
                userPs.setString(2, member.getEmail());
                userPs.setString(3, member.getPhone());
                userPs.setString(4, member.getPassword());
                userPs.setString(5, "MEMBER");

                int userRows = userPs.executeUpdate();

                if (userRows == 0) {
                    connection.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = userPs.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        generatedUserId = generatedKeys.getInt(1);

                    } else {

                        connection.rollback();
                        return false;
                    }
                }
            }


            try (PreparedStatement memberPs =
                        connection.prepareStatement(memberSql)) {

                memberPs.setInt(1, generatedUserId);

                memberPs.setString(
                        2,
                        member.getMemberType().toString()
                );

                int memberRows = memberPs.executeUpdate();

                if (memberRows == 0) {

                    connection.rollback();
                    return false;
                }
            }

            connection.commit();

            return true;

        } catch (SQLException e) {

            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
        } finally {

            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    public Member getMemberById(int id) {

        String sql = """
                SELECT *
                FROM users u
                JOIN members m
                ON u.id = m.user_id
                WHERE u.id = ?
            """;

        try(PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    MemberType type = MemberType.valueOf(rs.getString("member_type")) ;

                    return MemberFactory.createMember(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password"),
                            type
                    ) ;
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null ;
    }



    // public boolean updateMember(Member member) {

    //     String sql = "UPDATE MEMBERS SET username = ?, email = ?, phone = ? WHERE id = ?" ;

    //     try(PreparedStatement ps = connection.prepareStatement(sql)){

    //         ps.setString(1, member.getUsername());
    //         ps.setString(2, member.getEmail());
    //         ps.setString(3, member.getPhone());
    //         ps.setInt(4, member.getId());

    //         int rows = ps.executeUpdate() ;

    //         return rows > 0 ;

    //     }catch(SQLException e){
    //         e.printStackTrace();
    //     }

    //     return false ;
    // }



    // public List<Member> searchMembers(String keyword) {

    //     List<Member> members = new ArrayList<>() ;

    //     String sql = "SELECT * FROM MEMBERS WHERE LOWER(name) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) OR LOWER(phone) LIKE LOWER(?)" ;

    //     try(PreparedStatement ps = connection.prepareStatement(sql)){

    //         String searchPattern = "%" + keyword + "%" ;

    //         ps.setString(1, searchPattern);
    //         ps.setString(2, searchPattern);
    //         ps.setString(3, searchPattern);

    //         try(ResultSet rs = ps.executeQuery()){

    //             while(rs.next()){

    //                 MemberType type = MemberType.valueOf(rs.getString("type")) ;

    //                 Member member = MemberFactory.createMember(
    //                         rs.getInt("id"),
    //                         rs.getString("username"),
    //                         rs.getString("email"),
    //                         rs.getString("phone"),
    //                         rs.getString("password"),
    //                         type
    //                 ) ;

    //                 members.add(member) ;
    //             }
    //         }

    //     }catch(SQLException e){
    //         e.printStackTrace();
    //     }

    //     return members ;
    // }



    public boolean deleteMember(int id){

        String sql = "DELETE FROM users WHERE id = ?" ;

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