package com.example.demo.Book;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Book.Request.BookRequest;
import com.example.demo.Book.Response.BookResponse;
import com.example.demo.common.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class BookController {
 
    private final BookService bookService;

    public void SaveBook(
        @Valid
        @RequestBody
        BookRequest request,
        Authentication connectedUser
        ){
           bookService.Save(request, connectedUser);
    }


    @GetMapping("{Book_id}")
    public ResponseEntity<BookResponse> findBookById(
        @PathVariable("Book_id") Integer bookId
        ){
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    @GetMapping("path")
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks (
        @RequestParam(name = "page",defaultValue = "0",required = false) int page,
        @RequestParam(name = "size",defaultValue = "10",required = false) int size,
        Authentication connectedUser        
        ) {
        return ResponseEntity.ok(bookService.findAllBooks(page,size,connectedUser));

    }
    



}
