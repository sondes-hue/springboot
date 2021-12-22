package com.vermeg.bookland.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vermeg.bookland.dtos.FactureDto;
import com.vermeg.bookland.entities.Commande;





@Repository
public interface CommandeRepository extends CrudRepository<Commande, Long>{
	
	@Query(value="SELECT b.title as title,b.price as price ,b.picture as picture ,l.commande_date as commande from book b, commande c, ligne_commande l where c.id=l.commande_id and l.book_id=b.id and c.id_user=?1 ",nativeQuery = true)
	List<FactureDto> findFacture(long id);
	@Query(value="SELECT b.title as title,b.price as price ,b.picture as picture ,l.commande_date as commande from book b, commande c, ligne_commande l where c.id=l.commande_id and l.book_id=b.id and c.id_user=?1 and l.commande_date=?1",nativeQuery = true)
	List<FactureDto> findFacturepdf(long id,Date d);
}
