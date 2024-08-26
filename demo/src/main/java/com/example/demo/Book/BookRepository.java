package com.example.demo.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book,Integer> {

    @Query("""
        SELECT book From Book where 
        book.archived = false and book.shareable = true and book.owner.id != :userId   
            """
    )
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);

 


}
