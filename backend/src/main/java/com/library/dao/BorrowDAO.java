package com.library.dao;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.patterns.singleton.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {
    private final Connection connection;
    public  BorrowDAO() {
        connection = DBConnection.getInstance().getConnection();
    }
    public boolean createBorrow(Borrow borrow) {
        String query="""
        INSERT INTO borrows(book_id,member_id,borrow_date,expected_return_date,actual_return_date)
        VALUES (?,?,?,?,?)
        """;
        try(PreparedStatement ps=connection.prepareStatement(query)){
            ps.setInt(1,borrow.getBookID());
            ps.setInt(2,borrow.getMemberID());
            ps.setDate(3, Date.valueOf(borrow.getBorrowDate()));
            ps.setDate(4,Date.valueOf(borrow.getExpectedReturnDate()));
            if (borrow.getActualReturnDate() != null) {
                ps.setDate(5, Date.valueOf(borrow.getActualReturnDate()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            int rows=ps.executeUpdate();
            return rows>0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public List<Borrow> getAllBorrows(){
        List<Borrow> borrows=new ArrayList<Borrow>();
        String query="SELECT * FROM borrows";
        try (
                PreparedStatement ps=connection.prepareStatement(query);
                ResultSet rs=ps.executeQuery();
                ){
            while(rs.next()){
                Borrow borrow=new Borrow();
                borrow.setId(rs.getInt(1));
                borrow.setBookID(rs.getInt(2));
                borrow.setMemberID(rs.getInt(3));
                borrow.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                borrow.setExpectedReturnDate(rs.getDate("expected_return_date").toLocalDate());
                Date actualReturnDate = rs.getDate("actual_return_date");

                if (actualReturnDate != null) {
                    borrow.setActualReturnDate(
                            actualReturnDate.toLocalDate()
                    );
                }
                borrows.add(borrow);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return borrows;
    }
    public Borrow getBorrowById(int id){
        String query="SELECT * FROM borrows WHERE id=?";
        try(
                PreparedStatement ps=connection.prepareStatement(query);
        ){
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if (rs.next()){
                Borrow borrow=new Borrow();
                borrow.setId(rs.getInt("id"));
                borrow.setBookID(rs.getInt("book_id"));
                borrow.setMemberID(rs.getInt("member_id"));
                borrow.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                borrow.setExpectedReturnDate(rs.getDate("expected_return_date").toLocalDate());
                Date actualReturnDate = rs.getDate("actual_return_date");
                if (actualReturnDate != null) {
                    borrow.setActualReturnDate(actualReturnDate.toLocalDate());
                }
                return borrow;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Borrow getActiveBorrowByBookId(int bookId){
        String query= """
                    SELECT * from borrows
                    WHERE book_id=?
                    AND actual_return_date IS NULL
                """;
        try (
                PreparedStatement ps=connection.prepareStatement(query);
                ){
            ps.setInt(1,bookId);
            try(ResultSet rs=ps.executeQuery()){
                if (rs.next()){
                    Borrow borrow = new Borrow();

                    borrow.setId(rs.getInt("id"));
                    borrow.setBookID(rs.getInt("book_id"));
                    borrow.setMemberID(rs.getInt("member_id"));

                    borrow.setBorrowDate(
                            rs.getDate("borrow_date").toLocalDate()
                    );

                    borrow.setExpectedReturnDate(
                            rs.getDate("expected_return_date").toLocalDate()
                    );

                    Date actualReturnDate =
                            rs.getDate("actual_return_date");

                    if (actualReturnDate != null) {
                        borrow.setActualReturnDate(
                                actualReturnDate.toLocalDate()
                        );
                    }

                    return borrow;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean returnBook(int borrowId, LocalDate actualReturnDate){
        String query="""
                    UPDATE borrows
                    SET actual_return_date=?
                    where id=?
        """;
        try (PreparedStatement ps= connection.prepareStatement(query)){
                ps.setDate(1, Date.valueOf(actualReturnDate));
                ps.setInt(2, borrowId);
                int rows=ps.executeUpdate();
                return rows>0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}