package com.vermeg.bookland.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vermeg.bookland.entities.Categorie;

@Repository
public interface CategorieRepository extends CrudRepository<Categorie, Long>{

}
