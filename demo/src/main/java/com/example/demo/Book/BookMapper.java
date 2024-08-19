package com.example.demo.Book;

import com.example.demo.Book.Request.BookRequest;
import com.example.demo.Book.Response.BookResponse;
import com.example.demo.Book.Response.BorrowedBookResponse;
import com.example.demo.History.BookTransactionHistory;

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

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory){
        return BorrowedBookResponse.builder()
        .id(bookTransactionHistory.getBook().getId())
        .title(bookTransactionHistory.getBook().getTitle())
        .authorName(bookTransactionHistory.getBook().getAuthorName())
        .isbn(bookTransactionHistory.getBook().getIsbn())
        .returned(bookTransactionHistory.isReturned())
        .returnedApproved(bookTransactionHistory.isReturnApproved())
        .build();
        }



}
