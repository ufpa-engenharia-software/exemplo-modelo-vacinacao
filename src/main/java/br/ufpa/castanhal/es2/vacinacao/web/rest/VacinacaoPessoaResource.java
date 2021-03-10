package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.service.VacinacaoPessoaService;
import br.ufpa.castanhal.es2.vacinacao.web.rest.errors.BadRequestAlertException;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.VacinacaoPessoaQueryService;

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
 * REST controller for managing {@link br.ufpa.castanhal.es2.vacinacao.domain.VacinacaoPessoa}.
 */
@RestController
@RequestMapping("/api")
public class VacinacaoPessoaResource {

    private final Logger log = LoggerFactory.getLogger(VacinacaoPessoaResource.class);

    private static final String ENTITY_NAME = "vacinacaoPessoa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VacinacaoPessoaService vacinacaoPessoaService;

    private final VacinacaoPessoaQueryService vacinacaoPessoaQueryService;

    public VacinacaoPessoaResource(VacinacaoPessoaService vacinacaoPessoaService, VacinacaoPessoaQueryService vacinacaoPessoaQueryService) {
        this.vacinacaoPessoaService = vacinacaoPessoaService;
        this.vacinacaoPessoaQueryService = vacinacaoPessoaQueryService;
    }

    /**
     * {@code POST  /vacinacao-pessoas} : Create a new vacinacaoPessoa.
     *
     * @param vacinacaoPessoaDTO the vacinacaoPessoaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vacinacaoPessoaDTO, or with status {@code 400 (Bad Request)} if the vacinacaoPessoa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vacinacao-pessoas")
    public ResponseEntity<VacinacaoPessoaDTO> createVacinacaoPessoa(@RequestBody VacinacaoPessoaDTO vacinacaoPessoaDTO) throws URISyntaxException {
        log.debug("REST request to save VacinacaoPessoa : {}", vacinacaoPessoaDTO);
        if (vacinacaoPessoaDTO.getId() != null) {
            throw new BadRequestAlertException("A new vacinacaoPessoa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VacinacaoPessoaDTO result = vacinacaoPessoaService.save(vacinacaoPessoaDTO);
        return ResponseEntity.created(new URI("/api/vacinacao-pessoas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vacinacao-pessoas} : Updates an existing vacinacaoPessoa.
     *
     * @param vacinacaoPessoaDTO the vacinacaoPessoaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacinacaoPessoaDTO,
     * or with status {@code 400 (Bad Request)} if the vacinacaoPessoaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vacinacaoPessoaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vacinacao-pessoas")
    public ResponseEntity<VacinacaoPessoaDTO> updateVacinacaoPessoa(@RequestBody VacinacaoPessoaDTO vacinacaoPessoaDTO) throws URISyntaxException {
        log.debug("REST request to update VacinacaoPessoa : {}", vacinacaoPessoaDTO);
        if (vacinacaoPessoaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VacinacaoPessoaDTO result = vacinacaoPessoaService.save(vacinacaoPessoaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vacinacaoPessoaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vacinacao-pessoas} : get all the vacinacaoPessoas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vacinacaoPessoas in body.
     */
    @GetMapping("/vacinacao-pessoas")
    public ResponseEntity<List<VacinacaoPessoaDTO>> getAllVacinacaoPessoas(VacinacaoPessoaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VacinacaoPessoas by criteria: {}", criteria);
        Page<VacinacaoPessoaDTO> page = vacinacaoPessoaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vacinacao-pessoas/count} : count all the vacinacaoPessoas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vacinacao-pessoas/count")
    public ResponseEntity<Long> countVacinacaoPessoas(VacinacaoPessoaCriteria criteria) {
        log.debug("REST request to count VacinacaoPessoas by criteria: {}", criteria);
        return ResponseEntity.ok().body(vacinacaoPessoaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vacinacao-pessoas/:id} : get the "id" vacinacaoPessoa.
     *
     * @param id the id of the vacinacaoPessoaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vacinacaoPessoaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vacinacao-pessoas/{id}")
    public ResponseEntity<VacinacaoPessoaDTO> getVacinacaoPessoa(@PathVariable Long id) {
        log.debug("REST request to get VacinacaoPessoa : {}", id);
        Optional<VacinacaoPessoaDTO> vacinacaoPessoaDTO = vacinacaoPessoaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vacinacaoPessoaDTO);
    }

    /**
     * {@code DELETE  /vacinacao-pessoas/:id} : delete the "id" vacinacaoPessoa.
     *
     * @param id the id of the vacinacaoPessoaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vacinacao-pessoas/{id}")
    public ResponseEntity<Void> deleteVacinacaoPessoa(@PathVariable Long id) {
        log.debug("REST request to delete VacinacaoPessoa : {}", id);
        vacinacaoPessoaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
