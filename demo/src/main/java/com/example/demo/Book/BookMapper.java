package com.example.demo.Book;

import com.example.demo.Book.Request.BookRequest;
import com.example.demo.Book.Response.BookResponse;

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

    public BookResponse toBookResponse(Book book){
        return BookResponse.builder().id(book.getId())
        .authorName(book.getAuthorName())
        .rate(0.0)
        .shareable(book.isShareble())
        .synopsis(book.getSynopsis())
        .title(book.getTitle())
        .archived(book.isArchived())
        .owner(book.getOwner().getFullName())
        .build();
    }


}
