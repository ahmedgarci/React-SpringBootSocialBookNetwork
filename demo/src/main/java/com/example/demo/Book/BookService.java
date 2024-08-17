package com.example.demo.Book;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.Book.Request.BookRequest;
import com.example.demo.user.UserEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService {

    private final BookMapper BookMapper;
    private final BookRepository bookRepository;

    public void Save(BookRequest request,Authentication connectedUser){
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Book book = BookMapper.toBook(request);
        book.setOwner(user);
        bookRepository.save(book);
    }


}
