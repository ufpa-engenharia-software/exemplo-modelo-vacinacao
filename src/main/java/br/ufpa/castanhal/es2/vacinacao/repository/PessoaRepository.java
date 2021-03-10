package br.ufpa.castanhal.es2.vacinacao.repository;

import br.ufpa.castanhal.es2.vacinacao.domain.Pessoa;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Pessoa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>, JpaSpecificationExecutor<Pessoa> {
}
