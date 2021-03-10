package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.service.FabricanteService;
import br.ufpa.castanhal.es2.vacinacao.web.rest.errors.BadRequestAlertException;
import br.ufpa.castanhal.es2.vacinacao.service.dto.FabricanteDTO;
import br.ufpa.castanhal.es2.vacinacao.service.dto.FabricanteCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.FabricanteQueryService;

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
 * REST controller for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.Fabricante}.
 */
@RestController
@RequestMapping("/api")
public class FabricanteResource {

    private final Logger log = LoggerFactory.getLogger(FabricanteResource.class);

    private static final String ENTITY_NAME = "fabricante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FabricanteService fabricanteService;

    private final FabricanteQueryService fabricanteQueryService;

    public FabricanteResource(FabricanteService fabricanteService, FabricanteQueryService fabricanteQueryService) {
        this.fabricanteService = fabricanteService;
        this.fabricanteQueryService = fabricanteQueryService;
    }

    /**
     * {@code POST  /fabricantes} : Create a new fabricante.
     *
     * @param fabricanteDTO the fabricanteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fabricanteDTO, or with status {@code 400 (Bad Request)} if the fabricante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fabricantes")
    public ResponseEntity<FabricanteDTO> createFabricante(@RequestBody FabricanteDTO fabricanteDTO) throws URISyntaxException {
        log.debug("REST request to save Fabricante : {}", fabricanteDTO);
        if (fabricanteDTO.getId() != null) {
            throw new BadRequestAlertException("A new fabricante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FabricanteDTO result = fabricanteService.save(fabricanteDTO);
        return ResponseEntity.created(new URI("/api/fabricantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fabricantes} : Updates an existing fabricante.
     *
     * @param fabricanteDTO the fabricanteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fabricanteDTO,
     * or with status {@code 400 (Bad Request)} if the fabricanteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fabricanteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fabricantes")
    public ResponseEntity<FabricanteDTO> updateFabricante(@RequestBody FabricanteDTO fabricanteDTO) throws URISyntaxException {
        log.debug("REST request to update Fabricante : {}", fabricanteDTO);
        if (fabricanteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FabricanteDTO result = fabricanteService.save(fabricanteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fabricanteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fabricantes} : get all the fabricantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fabricantes in body.
     */
    @GetMapping("/fabricantes")
    public ResponseEntity<List<FabricanteDTO>> getAllFabricantes(FabricanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Fabricantes by criteria: {}", criteria);
        Page<FabricanteDTO> page = fabricanteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fabricantes/count} : count all the fabricantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fabricantes/count")
    public ResponseEntity<Long> countFabricantes(FabricanteCriteria criteria) {
        log.debug("REST request to count Fabricantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fabricanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fabricantes/:id} : get the "id" fabricante.
     *
     * @param id the id of the fabricanteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fabricanteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fabricantes/{id}")
    public ResponseEntity<FabricanteDTO> getFabricante(@PathVariable Long id) {
        log.debug("REST request to get Fabricante : {}", id);
        Optional<FabricanteDTO> fabricanteDTO = fabricanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fabricanteDTO);
    }

    /**
     * {@code DELETE  /fabricantes/:id} : delete the "id" fabricante.
     *
     * @param id the id of the fabricanteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fabricantes/{id}")
    public ResponseEntity<Void> deleteFabricante(@PathVariable Long id) {
        log.debug("REST request to delete Fabricante : {}", id);
        fabricanteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
