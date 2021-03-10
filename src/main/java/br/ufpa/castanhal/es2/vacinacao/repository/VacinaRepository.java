package br.ufpa.castanhal.es2.vacinacao.repository;

import br.ufpa.castanhal.es2.vacinacao.domain.Vacina;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Vacina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Long>, JpaSpecificationExecutor<Vacina> {
}
