package br.ufpa.castanhal.es2.vacinacao.repository;

import br.ufpa.castanhal.es2.vacinacao.domain.VacinacaoPessoa;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the VacinacaoPessoa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VacinacaoPessoaRepository extends JpaRepository<VacinacaoPessoa, Long>, JpaSpecificationExecutor<VacinacaoPessoa> {
}
