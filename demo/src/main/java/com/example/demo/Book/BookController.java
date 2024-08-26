package com.example.demo.Book;

import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;

import com.example.demo.Book.Request.BookRequest;
import com.example.demo.Book.Response.BookResponse;
import com.example.demo.Book.Response.BorrowedBookResponse;
import com.example.demo.common.PageResponse;

import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PutMapping;



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

    @GetMapping("/books")
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks (
        @RequestParam(name = "page",defaultValue = "0",required = false) int page,
        @RequestParam(name = "size",defaultValue = "10",required = false) int size,
        Authentication connectedUser        
        ) {
        return ResponseEntity.ok(bookService.findAllBooks(page,size,connectedUser));

    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> getUserBooks (
    @RequestParam(name = "page",defaultValue = "0",required = false) int page,
    @RequestParam(name = "size",defaultValue = "10",required = false) int size,
    Authentication connectedUser
        
        ){
        return ResponseEntity.ok(bookService.findUserBooks(page,size,connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getBorrowedBooks (
    @RequestParam(name = "page",defaultValue = "0",required = false) int page,
    @RequestParam(name = "size",defaultValue = "10",required = false) int size,
    Authentication connectedUser        
        ){
        return ResponseEntity.ok(bookService.getBorrowedBooks(page,size,connectedUser));
    }


    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllReturnedBooks (
    @RequestParam(name = "page",defaultValue = "0",required = false) int page,
    @RequestParam(name = "size",defaultValue = "10",required = false) int size,
    Authentication connectedUser        
        ){
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page,size,connectedUser));
    }

    @PatchMapping("/shareable/{book_id}")
    public ResponseEntity<Integer> updateShareable(
        @PathVariable Integer book_id, Authentication connectedUser
    ) {        
        return ResponseEntity.ok(bookService.updateShareableStatus(book_id,connectedUser));
    }

    @PatchMapping("/archived/{book_id}")
    public ResponseEntity<Integer> updateArchived(
        @PathVariable Integer book_id, Authentication connectedUser
    ) {        
        return ResponseEntity.ok(bookService.updateArchivedStatus(book_id,connectedUser));
    }


    @PostMapping("/borrow/{book_id}")
    public ResponseEntity<Integer> Borrow_a_Book(
        @PathVariable Integer book_id, Authentication connectedUser
    ) {        
        return ResponseEntity.ok(bookService.Borrow_a_Book(book_id,connectedUser));
    }

    @PatchMapping("return/borrowed/{book_id}")
    public ResponseEntity<Integer> return_a_Book(
        @PathVariable Integer book_id, Authentication connectedUser
    ) {        
        return ResponseEntity.ok(bookService.returnBook(book_id,connectedUser));
    }

    @PostMapping(value = "updateCover/{bookId}", consumes = "multipart/form-data")
    public ResponseEntity<?> postMethodName(@PathVariable("bookId") Integer book_id,
    @RequestPart("file") MultipartFile file,
    Authentication connectedUser
    ) {
        bookService.uploadBookCover(book_id,connectedUser,file);
        return null;
    }
    




}
