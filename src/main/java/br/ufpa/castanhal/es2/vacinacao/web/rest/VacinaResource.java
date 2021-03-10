package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.service.VacinaService;
import br.ufpa.castanhal.es2.vacinacao.web.rest.errors.BadRequestAlertException;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.VacinaQueryService;

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
 * REST controller for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.Vacina}.
 */
@RestController
@RequestMapping("/api")
public class VacinaResource {

    private final Logger log = LoggerFactory.getLogger(VacinaResource.class);

    private static final String ENTITY_NAME = "vacina";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VacinaService vacinaService;

    private final VacinaQueryService vacinaQueryService;

    public VacinaResource(VacinaService vacinaService, VacinaQueryService vacinaQueryService) {
        this.vacinaService = vacinaService;
        this.vacinaQueryService = vacinaQueryService;
    }

    /**
     * {@code POST  /vacinas} : Create a new vacina.
     *
     * @param vacinaDTO the vacinaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vacinaDTO, or with status {@code 400 (Bad Request)} if the vacina has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vacinas")
    public ResponseEntity<VacinaDTO> createVacina(@RequestBody VacinaDTO vacinaDTO) throws URISyntaxException {
        log.debug("REST request to save Vacina : {}", vacinaDTO);
        if (vacinaDTO.getId() != null) {
            throw new BadRequestAlertException("A new vacina cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VacinaDTO result = vacinaService.save(vacinaDTO);
        return ResponseEntity.created(new URI("/api/vacinas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vacinas} : Updates an existing vacina.
     *
     * @param vacinaDTO the vacinaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacinaDTO,
     * or with status {@code 400 (Bad Request)} if the vacinaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vacinaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vacinas")
    public ResponseEntity<VacinaDTO> updateVacina(@RequestBody VacinaDTO vacinaDTO) throws URISyntaxException {
        log.debug("REST request to update Vacina : {}", vacinaDTO);
        if (vacinaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VacinaDTO result = vacinaService.save(vacinaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vacinaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vacinas} : get all the vacinas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vacinas in body.
     */
    @GetMapping("/vacinas")
    public ResponseEntity<List<VacinaDTO>> getAllVacinas(VacinaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vacinas by criteria: {}", criteria);
        Page<VacinaDTO> page = vacinaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vacinas/count} : count all the vacinas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vacinas/count")
    public ResponseEntity<Long> countVacinas(VacinaCriteria criteria) {
        log.debug("REST request to count Vacinas by criteria: {}", criteria);
        return ResponseEntity.ok().body(vacinaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vacinas/:id} : get the "id" vacina.
     *
     * @param id the id of the vacinaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vacinaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vacinas/{id}")
    public ResponseEntity<VacinaDTO> getVacina(@PathVariable Long id) {
        log.debug("REST request to get Vacina : {}", id);
        Optional<VacinaDTO> vacinaDTO = vacinaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vacinaDTO);
    }

    /**
     * {@code DELETE  /vacinas/:id} : delete the "id" vacina.
     *
     * @param id the id of the vacinaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vacinas/{id}")
    public ResponseEntity<Void> deleteVacina(@PathVariable Long id) {
        log.debug("REST request to delete Vacina : {}", id);
        vacinaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
