package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.ExemploVacinacaoApp;
import br.ufpa.castanhal.es2.vacinacao.domain.Doenca;
import br.ufpa.castanhal.es2.vacinacao.domain.Pais;
import br.ufpa.castanhal.es2.vacinacao.repository.DoencaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.DoencaService;
import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.DoencaMapper;
import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.DoencaQueryService;

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
import java.time.LocalDate;
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
 * Integration tests for the {@link DoencaResource} REST controller.
 */
@SpringBootTest(classes = ExemploVacinacaoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DoencaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final LocalDate DEFAULT_DATA_PRIMEIRO_CASO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PRIMEIRO_CASO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_PRIMEIRO_CASO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LOCAL_PRIMEIRO_CASO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOCAL_PRIMEIRO_CASO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LOCAL_PRIMEIRO_CASO = LocalDate.ofEpochDay(-1L);

    @Autowired
    private DoencaRepository doencaRepository;

    @Autowired
    private DoencaMapper doencaMapper;

    @Autowired
    private DoencaService doencaService;

    @Autowired
    private DoencaQueryService doencaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoencaMockMvc;

    private Doenca doenca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doenca createEntity(EntityManager em) {
        Doenca doenca = new Doenca()
            .nome(DEFAULT_NOME)
            .criado(DEFAULT_CRIADO)
            .dataPrimeiroCaso(DEFAULT_DATA_PRIMEIRO_CASO)
            .localPrimeiroCaso(DEFAULT_LOCAL_PRIMEIRO_CASO);
        return doenca;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doenca createUpdatedEntity(EntityManager em) {
        Doenca doenca = new Doenca()
            .nome(UPDATED_NOME)
            .criado(UPDATED_CRIADO)
            .dataPrimeiroCaso(UPDATED_DATA_PRIMEIRO_CASO)
            .localPrimeiroCaso(UPDATED_LOCAL_PRIMEIRO_CASO);
        return doenca;
    }

    @BeforeEach
    public void initTest() {
        doenca = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoenca() throws Exception {
        int databaseSizeBeforeCreate = doencaRepository.findAll().size();
        // Create the Doenca
        DoencaDTO doencaDTO = doencaMapper.toDto(doenca);
        restDoencaMockMvc.perform(post("/api/doencas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doencaDTO)))
            .andExpect(status().isCreated());

        // Validate the Doenca in the database
        List<Doenca> doencaList = doencaRepository.findAll();
        assertThat(doencaList).hasSize(databaseSizeBeforeCreate + 1);
        Doenca testDoenca = doencaList.get(doencaList.size() - 1);
        assertThat(testDoenca.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDoenca.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testDoenca.getDataPrimeiroCaso()).isEqualTo(DEFAULT_DATA_PRIMEIRO_CASO);
        assertThat(testDoenca.getLocalPrimeiroCaso()).isEqualTo(DEFAULT_LOCAL_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void createDoencaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doencaRepository.findAll().size();

        // Create the Doenca with an existing ID
        doenca.setId(1L);
        DoencaDTO doencaDTO = doencaMapper.toDto(doenca);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoencaMockMvc.perform(post("/api/doencas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doencaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doenca in the database
        List<Doenca> doencaList = doencaRepository.findAll();
        assertThat(doencaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDoencas() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList
        restDoencaMockMvc.perform(get("/api/doencas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doenca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].dataPrimeiroCaso").value(hasItem(DEFAULT_DATA_PRIMEIRO_CASO.toString())))
            .andExpect(jsonPath("$.[*].localPrimeiroCaso").value(hasItem(DEFAULT_LOCAL_PRIMEIRO_CASO.toString())));
    }
    
    @Test
    @Transactional
    public void getDoenca() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get the doenca
        restDoencaMockMvc.perform(get("/api/doencas/{id}", doenca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doenca.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.dataPrimeiroCaso").value(DEFAULT_DATA_PRIMEIRO_CASO.toString()))
            .andExpect(jsonPath("$.localPrimeiroCaso").value(DEFAULT_LOCAL_PRIMEIRO_CASO.toString()));
    }


    @Test
    @Transactional
    public void getDoencasByIdFiltering() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        Long id = doenca.getId();

        defaultDoencaShouldBeFound("id.equals=" + id);
        defaultDoencaShouldNotBeFound("id.notEquals=" + id);

        defaultDoencaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDoencaShouldNotBeFound("id.greaterThan=" + id);

        defaultDoencaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDoencaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDoencasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where nome equals to DEFAULT_NOME
        defaultDoencaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the doencaList where nome equals to UPDATED_NOME
        defaultDoencaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDoencasByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where nome not equals to DEFAULT_NOME
        defaultDoencaShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the doencaList where nome not equals to UPDATED_NOME
        defaultDoencaShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDoencasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultDoencaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the doencaList where nome equals to UPDATED_NOME
        defaultDoencaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDoencasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where nome is not null
        defaultDoencaShouldBeFound("nome.specified=true");

        // Get all the doencaList where nome is null
        defaultDoencaShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoencasByNomeContainsSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where nome contains DEFAULT_NOME
        defaultDoencaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the doencaList where nome contains UPDATED_NOME
        defaultDoencaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDoencasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where nome does not contain DEFAULT_NOME
        defaultDoencaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the doencaList where nome does not contain UPDATED_NOME
        defaultDoencaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllDoencasByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where criado equals to DEFAULT_CRIADO
        defaultDoencaShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the doencaList where criado equals to UPDATED_CRIADO
        defaultDoencaShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllDoencasByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where criado not equals to DEFAULT_CRIADO
        defaultDoencaShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the doencaList where criado not equals to UPDATED_CRIADO
        defaultDoencaShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllDoencasByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultDoencaShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the doencaList where criado equals to UPDATED_CRIADO
        defaultDoencaShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllDoencasByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where criado is not null
        defaultDoencaShouldBeFound("criado.specified=true");

        // Get all the doencaList where criado is null
        defaultDoencaShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoencasByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where criado is greater than or equal to DEFAULT_CRIADO
        defaultDoencaShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the doencaList where criado is greater than or equal to UPDATED_CRIADO
        defaultDoencaShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllDoencasByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where criado is less than or equal to DEFAULT_CRIADO
        defaultDoencaShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the doencaList where criado is less than or equal to SMALLER_CRIADO
        defaultDoencaShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    public void getAllDoencasByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where criado is less than DEFAULT_CRIADO
        defaultDoencaShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the doencaList where criado is less than UPDATED_CRIADO
        defaultDoencaShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    public void getAllDoencasByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where criado is greater than DEFAULT_CRIADO
        defaultDoencaShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the doencaList where criado is greater than SMALLER_CRIADO
        defaultDoencaShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }


    @Test
    @Transactional
    public void getAllDoencasByDataPrimeiroCasoIsEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where dataPrimeiroCaso equals to DEFAULT_DATA_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("dataPrimeiroCaso.equals=" + DEFAULT_DATA_PRIMEIRO_CASO);

        // Get all the doencaList where dataPrimeiroCaso equals to UPDATED_DATA_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("dataPrimeiroCaso.equals=" + UPDATED_DATA_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByDataPrimeiroCasoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where dataPrimeiroCaso not equals to DEFAULT_DATA_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("dataPrimeiroCaso.notEquals=" + DEFAULT_DATA_PRIMEIRO_CASO);

        // Get all the doencaList where dataPrimeiroCaso not equals to UPDATED_DATA_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("dataPrimeiroCaso.notEquals=" + UPDATED_DATA_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByDataPrimeiroCasoIsInShouldWork() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where dataPrimeiroCaso in DEFAULT_DATA_PRIMEIRO_CASO or UPDATED_DATA_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("dataPrimeiroCaso.in=" + DEFAULT_DATA_PRIMEIRO_CASO + "," + UPDATED_DATA_PRIMEIRO_CASO);

        // Get all the doencaList where dataPrimeiroCaso equals to UPDATED_DATA_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("dataPrimeiroCaso.in=" + UPDATED_DATA_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByDataPrimeiroCasoIsNullOrNotNull() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where dataPrimeiroCaso is not null
        defaultDoencaShouldBeFound("dataPrimeiroCaso.specified=true");

        // Get all the doencaList where dataPrimeiroCaso is null
        defaultDoencaShouldNotBeFound("dataPrimeiroCaso.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoencasByDataPrimeiroCasoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where dataPrimeiroCaso is greater than or equal to DEFAULT_DATA_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("dataPrimeiroCaso.greaterThanOrEqual=" + DEFAULT_DATA_PRIMEIRO_CASO);

        // Get all the doencaList where dataPrimeiroCaso is greater than or equal to UPDATED_DATA_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("dataPrimeiroCaso.greaterThanOrEqual=" + UPDATED_DATA_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByDataPrimeiroCasoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where dataPrimeiroCaso is less than or equal to DEFAULT_DATA_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("dataPrimeiroCaso.lessThanOrEqual=" + DEFAULT_DATA_PRIMEIRO_CASO);

        // Get all the doencaList where dataPrimeiroCaso is less than or equal to SMALLER_DATA_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("dataPrimeiroCaso.lessThanOrEqual=" + SMALLER_DATA_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByDataPrimeiroCasoIsLessThanSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where dataPrimeiroCaso is less than DEFAULT_DATA_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("dataPrimeiroCaso.lessThan=" + DEFAULT_DATA_PRIMEIRO_CASO);

        // Get all the doencaList where dataPrimeiroCaso is less than UPDATED_DATA_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("dataPrimeiroCaso.lessThan=" + UPDATED_DATA_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByDataPrimeiroCasoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where dataPrimeiroCaso is greater than DEFAULT_DATA_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("dataPrimeiroCaso.greaterThan=" + DEFAULT_DATA_PRIMEIRO_CASO);

        // Get all the doencaList where dataPrimeiroCaso is greater than SMALLER_DATA_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("dataPrimeiroCaso.greaterThan=" + SMALLER_DATA_PRIMEIRO_CASO);
    }


    @Test
    @Transactional
    public void getAllDoencasByLocalPrimeiroCasoIsEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where localPrimeiroCaso equals to DEFAULT_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("localPrimeiroCaso.equals=" + DEFAULT_LOCAL_PRIMEIRO_CASO);

        // Get all the doencaList where localPrimeiroCaso equals to UPDATED_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("localPrimeiroCaso.equals=" + UPDATED_LOCAL_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByLocalPrimeiroCasoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where localPrimeiroCaso not equals to DEFAULT_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("localPrimeiroCaso.notEquals=" + DEFAULT_LOCAL_PRIMEIRO_CASO);

        // Get all the doencaList where localPrimeiroCaso not equals to UPDATED_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("localPrimeiroCaso.notEquals=" + UPDATED_LOCAL_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByLocalPrimeiroCasoIsInShouldWork() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where localPrimeiroCaso in DEFAULT_LOCAL_PRIMEIRO_CASO or UPDATED_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("localPrimeiroCaso.in=" + DEFAULT_LOCAL_PRIMEIRO_CASO + "," + UPDATED_LOCAL_PRIMEIRO_CASO);

        // Get all the doencaList where localPrimeiroCaso equals to UPDATED_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("localPrimeiroCaso.in=" + UPDATED_LOCAL_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByLocalPrimeiroCasoIsNullOrNotNull() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where localPrimeiroCaso is not null
        defaultDoencaShouldBeFound("localPrimeiroCaso.specified=true");

        // Get all the doencaList where localPrimeiroCaso is null
        defaultDoencaShouldNotBeFound("localPrimeiroCaso.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoencasByLocalPrimeiroCasoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where localPrimeiroCaso is greater than or equal to DEFAULT_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("localPrimeiroCaso.greaterThanOrEqual=" + DEFAULT_LOCAL_PRIMEIRO_CASO);

        // Get all the doencaList where localPrimeiroCaso is greater than or equal to UPDATED_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("localPrimeiroCaso.greaterThanOrEqual=" + UPDATED_LOCAL_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByLocalPrimeiroCasoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where localPrimeiroCaso is less than or equal to DEFAULT_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("localPrimeiroCaso.lessThanOrEqual=" + DEFAULT_LOCAL_PRIMEIRO_CASO);

        // Get all the doencaList where localPrimeiroCaso is less than or equal to SMALLER_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("localPrimeiroCaso.lessThanOrEqual=" + SMALLER_LOCAL_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByLocalPrimeiroCasoIsLessThanSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where localPrimeiroCaso is less than DEFAULT_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("localPrimeiroCaso.lessThan=" + DEFAULT_LOCAL_PRIMEIRO_CASO);

        // Get all the doencaList where localPrimeiroCaso is less than UPDATED_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("localPrimeiroCaso.lessThan=" + UPDATED_LOCAL_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void getAllDoencasByLocalPrimeiroCasoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        // Get all the doencaList where localPrimeiroCaso is greater than DEFAULT_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldNotBeFound("localPrimeiroCaso.greaterThan=" + DEFAULT_LOCAL_PRIMEIRO_CASO);

        // Get all the doencaList where localPrimeiroCaso is greater than SMALLER_LOCAL_PRIMEIRO_CASO
        defaultDoencaShouldBeFound("localPrimeiroCaso.greaterThan=" + SMALLER_LOCAL_PRIMEIRO_CASO);
    }


    @Test
    @Transactional
    public void getAllDoencasByPaisPrimeiroCasoIsEqualToSomething() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);
        Pais paisPrimeiroCaso = PaisResourceIT.createEntity(em);
        em.persist(paisPrimeiroCaso);
        em.flush();
        doenca.setPaisPrimeiroCaso(paisPrimeiroCaso);
        doencaRepository.saveAndFlush(doenca);
        Long paisPrimeiroCasoId = paisPrimeiroCaso.getId();

        // Get all the doencaList where paisPrimeiroCaso equals to paisPrimeiroCasoId
        defaultDoencaShouldBeFound("paisPrimeiroCasoId.equals=" + paisPrimeiroCasoId);

        // Get all the doencaList where paisPrimeiroCaso equals to paisPrimeiroCasoId + 1
        defaultDoencaShouldNotBeFound("paisPrimeiroCasoId.equals=" + (paisPrimeiroCasoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoencaShouldBeFound(String filter) throws Exception {
        restDoencaMockMvc.perform(get("/api/doencas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doenca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].dataPrimeiroCaso").value(hasItem(DEFAULT_DATA_PRIMEIRO_CASO.toString())))
            .andExpect(jsonPath("$.[*].localPrimeiroCaso").value(hasItem(DEFAULT_LOCAL_PRIMEIRO_CASO.toString())));

        // Check, that the count call also returns 1
        restDoencaMockMvc.perform(get("/api/doencas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoencaShouldNotBeFound(String filter) throws Exception {
        restDoencaMockMvc.perform(get("/api/doencas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoencaMockMvc.perform(get("/api/doencas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingDoenca() throws Exception {
        // Get the doenca
        restDoencaMockMvc.perform(get("/api/doencas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoenca() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        int databaseSizeBeforeUpdate = doencaRepository.findAll().size();

        // Update the doenca
        Doenca updatedDoenca = doencaRepository.findById(doenca.getId()).get();
        // Disconnect from session so that the updates on updatedDoenca are not directly saved in db
        em.detach(updatedDoenca);
        updatedDoenca
            .nome(UPDATED_NOME)
            .criado(UPDATED_CRIADO)
            .dataPrimeiroCaso(UPDATED_DATA_PRIMEIRO_CASO)
            .localPrimeiroCaso(UPDATED_LOCAL_PRIMEIRO_CASO);
        DoencaDTO doencaDTO = doencaMapper.toDto(updatedDoenca);

        restDoencaMockMvc.perform(put("/api/doencas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doencaDTO)))
            .andExpect(status().isOk());

        // Validate the Doenca in the database
        List<Doenca> doencaList = doencaRepository.findAll();
        assertThat(doencaList).hasSize(databaseSizeBeforeUpdate);
        Doenca testDoenca = doencaList.get(doencaList.size() - 1);
        assertThat(testDoenca.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDoenca.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testDoenca.getDataPrimeiroCaso()).isEqualTo(UPDATED_DATA_PRIMEIRO_CASO);
        assertThat(testDoenca.getLocalPrimeiroCaso()).isEqualTo(UPDATED_LOCAL_PRIMEIRO_CASO);
    }

    @Test
    @Transactional
    public void updateNonExistingDoenca() throws Exception {
        int databaseSizeBeforeUpdate = doencaRepository.findAll().size();

        // Create the Doenca
        DoencaDTO doencaDTO = doencaMapper.toDto(doenca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoencaMockMvc.perform(put("/api/doencas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doencaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doenca in the database
        List<Doenca> doencaList = doencaRepository.findAll();
        assertThat(doencaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDoenca() throws Exception {
        // Initialize the database
        doencaRepository.saveAndFlush(doenca);

        int databaseSizeBeforeDelete = doencaRepository.findAll().size();

        // Delete the doenca
        restDoencaMockMvc.perform(delete("/api/doencas/{id}", doenca.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doenca> doencaList = doencaRepository.findAll();
        assertThat(doencaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
