package com.example.demo.Book;

import com.example.demo.Book.Request.BookRequest;

public class BookMapper {

    public Book toBook(BookRequest request) {
        return Book.builder()
        .id(request.id())
        .isbn(request.isbn())
        .synopsis(request.synopsis())
        .archived(false)
        .shareble(request.shareble())
        .build();
    }

}
