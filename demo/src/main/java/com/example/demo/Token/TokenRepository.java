package com.example.demo.Token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<tokenEntity,Integer> {
    
}
