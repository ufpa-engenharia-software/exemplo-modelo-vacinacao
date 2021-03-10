package br.ufpa.castanhal.es2.vacinacao.service;

import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.VacinacaoPessoa}.
 */
public interface VacinacaoPessoaService {

    /**
     * Save a vacinacaoPessoa.
     *
     * @param vacinacaoPessoaDTO the entity to save.
     * @return the persisted entity.
     */
    VacinacaoPessoaDTO save(VacinacaoPessoaDTO vacinacaoPessoaDTO);

    /**
     * Get all the vacinacaoPessoas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VacinacaoPessoaDTO> findAll(Pageable pageable);


    /**
     * Get the "id" vacinacaoPessoa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VacinacaoPessoaDTO> findOne(Long id);

    /**
     * Delete the "id" vacinacaoPessoa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
