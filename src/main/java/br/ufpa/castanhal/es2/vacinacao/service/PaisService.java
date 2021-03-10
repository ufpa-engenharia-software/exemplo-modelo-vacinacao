package br.ufpa.castanhal.es2.vacinacao.service;

import br.ufpa.castanhal.es2.vacinacao.service.dto.PaisDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.Pais}.
 */
public interface PaisService {

    /**
     * Save a pais.
     *
     * @param paisDTO the entity to save.
     * @return the persisted entity.
     */
    PaisDTO save(PaisDTO paisDTO);

    /**
     * Get all the pais.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaisDTO> findAll(Pageable pageable);


    /**
     * Get the "id" pais.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaisDTO> findOne(Long id);

    /**
     * Delete the "id" pais.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
