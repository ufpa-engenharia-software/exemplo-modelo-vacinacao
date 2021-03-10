package br.ufpa.castanhal.es2.vacinacao.service;

import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.Vacina}.
 */
public interface VacinaService {

    /**
     * Save a vacina.
     *
     * @param vacinaDTO the entity to save.
     * @return the persisted entity.
     */
    VacinaDTO save(VacinaDTO vacinaDTO);

    /**
     * Get all the vacinas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VacinaDTO> findAll(Pageable pageable);


    /**
     * Get the "id" vacina.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VacinaDTO> findOne(Long id);

    /**
     * Delete the "id" vacina.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
