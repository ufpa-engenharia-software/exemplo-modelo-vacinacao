package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.ExemploVacinacaoApp;
import br.ufpa.castanhal.es2.vacinacao.domain.Pais;
import br.ufpa.castanhal.es2.vacinacao.repository.PaisRepository;
import br.ufpa.castanhal.es2.vacinacao.service.PaisService;
import br.ufpa.castanhal.es2.vacinacao.service.dto.PaisDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.PaisMapper;
import br.ufpa.castanhal.es2.vacinacao.service.dto.PaisCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.PaisQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PaisResource} REST controller.
 */
@SpringBootTest(classes = ExemploVacinacaoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaisResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private PaisMapper paisMapper;

    @Autowired
    private PaisService paisService;

    @Autowired
    private PaisQueryService paisQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaisMockMvc;

    private Pais pais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createEntity(EntityManager em) {
        Pais pais = new Pais()
            .nome(DEFAULT_NOME)
            .sigla(DEFAULT_SIGLA);
        return pais;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createUpdatedEntity(EntityManager em) {
        Pais pais = new Pais()
            .nome(UPDATED_NOME)
            .sigla(UPDATED_SIGLA);
        return pais;
    }

    @BeforeEach
    public void initTest() {
        pais = createEntity(em);
    }

    @Test
    @Transactional
    public void createPais() throws Exception {
        int databaseSizeBeforeCreate = paisRepository.findAll().size();
        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);
        restPaisMockMvc.perform(post("/api/pais")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paisDTO)))
            .andExpect(status().isCreated());

        // Validate the Pais in the database
        List<Pais> paisList = paisRepository.findAll();
        assertThat(paisList).hasSize(databaseSizeBeforeCreate + 1);
        Pais testPais = paisList.get(paisList.size() - 1);
        assertThat(testPais.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPais.getSigla()).isEqualTo(DEFAULT_SIGLA);
    }

    @Test
    @Transactional
    public void createPaisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paisRepository.findAll().size();

        // Create the Pais with an existing ID
        pais.setId(1L);
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaisMockMvc.perform(post("/api/pais")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        List<Pais> paisList = paisRepository.findAll();
        assertThat(paisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList
        restPaisMockMvc.perform(get("/api/pais?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)));
    }
    
    @Test
    @Transactional
    public void getPais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get the pais
        restPaisMockMvc.perform(get("/api/pais/{id}", pais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pais.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA));
    }


    @Test
    @Transactional
    public void getPaisByIdFiltering() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        Long id = pais.getId();

        defaultPaisShouldBeFound("id.equals=" + id);
        defaultPaisShouldNotBeFound("id.notEquals=" + id);

        defaultPaisShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaisShouldNotBeFound("id.greaterThan=" + id);

        defaultPaisShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaisShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaisByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where nome equals to DEFAULT_NOME
        defaultPaisShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the paisList where nome equals to UPDATED_NOME
        defaultPaisShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllPaisByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where nome not equals to DEFAULT_NOME
        defaultPaisShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the paisList where nome not equals to UPDATED_NOME
        defaultPaisShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllPaisByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultPaisShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the paisList where nome equals to UPDATED_NOME
        defaultPaisShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllPaisByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where nome is not null
        defaultPaisShouldBeFound("nome.specified=true");

        // Get all the paisList where nome is null
        defaultPaisShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaisByNomeContainsSomething() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where nome contains DEFAULT_NOME
        defaultPaisShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the paisList where nome contains UPDATED_NOME
        defaultPaisShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllPaisByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where nome does not contain DEFAULT_NOME
        defaultPaisShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the paisList where nome does not contain UPDATED_NOME
        defaultPaisShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllPaisBySiglaIsEqualToSomething() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where sigla equals to DEFAULT_SIGLA
        defaultPaisShouldBeFound("sigla.equals=" + DEFAULT_SIGLA);

        // Get all the paisList where sigla equals to UPDATED_SIGLA
        defaultPaisShouldNotBeFound("sigla.equals=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllPaisBySiglaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where sigla not equals to DEFAULT_SIGLA
        defaultPaisShouldNotBeFound("sigla.notEquals=" + DEFAULT_SIGLA);

        // Get all the paisList where sigla not equals to UPDATED_SIGLA
        defaultPaisShouldBeFound("sigla.notEquals=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllPaisBySiglaIsInShouldWork() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where sigla in DEFAULT_SIGLA or UPDATED_SIGLA
        defaultPaisShouldBeFound("sigla.in=" + DEFAULT_SIGLA + "," + UPDATED_SIGLA);

        // Get all the paisList where sigla equals to UPDATED_SIGLA
        defaultPaisShouldNotBeFound("sigla.in=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllPaisBySiglaIsNullOrNotNull() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where sigla is not null
        defaultPaisShouldBeFound("sigla.specified=true");

        // Get all the paisList where sigla is null
        defaultPaisShouldNotBeFound("sigla.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaisBySiglaContainsSomething() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where sigla contains DEFAULT_SIGLA
        defaultPaisShouldBeFound("sigla.contains=" + DEFAULT_SIGLA);

        // Get all the paisList where sigla contains UPDATED_SIGLA
        defaultPaisShouldNotBeFound("sigla.contains=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllPaisBySiglaNotContainsSomething() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList where sigla does not contain DEFAULT_SIGLA
        defaultPaisShouldNotBeFound("sigla.doesNotContain=" + DEFAULT_SIGLA);

        // Get all the paisList where sigla does not contain UPDATED_SIGLA
        defaultPaisShouldBeFound("sigla.doesNotContain=" + UPDATED_SIGLA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaisShouldBeFound(String filter) throws Exception {
        restPaisMockMvc.perform(get("/api/pais?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)));

        // Check, that the count call also returns 1
        restPaisMockMvc.perform(get("/api/pais/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaisShouldNotBeFound(String filter) throws Exception {
        restPaisMockMvc.perform(get("/api/pais?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaisMockMvc.perform(get("/api/pais/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPais() throws Exception {
        // Get the pais
        restPaisMockMvc.perform(get("/api/pais/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        int databaseSizeBeforeUpdate = paisRepository.findAll().size();

        // Update the pais
        Pais updatedPais = paisRepository.findById(pais.getId()).get();
        // Disconnect from session so that the updates on updatedPais are not directly saved in db
        em.detach(updatedPais);
        updatedPais
            .nome(UPDATED_NOME)
            .sigla(UPDATED_SIGLA);
        PaisDTO paisDTO = paisMapper.toDto(updatedPais);

        restPaisMockMvc.perform(put("/api/pais")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paisDTO)))
            .andExpect(status().isOk());

        // Validate the Pais in the database
        List<Pais> paisList = paisRepository.findAll();
        assertThat(paisList).hasSize(databaseSizeBeforeUpdate);
        Pais testPais = paisList.get(paisList.size() - 1);
        assertThat(testPais.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPais.getSigla()).isEqualTo(UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void updateNonExistingPais() throws Exception {
        int databaseSizeBeforeUpdate = paisRepository.findAll().size();

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc.perform(put("/api/pais")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        List<Pais> paisList = paisRepository.findAll();
        assertThat(paisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        int databaseSizeBeforeDelete = paisRepository.findAll().size();

        // Delete the pais
        restPaisMockMvc.perform(delete("/api/pais/{id}", pais.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pais> paisList = paisRepository.findAll();
        assertThat(paisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
