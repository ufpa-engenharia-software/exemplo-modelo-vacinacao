package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.service.PaisService;
import br.ufpa.castanhal.es2.vacinacao.web.rest.errors.BadRequestAlertException;
import br.ufpa.castanhal.es2.vacinacao.service.dto.PaisDTO;
import br.ufpa.castanhal.es2.vacinacao.service.dto.PaisCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.PaisQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.Pais}.
 */
@RestController
@RequestMapping("/api")
public class PaisResource {

    private final Logger log = LoggerFactory.getLogger(PaisResource.class);

    private static final String ENTITY_NAME = "pais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaisService paisService;

    private final PaisQueryService paisQueryService;

    public PaisResource(PaisService paisService, PaisQueryService paisQueryService) {
        this.paisService = paisService;
        this.paisQueryService = paisQueryService;
    }

    /**
     * {@code POST  /pais} : Create a new pais.
     *
     * @param paisDTO the paisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paisDTO, or with status {@code 400 (Bad Request)} if the pais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pais")
    public ResponseEntity<PaisDTO> createPais(@RequestBody PaisDTO paisDTO) throws URISyntaxException {
        log.debug("REST request to save Pais : {}", paisDTO);
        if (paisDTO.getId() != null) {
            throw new BadRequestAlertException("A new pais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaisDTO result = paisService.save(paisDTO);
        return ResponseEntity.created(new URI("/api/pais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pais} : Updates an existing pais.
     *
     * @param paisDTO the paisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paisDTO,
     * or with status {@code 400 (Bad Request)} if the paisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pais")
    public ResponseEntity<PaisDTO> updatePais(@RequestBody PaisDTO paisDTO) throws URISyntaxException {
        log.debug("REST request to update Pais : {}", paisDTO);
        if (paisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaisDTO result = paisService.save(paisDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pais} : get all the pais.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pais in body.
     */
    @GetMapping("/pais")
    public ResponseEntity<List<PaisDTO>> getAllPais(PaisCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pais by criteria: {}", criteria);
        Page<PaisDTO> page = paisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pais/count} : count all the pais.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pais/count")
    public ResponseEntity<Long> countPais(PaisCriteria criteria) {
        log.debug("REST request to count Pais by criteria: {}", criteria);
        return ResponseEntity.ok().body(paisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pais/:id} : get the "id" pais.
     *
     * @param id the id of the paisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pais/{id}")
    public ResponseEntity<PaisDTO> getPais(@PathVariable Long id) {
        log.debug("REST request to get Pais : {}", id);
        Optional<PaisDTO> paisDTO = paisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paisDTO);
    }

    /**
     * {@code DELETE  /pais/:id} : delete the "id" pais.
     *
     * @param id the id of the paisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pais/{id}")
    public ResponseEntity<Void> deletePais(@PathVariable Long id) {
        log.debug("REST request to delete Pais : {}", id);
        paisService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
