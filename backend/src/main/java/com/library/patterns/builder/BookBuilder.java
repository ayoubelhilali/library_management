package com.library.patterns.builder ;

import com.library.model.Book;

public class BookBuilder {

    private Book book ;

    public BookBuilder(){
        book = new Book() ;
    }

    public BookBuilder id(int id){
        book.setId(id);

        return this ;
    }


    public BookBuilder title(String title){
        book.setTitle(title);

        return this ;
    }


    public BookBuilder author(String author){
        book.setAuthor(author);

        return this ;
    }

    public BookBuilder category(String category){
        book.setCategory(category);

        return this ;
    }


    public BookBuilder isbn(String isbn){
        book.setIsbn(isbn) ;

        return this ;
    }


    public BookBuilder status(String status){
        book.setStatus(status);

        return this ;
    }


    public Book build(){
        return book ;
    }

}