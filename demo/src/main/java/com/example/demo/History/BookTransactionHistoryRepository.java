package com.example.demo.History;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer>{
    @Query("""
            SELECT history FROM BookTransactionHistory history WHERE 
            history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
            SELECT history FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);
        
    @Query("""
        SELECT (count(history)> 0 ) 
        FROM BookTransactionHistory history
        WHERE history.user.id = :userId and history.book.id = :bookId
        and history.returnApproved = false
        """)
boolean isTheBookIsAlreadyBorrowedBy(Integer bookId,Integer userId);

        @Query("""
                SELECT transaction FROM BookTransactionHistory transaction
                WHERE transaction.user.id = :userId and transaction.book.id = :book_id
                and transaction.returned = false and transaction.returnApproved = false
                        """)
        Optional<BookTransactionHistory> findByBookIdAndUserId(Integer book_id, Integer userId);


}
