package com.vermeg.bookland.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vermeg.bookland.entities.Book;





@Repository
public interface BookRepository extends CrudRepository<Book, Long>{

}
