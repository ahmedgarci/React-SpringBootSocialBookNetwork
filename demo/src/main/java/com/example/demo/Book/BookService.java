package com.example.demo.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;

import com.example.demo.Book.Request.BookRequest;
import com.example.demo.Book.Response.BookResponse;
import com.example.demo.Book.Response.BorrowedBookResponse;
import com.example.demo.History.BookTransactionHistory;
import com.example.demo.History.BookTransactionHistoryRepository;
import com.example.demo.common.PageResponse;
import com.example.demo.file.FileStorageService;
import com.example.demo.user.UserEntity;

import io.jsonwebtoken.lang.Objects;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {

    private final BookMapper BookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookTransactionHistory bookTransactionHistory;
    private final FileStorageService fileStorageService;


    public void Save(BookRequest request, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Book book = BookMapper.toBook(request);
        book.setOwner(user);
        bookRepository.save(book);
    }

    public BookResponse findById(Integer BookId) {
        return bookRepository.findById(BookId).map(BookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("entity not found"));

    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
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

    public ResponseEntity<PageResponse<BookResponse>> findUserBooks(
            int page, int size,
            Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll();
        return null;
    }

    public PageResponse<BorrowedBookResponse> getBorrowedBooks(
            int page, int size,
            Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable,
                user.getId());
        List<BorrowedBookResponse> BorrowedBooks = allBorrowedBooks.stream()
                .map(BookMapper::toBorrowedBookResponse).toList();

        return new PageResponse<>(
                BorrowedBooks,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast());

    }

        public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
                UserEntity user = ((UserEntity) connectedUser.getPrincipal());
                Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
                Page<BookTransactionHistory> allReturnedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable,
                        user.getId());
                List<BorrowedBookResponse> BorrowedBooks = allReturnedBooks.stream()
                        .map(BookMapper::toBorrowedBookResponse).toList();
        
                return new PageResponse<>(
                        BorrowedBooks,
                        allReturnedBooks.getNumber(),
                        allReturnedBooks.getSize(),
                        allReturnedBooks.getTotalElements(),
                        allReturnedBooks.getTotalPages(),
                        allReturnedBooks.isFirst(),
                        allReturnedBooks.isLast());

        }

        public Integer updateShareableStatus(Integer book_id, Authentication connectedUser) {
                Book book = bookRepository.findById(book_id)
                .orElseThrow(()->new EntityNotFoundException("no book found with the id"));
                UserEntity user = ((UserEntity)connectedUser.getPrincipal());
                if (!book.getOwner().getId().equals(user.getId())){
                        throw new AccessDeniedException("u are not the owner of this book");
                }
                book.setShareble(!book.isShareble());
                bookRepository.save(book);
                return book_id;
        }

        public Integer updateArchivedStatus(Integer book_id, Authentication connectedUser) {
                Book book = bookRepository.findById(book_id)
                .orElseThrow(()->new EntityNotFoundException("no book found with the id"));
                UserEntity user = ((UserEntity)connectedUser.getPrincipal());
                if (!book.getOwner().getId().equals(user.getId())){
                        throw new AccessDeniedException("u are not the owner of this book");
                }
                book.setShareble(!book.isArchived());
                bookRepository.save(book);
                return book_id;
        }

        public Integer Borrow_a_Book(Integer book_id, Authentication connectedUser){
                Book book = bookRepository.findById(book_id).orElseThrow(()-> new EntityNotFoundException("book does not exist"));
                if(book.isArchived() || !book.isShareble()){
                        throw new OperationNotPermittedException("the requested book cannot be borrowed coz its shareable or archived");
                }
                UserEntity user = ((UserEntity)connectedUser.getPrincipal());
                if (!book.getOwner().getId().equals(user.getId())){
                        throw new AccessDeniedException("u are not the owner of this book");
                }
                final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isTheBookIsAlreadyBorrowedBy(book_id,user.getId());
                if(isAlreadyBorrowed){
                        throw new OperationNotPermittedException("aa");
                }
                BookTransactionHistory transaction = bookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
                bookTransactionHistoryRepository.save(transaction);
                return book_id;
        }

        public Integer returnBook(Integer book_id, Authentication connectedUser) {
                Book book = bookRepository.findById(book_id)
                .orElseThrow(()->new EntityNotFoundException("no book found with the id"));
                UserEntity user = ((UserEntity)connectedUser.getPrincipal());
                if (!book.getOwner().getId().equals(user.getId())){
                        throw new AccessDeniedException("u are not the owner of this book");
                }
                BookTransactionHistory bookHistory = bookTransactionHistoryRepository
                .findByBookIdAndUserId(book_id,user.getId())
                .orElseThrow(()->new EntityNotFoundException("transaction not found"));
                bookHistory.setReturned(true);
                return bookTransactionHistoryRepository.save(bookHistory).getId();
        }

        public void uploadBookCover(Integer book_id, Authentication connectedUser, MultipartFile file) {
                Book book = bookRepository.findById(book_id)
                .orElseThrow(()->new EntityNotFoundException("no book found with the id"));
                UserEntity user = ((UserEntity)connectedUser.getPrincipal());
                var bookCover = fileStorageService.saveFile(file,book,user.getId());
        }






}
