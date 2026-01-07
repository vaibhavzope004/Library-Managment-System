package com.Library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.Library.entity.Book;
import com.Library.entity.BookTransaction;
import com.Library.repository.BookTransactionRepository;

@Service
public class BookTransactionService {    

    private final BookTransactionRepository bookTransactionRepository;
    private final BookService bookService;
    
    public BookTransactionService(BookTransactionRepository repo, BookService bookService) {
        this.bookTransactionRepository = repo;
        this.bookService = bookService;
    }

    // Issue Book to Student    
    public boolean issueBook(Long bookId, String issuedTo) {
        Optional<Book> bookOpt = bookService.getBookById(bookId);

        if (bookOpt.isEmpty() || bookOpt.get().getQuantity() <= 0) {
            return false;
        }

        Book book = bookOpt.get();
        book.setQuantity(book.getQuantity() - 1);
        bookService.saveBook(book);

        BookTransaction transaction = new BookTransaction();
        transaction.setBook(book);
        transaction.setIssuedTo(issuedTo);
        transaction.setIssueDate(LocalDate.now());
        transaction.setReturnDate(null);

        bookTransactionRepository.save(transaction);
        return true;
    }
    
    // Return Book    
    public boolean returnBook(Long transactionId) {
        Optional<BookTransaction> transactionOpt = bookTransactionRepository.findById(transactionId); 
        if (transactionOpt.isEmpty()) return false;

        BookTransaction tx = transactionOpt.get();       
        tx.setReturnDate(LocalDate.now());
        bookTransactionRepository.save(tx);

        Book book = tx.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookService.saveBook(book);

        return true;               
    }

    // Transactions    
    public List<BookTransaction> getAllTransactions() {
        return bookTransactionRepository.findAll();
    }

    // Delete All Transactions
    public void deleteAll() {
        bookTransactionRepository.deleteAll();
    }

    // Check if any transaction is unreturned
    public boolean hasUnreturnedTransactions() {
        return getAllTransactions().stream().anyMatch(t -> t.getReturnDate() == null);
    }
}
