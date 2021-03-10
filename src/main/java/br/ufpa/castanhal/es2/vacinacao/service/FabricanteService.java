package br.ufpa.castanhal.es2.vacinacao.service;

import br.ufpa.castanhal.es2.vacinacao.service.dto.FabricanteDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.Fabricante}.
 */
public interface FabricanteService {

    /**
     * Save a fabricante.
     *
     * @param fabricanteDTO the entity to save.
     * @return the persisted entity.
     */
    FabricanteDTO save(FabricanteDTO fabricanteDTO);

    /**
     * Get all the fabricantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FabricanteDTO> findAll(Pageable pageable);

    /**
     * Get all the fabricantes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<FabricanteDTO> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" fabricante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FabricanteDTO> findOne(Long id);

    /**
     * Delete the "id" fabricante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
