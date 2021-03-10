package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.ExemploVacinacaoApp;
import br.ufpa.castanhal.es2.vacinacao.domain.Vacina;
import br.ufpa.castanhal.es2.vacinacao.domain.Doenca;
import br.ufpa.castanhal.es2.vacinacao.domain.Fabricante;
import br.ufpa.castanhal.es2.vacinacao.repository.VacinaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.VacinaService;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.VacinaMapper;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.VacinaQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static br.ufpa.castanhal.es2.vacinacao.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VacinaResource} REST controller.
 */
@SpringBootTest(classes = ExemploVacinacaoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VacinaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CRIADA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private VacinaMapper vacinaMapper;

    @Autowired
    private VacinaService vacinaService;

    @Autowired
    private VacinaQueryService vacinaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVacinaMockMvc;

    private Vacina vacina;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacina createEntity(EntityManager em) {
        Vacina vacina = new Vacina()
            .nome(DEFAULT_NOME)
            .criada(DEFAULT_CRIADA);
        return vacina;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacina createUpdatedEntity(EntityManager em) {
        Vacina vacina = new Vacina()
            .nome(UPDATED_NOME)
            .criada(UPDATED_CRIADA);
        return vacina;
    }

    @BeforeEach
    public void initTest() {
        vacina = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacina() throws Exception {
        int databaseSizeBeforeCreate = vacinaRepository.findAll().size();
        // Create the Vacina
        VacinaDTO vacinaDTO = vacinaMapper.toDto(vacina);
        restVacinaMockMvc.perform(post("/api/vacinas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacinaDTO)))
            .andExpect(status().isCreated());

        // Validate the Vacina in the database
        List<Vacina> vacinaList = vacinaRepository.findAll();
        assertThat(vacinaList).hasSize(databaseSizeBeforeCreate + 1);
        Vacina testVacina = vacinaList.get(vacinaList.size() - 1);
        assertThat(testVacina.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testVacina.getCriada()).isEqualTo(DEFAULT_CRIADA);
    }

    @Test
    @Transactional
    public void createVacinaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vacinaRepository.findAll().size();

        // Create the Vacina with an existing ID
        vacina.setId(1L);
        VacinaDTO vacinaDTO = vacinaMapper.toDto(vacina);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVacinaMockMvc.perform(post("/api/vacinas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacinaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vacina in the database
        List<Vacina> vacinaList = vacinaRepository.findAll();
        assertThat(vacinaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVacinas() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList
        restVacinaMockMvc.perform(get("/api/vacinas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacina.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criada").value(hasItem(sameInstant(DEFAULT_CRIADA))));
    }
    
    @Test
    @Transactional
    public void getVacina() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get the vacina
        restVacinaMockMvc.perform(get("/api/vacinas/{id}", vacina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vacina.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.criada").value(sameInstant(DEFAULT_CRIADA)));
    }


    @Test
    @Transactional
    public void getVacinasByIdFiltering() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        Long id = vacina.getId();

        defaultVacinaShouldBeFound("id.equals=" + id);
        defaultVacinaShouldNotBeFound("id.notEquals=" + id);

        defaultVacinaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVacinaShouldNotBeFound("id.greaterThan=" + id);

        defaultVacinaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVacinaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVacinasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where nome equals to DEFAULT_NOME
        defaultVacinaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the vacinaList where nome equals to UPDATED_NOME
        defaultVacinaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllVacinasByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where nome not equals to DEFAULT_NOME
        defaultVacinaShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the vacinaList where nome not equals to UPDATED_NOME
        defaultVacinaShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllVacinasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultVacinaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the vacinaList where nome equals to UPDATED_NOME
        defaultVacinaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllVacinasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where nome is not null
        defaultVacinaShouldBeFound("nome.specified=true");

        // Get all the vacinaList where nome is null
        defaultVacinaShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllVacinasByNomeContainsSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where nome contains DEFAULT_NOME
        defaultVacinaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the vacinaList where nome contains UPDATED_NOME
        defaultVacinaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllVacinasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where nome does not contain DEFAULT_NOME
        defaultVacinaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the vacinaList where nome does not contain UPDATED_NOME
        defaultVacinaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllVacinasByCriadaIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where criada equals to DEFAULT_CRIADA
        defaultVacinaShouldBeFound("criada.equals=" + DEFAULT_CRIADA);

        // Get all the vacinaList where criada equals to UPDATED_CRIADA
        defaultVacinaShouldNotBeFound("criada.equals=" + UPDATED_CRIADA);
    }

    @Test
    @Transactional
    public void getAllVacinasByCriadaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where criada not equals to DEFAULT_CRIADA
        defaultVacinaShouldNotBeFound("criada.notEquals=" + DEFAULT_CRIADA);

        // Get all the vacinaList where criada not equals to UPDATED_CRIADA
        defaultVacinaShouldBeFound("criada.notEquals=" + UPDATED_CRIADA);
    }

    @Test
    @Transactional
    public void getAllVacinasByCriadaIsInShouldWork() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where criada in DEFAULT_CRIADA or UPDATED_CRIADA
        defaultVacinaShouldBeFound("criada.in=" + DEFAULT_CRIADA + "," + UPDATED_CRIADA);

        // Get all the vacinaList where criada equals to UPDATED_CRIADA
        defaultVacinaShouldNotBeFound("criada.in=" + UPDATED_CRIADA);
    }

    @Test
    @Transactional
    public void getAllVacinasByCriadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where criada is not null
        defaultVacinaShouldBeFound("criada.specified=true");

        // Get all the vacinaList where criada is null
        defaultVacinaShouldNotBeFound("criada.specified=false");
    }

    @Test
    @Transactional
    public void getAllVacinasByCriadaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where criada is greater than or equal to DEFAULT_CRIADA
        defaultVacinaShouldBeFound("criada.greaterThanOrEqual=" + DEFAULT_CRIADA);

        // Get all the vacinaList where criada is greater than or equal to UPDATED_CRIADA
        defaultVacinaShouldNotBeFound("criada.greaterThanOrEqual=" + UPDATED_CRIADA);
    }

    @Test
    @Transactional
    public void getAllVacinasByCriadaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where criada is less than or equal to DEFAULT_CRIADA
        defaultVacinaShouldBeFound("criada.lessThanOrEqual=" + DEFAULT_CRIADA);

        // Get all the vacinaList where criada is less than or equal to SMALLER_CRIADA
        defaultVacinaShouldNotBeFound("criada.lessThanOrEqual=" + SMALLER_CRIADA);
    }

    @Test
    @Transactional
    public void getAllVacinasByCriadaIsLessThanSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where criada is less than DEFAULT_CRIADA
        defaultVacinaShouldNotBeFound("criada.lessThan=" + DEFAULT_CRIADA);

        // Get all the vacinaList where criada is less than UPDATED_CRIADA
        defaultVacinaShouldBeFound("criada.lessThan=" + UPDATED_CRIADA);
    }

    @Test
    @Transactional
    public void getAllVacinasByCriadaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        // Get all the vacinaList where criada is greater than DEFAULT_CRIADA
        defaultVacinaShouldNotBeFound("criada.greaterThan=" + DEFAULT_CRIADA);

        // Get all the vacinaList where criada is greater than SMALLER_CRIADA
        defaultVacinaShouldBeFound("criada.greaterThan=" + SMALLER_CRIADA);
    }


    @Test
    @Transactional
    public void getAllVacinasByDoencaIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);
        Doenca doenca = DoencaResourceIT.createEntity(em);
        em.persist(doenca);
        em.flush();
        vacina.setDoenca(doenca);
        vacinaRepository.saveAndFlush(vacina);
        Long doencaId = doenca.getId();

        // Get all the vacinaList where doenca equals to doencaId
        defaultVacinaShouldBeFound("doencaId.equals=" + doencaId);

        // Get all the vacinaList where doenca equals to doencaId + 1
        defaultVacinaShouldNotBeFound("doencaId.equals=" + (doencaId + 1));
    }


    @Test
    @Transactional
    public void getAllVacinasByFabricantesIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);
        Fabricante fabricantes = FabricanteResourceIT.createEntity(em);
        em.persist(fabricantes);
        em.flush();
        vacina.addFabricantes(fabricantes);
        vacinaRepository.saveAndFlush(vacina);
        Long fabricantesId = fabricantes.getId();

        // Get all the vacinaList where fabricantes equals to fabricantesId
        defaultVacinaShouldBeFound("fabricantesId.equals=" + fabricantesId);

        // Get all the vacinaList where fabricantes equals to fabricantesId + 1
        defaultVacinaShouldNotBeFound("fabricantesId.equals=" + (fabricantesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVacinaShouldBeFound(String filter) throws Exception {
        restVacinaMockMvc.perform(get("/api/vacinas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacina.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criada").value(hasItem(sameInstant(DEFAULT_CRIADA))));

        // Check, that the count call also returns 1
        restVacinaMockMvc.perform(get("/api/vacinas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVacinaShouldNotBeFound(String filter) throws Exception {
        restVacinaMockMvc.perform(get("/api/vacinas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVacinaMockMvc.perform(get("/api/vacinas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingVacina() throws Exception {
        // Get the vacina
        restVacinaMockMvc.perform(get("/api/vacinas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacina() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        int databaseSizeBeforeUpdate = vacinaRepository.findAll().size();

        // Update the vacina
        Vacina updatedVacina = vacinaRepository.findById(vacina.getId()).get();
        // Disconnect from session so that the updates on updatedVacina are not directly saved in db
        em.detach(updatedVacina);
        updatedVacina
            .nome(UPDATED_NOME)
            .criada(UPDATED_CRIADA);
        VacinaDTO vacinaDTO = vacinaMapper.toDto(updatedVacina);

        restVacinaMockMvc.perform(put("/api/vacinas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacinaDTO)))
            .andExpect(status().isOk());

        // Validate the Vacina in the database
        List<Vacina> vacinaList = vacinaRepository.findAll();
        assertThat(vacinaList).hasSize(databaseSizeBeforeUpdate);
        Vacina testVacina = vacinaList.get(vacinaList.size() - 1);
        assertThat(testVacina.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testVacina.getCriada()).isEqualTo(UPDATED_CRIADA);
    }

    @Test
    @Transactional
    public void updateNonExistingVacina() throws Exception {
        int databaseSizeBeforeUpdate = vacinaRepository.findAll().size();

        // Create the Vacina
        VacinaDTO vacinaDTO = vacinaMapper.toDto(vacina);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVacinaMockMvc.perform(put("/api/vacinas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacinaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vacina in the database
        List<Vacina> vacinaList = vacinaRepository.findAll();
        assertThat(vacinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVacina() throws Exception {
        // Initialize the database
        vacinaRepository.saveAndFlush(vacina);

        int databaseSizeBeforeDelete = vacinaRepository.findAll().size();

        // Delete the vacina
        restVacinaMockMvc.perform(delete("/api/vacinas/{id}", vacina.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vacina> vacinaList = vacinaRepository.findAll();
        assertThat(vacinaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
