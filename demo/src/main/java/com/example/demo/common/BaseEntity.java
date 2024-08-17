package com.example.demo.common;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
}
