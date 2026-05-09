package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;

import java.util.List;

public class BookService {

    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public Book getBookById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid book id");
        }

        return bookDAO.getBookById(id);
    }

    public boolean addBook(Book book) {

        validateBook(book);

        // Check ISBN duplication
        List<Book> books = bookDAO.getAllBooks();

        boolean isbnExists = books.stream()
                .anyMatch(b -> b.getIsbn()
                        .equalsIgnoreCase(book.getIsbn()));

        if (isbnExists) {
            throw new IllegalArgumentException("ISBN already exists");
        }

        if (book.getStatus() == null || book.getStatus().isBlank()) {
            book.setStatus("AVAILABLE");
        }
        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) {

        if (book.getId() <= 0) {
            throw new IllegalArgumentException("Invalid book id");
        }

        validateBook(book);

        Book existingBook = bookDAO.getBookById(book.getId());

        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found");
        }

        return bookDAO.updateBook(book);
    }

    public boolean deleteBook(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid book id");
        }

        Book existingBook = bookDAO.getBookById(id);

        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found");
        }

        return bookDAO.deleteBook(id);
    }

    public List<Book> searchBooks(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllBooks();
        }

        return bookDAO.searchBooks(keyword.trim());
    }

    private void validateBook(Book book) {

        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Author is required");
        }

        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN is required");
        }

        if (book.getTitle().length() > 150) {
            throw new IllegalArgumentException("Title too long");
        }

        if (book.getAuthor().length() > 100) {
            throw new IllegalArgumentException("Author name too long");
        }
    }
}