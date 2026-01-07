package com.Library.service;
import com.Library.entity.Book;
import com.Library.repository.BookRepository;
import com.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService 
{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book saveBook(Book book) 
    {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks()
    {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) 
    {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteBook(Long id) 
    {        
            bookRepository.deleteById(id);
        
    }
    
    
    
}
