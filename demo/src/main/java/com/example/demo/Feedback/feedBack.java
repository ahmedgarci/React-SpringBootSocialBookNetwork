package com.example.demo.Feedback;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.Book.Book;
import com.example.demo.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class feedBack extends BaseEntity{
   
    private Double note;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
