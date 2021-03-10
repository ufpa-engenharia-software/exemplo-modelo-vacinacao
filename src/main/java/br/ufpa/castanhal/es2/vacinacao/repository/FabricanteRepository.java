package br.ufpa.castanhal.es2.vacinacao.repository;

import br.ufpa.castanhal.es2.vacinacao.domain.Fabricante;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Fabricante entity.
 */
@Repository
public interface FabricanteRepository extends JpaRepository<Fabricante, Long>, JpaSpecificationExecutor<Fabricante> {

    @Query(value = "select distinct fabricante from Fabricante fabricante left join fetch fabricante.vacinas",
        countQuery = "select count(distinct fabricante) from Fabricante fabricante")
    Page<Fabricante> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct fabricante from Fabricante fabricante left join fetch fabricante.vacinas")
    List<Fabricante> findAllWithEagerRelationships();

    @Query("select fabricante from Fabricante fabricante left join fetch fabricante.vacinas where fabricante.id =:id")
    Optional<Fabricante> findOneWithEagerRelationships(@Param("id") Long id);
}
