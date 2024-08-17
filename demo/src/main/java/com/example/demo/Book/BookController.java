package com.example.demo.Book;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Book.Request.BookRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

}
