package com.example.demo.Book;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.example.demo.Feedback.feedBack;
import com.example.demo.History.BookTransactionHistory;
import com.example.demo.common.BaseEntity;
import com.example.demo.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {
  
    private String title;
    private String authorName;
    private String isbn;

    private String bookCover;

    private String synopsis;
    private boolean archived;
    private boolean shareble;
    
    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @CreatedBy
    @Column(nullable = false , updatable = false)
    private Integer createdBy;
    @LastModifiedBy
    @Column(insertable = false )
    private Integer lastModifiedBy;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @OneToMany(mappedBy = "book")
    private List<feedBack> feedbacks; 

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

}
