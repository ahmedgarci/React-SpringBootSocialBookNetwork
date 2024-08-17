package com.example.demo.History;

import com.example.demo.Book.Book;
import com.example.demo.common.BaseEntity;
import com.example.demo.user.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean returned;
    private boolean returnApproved;
}
