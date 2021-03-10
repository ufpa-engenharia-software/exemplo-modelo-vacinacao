package br.ufpa.castanhal.es2.vacinacao.service;

import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.Doenca}.
 */
public interface DoencaService {

    /**
     * Save a doenca.
     *
     * @param doencaDTO the entity to save.
     * @return the persisted entity.
     */
    DoencaDTO save(DoencaDTO doencaDTO);

    /**
     * Get all the doencas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoencaDTO> findAll(Pageable pageable);


    /**
     * Get the "id" doenca.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DoencaDTO> findOne(Long id);

    /**
     * Delete the "id" doenca.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
