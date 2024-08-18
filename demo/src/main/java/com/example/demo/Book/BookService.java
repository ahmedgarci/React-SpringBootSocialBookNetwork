package com.example.demo.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.Book.Request.BookRequest;
import com.example.demo.Book.Response.BookResponse;
import com.example.demo.common.PageResponse;
import com.example.demo.user.UserEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {

    private final BookMapper BookMapper;
    private final BookRepository bookRepository;
    public Object findById;

    public void Save(BookRequest request,Authentication connectedUser){
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Book book = BookMapper.toBook(request);
        book.setOwner(user);
        bookRepository.save(book);
    }


    public BookResponse findById(Integer BookId){
        return bookRepository.findById(BookId).map(BookMapper::toBookResponse)
        .orElseThrow(()-> new EntityNotFoundException("entity not found"));
    
    }


    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size,Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable,user.getId());
        List<BookResponse> bookResponses = books.stream().map(BookMapper::toBookResponse)
        .toList();
        return PageResponse.<BookResponse>builder()
        .content(bookResponses)
        .number(books.getNumber())
        .size(books.getSize())
        .totalElements(books.getTotalElements())  
        .totalPages(books.getTotalPages())
        .first(books.isFirst())
        .last(books.isLast())
        .build();

    }




}
