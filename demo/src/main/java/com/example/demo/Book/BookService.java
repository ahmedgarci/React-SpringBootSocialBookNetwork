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

import com.example.demo.Book.Request.BookRequest;
import com.example.demo.Book.Response.BookResponse;
import com.example.demo.Book.Response.BorrowedBookResponse;
import com.example.demo.History.BookTransactionHistory;
import com.example.demo.History.BookTransactionHistoryRepository;
import com.example.demo.common.PageResponse;
import com.example.demo.file.FileStorageService;
import com.example.demo.user.UserEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public void save(BookRequest request, Authentication connectedUser) {
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        bookRepository.save(book);
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public ResponseEntity<PageResponse<BookResponse>> findUserBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllByUserId(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        PageResponse<BookResponse> pageResponse = new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
        return ResponseEntity.ok(pageResponse);
    }

    public PageResponse<BorrowedBookResponse> getBorrowedBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> borrowedBooks = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                borrowedBooks,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allReturnedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> borrowedBooks = allReturnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                borrowedBooks,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the id"));
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not the owner of this book");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the id"));
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not the owner of this book");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book does not exist"));
        if (book.isArchived() || !book.isShareable()) {
            throw new AccessDeniedException("Cannot be borrowed");
        }
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        if (book.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot borrow your own book");
        }
        boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isTheBookIsAlreadyBorrowedBy(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new AccessDeniedException("This book is already borrowed");
        }
        BookTransactionHistory transaction = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        bookTransactionHistoryRepository.save(transaction);
        return bookId;
    }

    public Integer returnBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the id"));
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not the owner of this book");
        }
        BookTransactionHistory bookHistory = bookTransactionHistoryRepository
                .findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        bookHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookHistory).getId();
    }

    public void uploadBookCover(Integer bookId, Authentication connectedUser, MultipartFile file) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the id"));
        UserEntity user = (UserEntity) connectedUser.getPrincipal();
        fileStorageService.saveFile(file, book, user.getId());
    }
}
