package com.Library.repository;
import com.Library.entity.BookTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long>
{
	
    
}
