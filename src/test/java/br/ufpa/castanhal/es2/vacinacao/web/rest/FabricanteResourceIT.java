package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.ExemploVacinacaoApp;
import br.ufpa.castanhal.es2.vacinacao.domain.Fabricante;
import br.ufpa.castanhal.es2.vacinacao.domain.Pais;
import br.ufpa.castanhal.es2.vacinacao.domain.Vacina;
import br.ufpa.castanhal.es2.vacinacao.repository.FabricanteRepository;
import br.ufpa.castanhal.es2.vacinacao.service.FabricanteService;
import br.ufpa.castanhal.es2.vacinacao.service.dto.FabricanteDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.FabricanteMapper;
import br.ufpa.castanhal.es2.vacinacao.service.dto.FabricanteCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.FabricanteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static br.ufpa.castanhal.es2.vacinacao.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FabricanteResource} REST controller.
 */
@SpringBootTest(classes = ExemploVacinacaoApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FabricanteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private FabricanteRepository fabricanteRepository;

    @Mock
    private FabricanteRepository fabricanteRepositoryMock;

    @Autowired
    private FabricanteMapper fabricanteMapper;

    @Mock
    private FabricanteService fabricanteServiceMock;

    @Autowired
    private FabricanteService fabricanteService;

    @Autowired
    private FabricanteQueryService fabricanteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFabricanteMockMvc;

    private Fabricante fabricante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fabricante createEntity(EntityManager em) {
        Fabricante fabricante = new Fabricante()
            .nome(DEFAULT_NOME)
            .criado(DEFAULT_CRIADO);
        return fabricante;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fabricante createUpdatedEntity(EntityManager em) {
        Fabricante fabricante = new Fabricante()
            .nome(UPDATED_NOME)
            .criado(UPDATED_CRIADO);
        return fabricante;
    }

    @BeforeEach
    public void initTest() {
        fabricante = createEntity(em);
    }

    @Test
    @Transactional
    public void createFabricante() throws Exception {
        int databaseSizeBeforeCreate = fabricanteRepository.findAll().size();
        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);
        restFabricanteMockMvc.perform(post("/api/fabricantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO)))
            .andExpect(status().isCreated());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeCreate + 1);
        Fabricante testFabricante = fabricanteList.get(fabricanteList.size() - 1);
        assertThat(testFabricante.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFabricante.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    public void createFabricanteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fabricanteRepository.findAll().size();

        // Create the Fabricante with an existing ID
        fabricante.setId(1L);
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFabricanteMockMvc.perform(post("/api/fabricantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFabricantes() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList
        restFabricanteMockMvc.perform(get("/api/fabricantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllFabricantesWithEagerRelationshipsIsEnabled() throws Exception {
        when(fabricanteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFabricanteMockMvc.perform(get("/api/fabricantes?eagerload=true"))
            .andExpect(status().isOk());

        verify(fabricanteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllFabricantesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fabricanteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFabricanteMockMvc.perform(get("/api/fabricantes?eagerload=true"))
            .andExpect(status().isOk());

        verify(fabricanteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFabricante() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get the fabricante
        restFabricanteMockMvc.perform(get("/api/fabricantes/{id}", fabricante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fabricante.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }


    @Test
    @Transactional
    public void getFabricantesByIdFiltering() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        Long id = fabricante.getId();

        defaultFabricanteShouldBeFound("id.equals=" + id);
        defaultFabricanteShouldNotBeFound("id.notEquals=" + id);

        defaultFabricanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFabricanteShouldNotBeFound("id.greaterThan=" + id);

        defaultFabricanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFabricanteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFabricantesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome equals to DEFAULT_NOME
        defaultFabricanteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the fabricanteList where nome equals to UPDATED_NOME
        defaultFabricanteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllFabricantesByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome not equals to DEFAULT_NOME
        defaultFabricanteShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the fabricanteList where nome not equals to UPDATED_NOME
        defaultFabricanteShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllFabricantesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultFabricanteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the fabricanteList where nome equals to UPDATED_NOME
        defaultFabricanteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllFabricantesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome is not null
        defaultFabricanteShouldBeFound("nome.specified=true");

        // Get all the fabricanteList where nome is null
        defaultFabricanteShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllFabricantesByNomeContainsSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome contains DEFAULT_NOME
        defaultFabricanteShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the fabricanteList where nome contains UPDATED_NOME
        defaultFabricanteShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllFabricantesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome does not contain DEFAULT_NOME
        defaultFabricanteShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the fabricanteList where nome does not contain UPDATED_NOME
        defaultFabricanteShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllFabricantesByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado equals to DEFAULT_CRIADO
        defaultFabricanteShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado equals to UPDATED_CRIADO
        defaultFabricanteShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllFabricantesByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado not equals to DEFAULT_CRIADO
        defaultFabricanteShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado not equals to UPDATED_CRIADO
        defaultFabricanteShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllFabricantesByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultFabricanteShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the fabricanteList where criado equals to UPDATED_CRIADO
        defaultFabricanteShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllFabricantesByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is not null
        defaultFabricanteShouldBeFound("criado.specified=true");

        // Get all the fabricanteList where criado is null
        defaultFabricanteShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    public void getAllFabricantesByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is greater than or equal to DEFAULT_CRIADO
        defaultFabricanteShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado is greater than or equal to UPDATED_CRIADO
        defaultFabricanteShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllFabricantesByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is less than or equal to DEFAULT_CRIADO
        defaultFabricanteShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado is less than or equal to SMALLER_CRIADO
        defaultFabricanteShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    public void getAllFabricantesByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is less than DEFAULT_CRIADO
        defaultFabricanteShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado is less than UPDATED_CRIADO
        defaultFabricanteShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllFabricantesByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is greater than DEFAULT_CRIADO
        defaultFabricanteShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado is greater than SMALLER_CRIADO
        defaultFabricanteShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }


    @Test
    @Transactional
    public void getAllFabricantesByPaisIsEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);
        Pais pais = PaisResourceIT.createEntity(em);
        em.persist(pais);
        em.flush();
        fabricante.setPais(pais);
        fabricanteRepository.saveAndFlush(fabricante);
        Long paisId = pais.getId();

        // Get all the fabricanteList where pais equals to paisId
        defaultFabricanteShouldBeFound("paisId.equals=" + paisId);

        // Get all the fabricanteList where pais equals to paisId + 1
        defaultFabricanteShouldNotBeFound("paisId.equals=" + (paisId + 1));
    }


    @Test
    @Transactional
    public void getAllFabricantesByVacinasIsEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);
        Vacina vacinas = VacinaResourceIT.createEntity(em);
        em.persist(vacinas);
        em.flush();
        fabricante.addVacinas(vacinas);
        fabricanteRepository.saveAndFlush(fabricante);
        Long vacinasId = vacinas.getId();

        // Get all the fabricanteList where vacinas equals to vacinasId
        defaultFabricanteShouldBeFound("vacinasId.equals=" + vacinasId);

        // Get all the fabricanteList where vacinas equals to vacinasId + 1
        defaultFabricanteShouldNotBeFound("vacinasId.equals=" + (vacinasId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFabricanteShouldBeFound(String filter) throws Exception {
        restFabricanteMockMvc.perform(get("/api/fabricantes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restFabricanteMockMvc.perform(get("/api/fabricantes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFabricanteShouldNotBeFound(String filter) throws Exception {
        restFabricanteMockMvc.perform(get("/api/fabricantes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFabricanteMockMvc.perform(get("/api/fabricantes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFabricante() throws Exception {
        // Get the fabricante
        restFabricanteMockMvc.perform(get("/api/fabricantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFabricante() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();

        // Update the fabricante
        Fabricante updatedFabricante = fabricanteRepository.findById(fabricante.getId()).get();
        // Disconnect from session so that the updates on updatedFabricante are not directly saved in db
        em.detach(updatedFabricante);
        updatedFabricante
            .nome(UPDATED_NOME)
            .criado(UPDATED_CRIADO);
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(updatedFabricante);

        restFabricanteMockMvc.perform(put("/api/fabricantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO)))
            .andExpect(status().isOk());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
        Fabricante testFabricante = fabricanteList.get(fabricanteList.size() - 1);
        assertThat(testFabricante.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFabricante.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void updateNonExistingFabricante() throws Exception {
        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();

        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFabricanteMockMvc.perform(put("/api/fabricantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFabricante() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        int databaseSizeBeforeDelete = fabricanteRepository.findAll().size();

        // Delete the fabricante
        restFabricanteMockMvc.perform(delete("/api/fabricantes/{id}", fabricante.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
