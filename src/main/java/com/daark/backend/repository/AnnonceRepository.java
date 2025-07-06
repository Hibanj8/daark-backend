package com.daark.backend.repository;

import com.daark.backend.entity.Annonce;
import com.daark.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.daark.backend.entity.StatutAnnonce;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
    List<Annonce> findByStatut(StatutAnnonce statut);
    @Query("SELECT a FROM Annonce a WHERE " +
            "(:ville IS NULL OR a.ville = :ville) AND " +
            "(:typeLocation IS NULL OR a.typeLocation = :typeLocation) AND " +
            "(:typeLogement IS NULL OR a.typeLogement = :typeLogement) AND " +
            "(:minPrix IS NULL OR a.prix >= :minPrix) AND " +
            "(:maxPrix IS NULL OR a.prix <= :maxPrix) AND " +
            "a.statut = 'ACCEPTEE'")
    List<Annonce> searchByFilters(
            @Param("ville") String ville,
            @Param("typeLocation") String typeLocation,
            @Param("typeLogement") String typeLogement,
            @Param("minPrix") Double minPrix,
            @Param("maxPrix") Double maxPrix
    );

    List<Annonce> findByUser(User user);

}
