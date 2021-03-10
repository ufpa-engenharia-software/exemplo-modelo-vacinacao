package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.service.DoencaService;
import br.ufpa.castanhal.es2.vacinacao.web.rest.errors.BadRequestAlertException;
import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.DoencaQueryService;

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
 * REST controller for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.Doenca}.
 */
@RestController
@RequestMapping("/api")
public class DoencaResource {

    private final Logger log = LoggerFactory.getLogger(DoencaResource.class);

    private static final String ENTITY_NAME = "doenca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoencaService doencaService;

    private final DoencaQueryService doencaQueryService;

    public DoencaResource(DoencaService doencaService, DoencaQueryService doencaQueryService) {
        this.doencaService = doencaService;
        this.doencaQueryService = doencaQueryService;
    }

    /**
     * {@code POST  /doencas} : Create a new doenca.
     *
     * @param doencaDTO the doencaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doencaDTO, or with status {@code 400 (Bad Request)} if the doenca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doencas")
    public ResponseEntity<DoencaDTO> createDoenca(@RequestBody DoencaDTO doencaDTO) throws URISyntaxException {
        log.debug("REST request to save Doenca : {}", doencaDTO);
        if (doencaDTO.getId() != null) {
            throw new BadRequestAlertException("A new doenca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoencaDTO result = doencaService.save(doencaDTO);
        return ResponseEntity.created(new URI("/api/doencas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doencas} : Updates an existing doenca.
     *
     * @param doencaDTO the doencaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doencaDTO,
     * or with status {@code 400 (Bad Request)} if the doencaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doencaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doencas")
    public ResponseEntity<DoencaDTO> updateDoenca(@RequestBody DoencaDTO doencaDTO) throws URISyntaxException {
        log.debug("REST request to update Doenca : {}", doencaDTO);
        if (doencaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoencaDTO result = doencaService.save(doencaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doencaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /doencas} : get all the doencas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doencas in body.
     */
    @GetMapping("/doencas")
    public ResponseEntity<List<DoencaDTO>> getAllDoencas(DoencaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Doencas by criteria: {}", criteria);
        Page<DoencaDTO> page = doencaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doencas/count} : count all the doencas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doencas/count")
    public ResponseEntity<Long> countDoencas(DoencaCriteria criteria) {
        log.debug("REST request to count Doencas by criteria: {}", criteria);
        return ResponseEntity.ok().body(doencaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doencas/:id} : get the "id" doenca.
     *
     * @param id the id of the doencaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doencaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doencas/{id}")
    public ResponseEntity<DoencaDTO> getDoenca(@PathVariable Long id) {
        log.debug("REST request to get Doenca : {}", id);
        Optional<DoencaDTO> doencaDTO = doencaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doencaDTO);
    }

    /**
     * {@code DELETE  /doencas/:id} : delete the "id" doenca.
     *
     * @param id the id of the doencaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doencas/{id}")
    public ResponseEntity<Void> deleteDoenca(@PathVariable Long id) {
        log.debug("REST request to delete Doenca : {}", id);
        doencaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
