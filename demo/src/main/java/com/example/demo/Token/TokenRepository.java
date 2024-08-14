package com.example.demo.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TokenRepository extends JpaRepository<tokenEntity,Integer> {
    Optional<tokenEntity> findByToken(String token);
}
