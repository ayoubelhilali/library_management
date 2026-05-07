package com.library.dao;
import com.library.model.Book;
import com.library.patterns.singleton.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private final Connection connection;
    public BookDAO(){
        connection=DBConnection.getInstance().getConnection();
    }

    // Get all books
    public List<Book> getAllBooks(){
        List<Book> books=new ArrayList<>();
        String query="select * from books";
        try(
                Statement stmt=connection.createStatement();
                ResultSet rs=stmt.executeQuery(query)
                ) {
                while (rs.next()){
                    Book book=new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setCategory(rs.getString("category"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setStatus(rs.getString("status"));

                    books.add(book);
                }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return books;
    }
    public boolean addBook(Book book){
        String query="INSERT INTO books VALUES(?,?,?,?,?)";
        try(PreparedStatement ps=connection.prepareStatement(query)){
            ps.setString(1,book.getTitle());
            ps.setString(2,book.getAuthor());
            ps.setString(3,book.getCategory());
            ps.setString(4,book.getIsbn());
            ps.setString(5,book.getStatus());

            int rows=ps.executeUpdate();
            return rows>0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteBook(int id){
        String query="DELETE FROM books WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(query)){
            ps.setInt(1,id);
            int rows=ps.executeUpdate();
            return rows>0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}