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
            fetchResult(books, rs);
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
    public Book getBookById(int id){
        String query="SELECT * FROM books WHERE id=?";
        try(
                PreparedStatement ps=connection.prepareStatement(query);
                ){
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if (rs.next()){
                Book book=new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setIsbn(rs.getString("isbn"));
                book.setStatus(rs.getString("status"));
                return book;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Boolean updateBook(Book book){
        String query ="UPDATE books SET title=?,author=?,category=?,isbn=?,status=? WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(query)){
            ps.setString(1,book.getTitle());
            ps.setString(2,book.getAuthor());
            ps.setString(3,book.getCategory());
            ps.setString(4,book.getIsbn());
            ps.setString(5,book.getStatus());
            ps.setInt(6,book.getId());
            int rows=ps.executeUpdate();
            return rows>0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String query = """
            SELECT * FROM books
            WHERE title LIKE ?
            OR author LIKE ?
            OR category LIKE ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();
            fetchResult(books, rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    private void fetchResult(List<Book> books, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setCategory(rs.getString("category"));
            book.setIsbn(rs.getString("isbn"));
            book.setStatus(rs.getString("status"));
            books.add(book);
        }
    }
}