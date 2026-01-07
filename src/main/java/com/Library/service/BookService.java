package com.Library.service;
import com.Library.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookService 
{
    Book saveBook(Book book);
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    void deleteBook(Long id);

}
