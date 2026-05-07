package com.library.patterns.prototype ;

import com.library.model.Book;
import com.library.patterns.builder.BookBuilder;

public class BookPrototype {


    private Book book ;

    public BookPrototype(Book book){
        this.book = book ;
    }

    public Book clone(){

        return new BookBuilder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .category(book.getCategory())
                    .isbn(book.getIsbn())
                    .status(book.getStatus())
                    .build() ;
    }
}