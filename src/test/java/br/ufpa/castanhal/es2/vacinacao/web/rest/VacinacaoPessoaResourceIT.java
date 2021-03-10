package br.ufpa.castanhal.es2.vacinacao.web.rest;

import br.ufpa.castanhal.es2.vacinacao.ExemploVacinacaoApp;
import br.ufpa.castanhal.es2.vacinacao.domain.VacinacaoPessoa;
import br.ufpa.castanhal.es2.vacinacao.domain.Pessoa;
import br.ufpa.castanhal.es2.vacinacao.domain.Vacina;
import br.ufpa.castanhal.es2.vacinacao.domain.Fabricante;
import br.ufpa.castanhal.es2.vacinacao.repository.VacinacaoPessoaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.VacinacaoPessoaService;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.VacinacaoPessoaMapper;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.VacinacaoPessoaQueryService;

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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VacinacaoPessoaResource} REST controller.
 */
@SpringBootTest(classes = ExemploVacinacaoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VacinacaoPessoaResourceIT {

    private static final LocalDate DEFAULT_QUANDO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_QUANDO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_QUANDO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CNS = "AAAAAAAAAA";
    private static final String UPDATED_CNS = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_PROFISSIONAL = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_PROFISSIONAL = "BBBBBBBBBB";

    @Autowired
    private VacinacaoPessoaRepository vacinacaoPessoaRepository;

    @Autowired
    private VacinacaoPessoaMapper vacinacaoPessoaMapper;

    @Autowired
    private VacinacaoPessoaService vacinacaoPessoaService;

    @Autowired
    private VacinacaoPessoaQueryService vacinacaoPessoaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVacinacaoPessoaMockMvc;

    private VacinacaoPessoa vacinacaoPessoa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VacinacaoPessoa createEntity(EntityManager em) {
        VacinacaoPessoa vacinacaoPessoa = new VacinacaoPessoa()
            .quando(DEFAULT_QUANDO)
            .cns(DEFAULT_CNS)
            .codigoProfissional(DEFAULT_CODIGO_PROFISSIONAL);
        return vacinacaoPessoa;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VacinacaoPessoa createUpdatedEntity(EntityManager em) {
        VacinacaoPessoa vacinacaoPessoa = new VacinacaoPessoa()
            .quando(UPDATED_QUANDO)
            .cns(UPDATED_CNS)
            .codigoProfissional(UPDATED_CODIGO_PROFISSIONAL);
        return vacinacaoPessoa;
    }

    @BeforeEach
    public void initTest() {
        vacinacaoPessoa = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacinacaoPessoa() throws Exception {
        int databaseSizeBeforeCreate = vacinacaoPessoaRepository.findAll().size();
        // Create the VacinacaoPessoa
        VacinacaoPessoaDTO vacinacaoPessoaDTO = vacinacaoPessoaMapper.toDto(vacinacaoPessoa);
        restVacinacaoPessoaMockMvc.perform(post("/api/vacinacao-pessoas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoPessoaDTO)))
            .andExpect(status().isCreated());

        // Validate the VacinacaoPessoa in the database
        List<VacinacaoPessoa> vacinacaoPessoaList = vacinacaoPessoaRepository.findAll();
        assertThat(vacinacaoPessoaList).hasSize(databaseSizeBeforeCreate + 1);
        VacinacaoPessoa testVacinacaoPessoa = vacinacaoPessoaList.get(vacinacaoPessoaList.size() - 1);
        assertThat(testVacinacaoPessoa.getQuando()).isEqualTo(DEFAULT_QUANDO);
        assertThat(testVacinacaoPessoa.getCns()).isEqualTo(DEFAULT_CNS);
        assertThat(testVacinacaoPessoa.getCodigoProfissional()).isEqualTo(DEFAULT_CODIGO_PROFISSIONAL);
    }

    @Test
    @Transactional
    public void createVacinacaoPessoaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vacinacaoPessoaRepository.findAll().size();

        // Create the VacinacaoPessoa with an existing ID
        vacinacaoPessoa.setId(1L);
        VacinacaoPessoaDTO vacinacaoPessoaDTO = vacinacaoPessoaMapper.toDto(vacinacaoPessoa);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVacinacaoPessoaMockMvc.perform(post("/api/vacinacao-pessoas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoPessoaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VacinacaoPessoa in the database
        List<VacinacaoPessoa> vacinacaoPessoaList = vacinacaoPessoaRepository.findAll();
        assertThat(vacinacaoPessoaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVacinacaoPessoas() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList
        restVacinacaoPessoaMockMvc.perform(get("/api/vacinacao-pessoas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacinacaoPessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].quando").value(hasItem(DEFAULT_QUANDO.toString())))
            .andExpect(jsonPath("$.[*].cns").value(hasItem(DEFAULT_CNS)))
            .andExpect(jsonPath("$.[*].codigoProfissional").value(hasItem(DEFAULT_CODIGO_PROFISSIONAL)));
    }
    
    @Test
    @Transactional
    public void getVacinacaoPessoa() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get the vacinacaoPessoa
        restVacinacaoPessoaMockMvc.perform(get("/api/vacinacao-pessoas/{id}", vacinacaoPessoa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vacinacaoPessoa.getId().intValue()))
            .andExpect(jsonPath("$.quando").value(DEFAULT_QUANDO.toString()))
            .andExpect(jsonPath("$.cns").value(DEFAULT_CNS))
            .andExpect(jsonPath("$.codigoProfissional").value(DEFAULT_CODIGO_PROFISSIONAL));
    }


    @Test
    @Transactional
    public void getVacinacaoPessoasByIdFiltering() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        Long id = vacinacaoPessoa.getId();

        defaultVacinacaoPessoaShouldBeFound("id.equals=" + id);
        defaultVacinacaoPessoaShouldNotBeFound("id.notEquals=" + id);

        defaultVacinacaoPessoaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVacinacaoPessoaShouldNotBeFound("id.greaterThan=" + id);

        defaultVacinacaoPessoaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVacinacaoPessoaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVacinacaoPessoasByQuandoIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where quando equals to DEFAULT_QUANDO
        defaultVacinacaoPessoaShouldBeFound("quando.equals=" + DEFAULT_QUANDO);

        // Get all the vacinacaoPessoaList where quando equals to UPDATED_QUANDO
        defaultVacinacaoPessoaShouldNotBeFound("quando.equals=" + UPDATED_QUANDO);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByQuandoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where quando not equals to DEFAULT_QUANDO
        defaultVacinacaoPessoaShouldNotBeFound("quando.notEquals=" + DEFAULT_QUANDO);

        // Get all the vacinacaoPessoaList where quando not equals to UPDATED_QUANDO
        defaultVacinacaoPessoaShouldBeFound("quando.notEquals=" + UPDATED_QUANDO);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByQuandoIsInShouldWork() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where quando in DEFAULT_QUANDO or UPDATED_QUANDO
        defaultVacinacaoPessoaShouldBeFound("quando.in=" + DEFAULT_QUANDO + "," + UPDATED_QUANDO);

        // Get all the vacinacaoPessoaList where quando equals to UPDATED_QUANDO
        defaultVacinacaoPessoaShouldNotBeFound("quando.in=" + UPDATED_QUANDO);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByQuandoIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where quando is not null
        defaultVacinacaoPessoaShouldBeFound("quando.specified=true");

        // Get all the vacinacaoPessoaList where quando is null
        defaultVacinacaoPessoaShouldNotBeFound("quando.specified=false");
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByQuandoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where quando is greater than or equal to DEFAULT_QUANDO
        defaultVacinacaoPessoaShouldBeFound("quando.greaterThanOrEqual=" + DEFAULT_QUANDO);

        // Get all the vacinacaoPessoaList where quando is greater than or equal to UPDATED_QUANDO
        defaultVacinacaoPessoaShouldNotBeFound("quando.greaterThanOrEqual=" + UPDATED_QUANDO);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByQuandoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where quando is less than or equal to DEFAULT_QUANDO
        defaultVacinacaoPessoaShouldBeFound("quando.lessThanOrEqual=" + DEFAULT_QUANDO);

        // Get all the vacinacaoPessoaList where quando is less than or equal to SMALLER_QUANDO
        defaultVacinacaoPessoaShouldNotBeFound("quando.lessThanOrEqual=" + SMALLER_QUANDO);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByQuandoIsLessThanSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where quando is less than DEFAULT_QUANDO
        defaultVacinacaoPessoaShouldNotBeFound("quando.lessThan=" + DEFAULT_QUANDO);

        // Get all the vacinacaoPessoaList where quando is less than UPDATED_QUANDO
        defaultVacinacaoPessoaShouldBeFound("quando.lessThan=" + UPDATED_QUANDO);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByQuandoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where quando is greater than DEFAULT_QUANDO
        defaultVacinacaoPessoaShouldNotBeFound("quando.greaterThan=" + DEFAULT_QUANDO);

        // Get all the vacinacaoPessoaList where quando is greater than SMALLER_QUANDO
        defaultVacinacaoPessoaShouldBeFound("quando.greaterThan=" + SMALLER_QUANDO);
    }


    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCnsIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where cns equals to DEFAULT_CNS
        defaultVacinacaoPessoaShouldBeFound("cns.equals=" + DEFAULT_CNS);

        // Get all the vacinacaoPessoaList where cns equals to UPDATED_CNS
        defaultVacinacaoPessoaShouldNotBeFound("cns.equals=" + UPDATED_CNS);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCnsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where cns not equals to DEFAULT_CNS
        defaultVacinacaoPessoaShouldNotBeFound("cns.notEquals=" + DEFAULT_CNS);

        // Get all the vacinacaoPessoaList where cns not equals to UPDATED_CNS
        defaultVacinacaoPessoaShouldBeFound("cns.notEquals=" + UPDATED_CNS);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCnsIsInShouldWork() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where cns in DEFAULT_CNS or UPDATED_CNS
        defaultVacinacaoPessoaShouldBeFound("cns.in=" + DEFAULT_CNS + "," + UPDATED_CNS);

        // Get all the vacinacaoPessoaList where cns equals to UPDATED_CNS
        defaultVacinacaoPessoaShouldNotBeFound("cns.in=" + UPDATED_CNS);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCnsIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where cns is not null
        defaultVacinacaoPessoaShouldBeFound("cns.specified=true");

        // Get all the vacinacaoPessoaList where cns is null
        defaultVacinacaoPessoaShouldNotBeFound("cns.specified=false");
    }
                @Test
    @Transactional
    public void getAllVacinacaoPessoasByCnsContainsSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where cns contains DEFAULT_CNS
        defaultVacinacaoPessoaShouldBeFound("cns.contains=" + DEFAULT_CNS);

        // Get all the vacinacaoPessoaList where cns contains UPDATED_CNS
        defaultVacinacaoPessoaShouldNotBeFound("cns.contains=" + UPDATED_CNS);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCnsNotContainsSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where cns does not contain DEFAULT_CNS
        defaultVacinacaoPessoaShouldNotBeFound("cns.doesNotContain=" + DEFAULT_CNS);

        // Get all the vacinacaoPessoaList where cns does not contain UPDATED_CNS
        defaultVacinacaoPessoaShouldBeFound("cns.doesNotContain=" + UPDATED_CNS);
    }


    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCodigoProfissionalIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where codigoProfissional equals to DEFAULT_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldBeFound("codigoProfissional.equals=" + DEFAULT_CODIGO_PROFISSIONAL);

        // Get all the vacinacaoPessoaList where codigoProfissional equals to UPDATED_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldNotBeFound("codigoProfissional.equals=" + UPDATED_CODIGO_PROFISSIONAL);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCodigoProfissionalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where codigoProfissional not equals to DEFAULT_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldNotBeFound("codigoProfissional.notEquals=" + DEFAULT_CODIGO_PROFISSIONAL);

        // Get all the vacinacaoPessoaList where codigoProfissional not equals to UPDATED_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldBeFound("codigoProfissional.notEquals=" + UPDATED_CODIGO_PROFISSIONAL);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCodigoProfissionalIsInShouldWork() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where codigoProfissional in DEFAULT_CODIGO_PROFISSIONAL or UPDATED_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldBeFound("codigoProfissional.in=" + DEFAULT_CODIGO_PROFISSIONAL + "," + UPDATED_CODIGO_PROFISSIONAL);

        // Get all the vacinacaoPessoaList where codigoProfissional equals to UPDATED_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldNotBeFound("codigoProfissional.in=" + UPDATED_CODIGO_PROFISSIONAL);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCodigoProfissionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where codigoProfissional is not null
        defaultVacinacaoPessoaShouldBeFound("codigoProfissional.specified=true");

        // Get all the vacinacaoPessoaList where codigoProfissional is null
        defaultVacinacaoPessoaShouldNotBeFound("codigoProfissional.specified=false");
    }
                @Test
    @Transactional
    public void getAllVacinacaoPessoasByCodigoProfissionalContainsSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where codigoProfissional contains DEFAULT_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldBeFound("codigoProfissional.contains=" + DEFAULT_CODIGO_PROFISSIONAL);

        // Get all the vacinacaoPessoaList where codigoProfissional contains UPDATED_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldNotBeFound("codigoProfissional.contains=" + UPDATED_CODIGO_PROFISSIONAL);
    }

    @Test
    @Transactional
    public void getAllVacinacaoPessoasByCodigoProfissionalNotContainsSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        // Get all the vacinacaoPessoaList where codigoProfissional does not contain DEFAULT_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldNotBeFound("codigoProfissional.doesNotContain=" + DEFAULT_CODIGO_PROFISSIONAL);

        // Get all the vacinacaoPessoaList where codigoProfissional does not contain UPDATED_CODIGO_PROFISSIONAL
        defaultVacinacaoPessoaShouldBeFound("codigoProfissional.doesNotContain=" + UPDATED_CODIGO_PROFISSIONAL);
    }


    @Test
    @Transactional
    public void getAllVacinacaoPessoasByPessoaIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);
        Pessoa pessoa = PessoaResourceIT.createEntity(em);
        em.persist(pessoa);
        em.flush();
        vacinacaoPessoa.setPessoa(pessoa);
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);
        Long pessoaId = pessoa.getId();

        // Get all the vacinacaoPessoaList where pessoa equals to pessoaId
        defaultVacinacaoPessoaShouldBeFound("pessoaId.equals=" + pessoaId);

        // Get all the vacinacaoPessoaList where pessoa equals to pessoaId + 1
        defaultVacinacaoPessoaShouldNotBeFound("pessoaId.equals=" + (pessoaId + 1));
    }


    @Test
    @Transactional
    public void getAllVacinacaoPessoasByVacinaIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);
        Vacina vacina = VacinaResourceIT.createEntity(em);
        em.persist(vacina);
        em.flush();
        vacinacaoPessoa.setVacina(vacina);
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);
        Long vacinaId = vacina.getId();

        // Get all the vacinacaoPessoaList where vacina equals to vacinaId
        defaultVacinacaoPessoaShouldBeFound("vacinaId.equals=" + vacinaId);

        // Get all the vacinacaoPessoaList where vacina equals to vacinaId + 1
        defaultVacinacaoPessoaShouldNotBeFound("vacinaId.equals=" + (vacinaId + 1));
    }


    @Test
    @Transactional
    public void getAllVacinacaoPessoasByFabricanteIsEqualToSomething() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);
        Fabricante fabricante = FabricanteResourceIT.createEntity(em);
        em.persist(fabricante);
        em.flush();
        vacinacaoPessoa.setFabricante(fabricante);
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);
        Long fabricanteId = fabricante.getId();

        // Get all the vacinacaoPessoaList where fabricante equals to fabricanteId
        defaultVacinacaoPessoaShouldBeFound("fabricanteId.equals=" + fabricanteId);

        // Get all the vacinacaoPessoaList where fabricante equals to fabricanteId + 1
        defaultVacinacaoPessoaShouldNotBeFound("fabricanteId.equals=" + (fabricanteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVacinacaoPessoaShouldBeFound(String filter) throws Exception {
        restVacinacaoPessoaMockMvc.perform(get("/api/vacinacao-pessoas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacinacaoPessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].quando").value(hasItem(DEFAULT_QUANDO.toString())))
            .andExpect(jsonPath("$.[*].cns").value(hasItem(DEFAULT_CNS)))
            .andExpect(jsonPath("$.[*].codigoProfissional").value(hasItem(DEFAULT_CODIGO_PROFISSIONAL)));

        // Check, that the count call also returns 1
        restVacinacaoPessoaMockMvc.perform(get("/api/vacinacao-pessoas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVacinacaoPessoaShouldNotBeFound(String filter) throws Exception {
        restVacinacaoPessoaMockMvc.perform(get("/api/vacinacao-pessoas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVacinacaoPessoaMockMvc.perform(get("/api/vacinacao-pessoas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingVacinacaoPessoa() throws Exception {
        // Get the vacinacaoPessoa
        restVacinacaoPessoaMockMvc.perform(get("/api/vacinacao-pessoas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacinacaoPessoa() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        int databaseSizeBeforeUpdate = vacinacaoPessoaRepository.findAll().size();

        // Update the vacinacaoPessoa
        VacinacaoPessoa updatedVacinacaoPessoa = vacinacaoPessoaRepository.findById(vacinacaoPessoa.getId()).get();
        // Disconnect from session so that the updates on updatedVacinacaoPessoa are not directly saved in db
        em.detach(updatedVacinacaoPessoa);
        updatedVacinacaoPessoa
            .quando(UPDATED_QUANDO)
            .cns(UPDATED_CNS)
            .codigoProfissional(UPDATED_CODIGO_PROFISSIONAL);
        VacinacaoPessoaDTO vacinacaoPessoaDTO = vacinacaoPessoaMapper.toDto(updatedVacinacaoPessoa);

        restVacinacaoPessoaMockMvc.perform(put("/api/vacinacao-pessoas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoPessoaDTO)))
            .andExpect(status().isOk());

        // Validate the VacinacaoPessoa in the database
        List<VacinacaoPessoa> vacinacaoPessoaList = vacinacaoPessoaRepository.findAll();
        assertThat(vacinacaoPessoaList).hasSize(databaseSizeBeforeUpdate);
        VacinacaoPessoa testVacinacaoPessoa = vacinacaoPessoaList.get(vacinacaoPessoaList.size() - 1);
        assertThat(testVacinacaoPessoa.getQuando()).isEqualTo(UPDATED_QUANDO);
        assertThat(testVacinacaoPessoa.getCns()).isEqualTo(UPDATED_CNS);
        assertThat(testVacinacaoPessoa.getCodigoProfissional()).isEqualTo(UPDATED_CODIGO_PROFISSIONAL);
    }

    @Test
    @Transactional
    public void updateNonExistingVacinacaoPessoa() throws Exception {
        int databaseSizeBeforeUpdate = vacinacaoPessoaRepository.findAll().size();

        // Create the VacinacaoPessoa
        VacinacaoPessoaDTO vacinacaoPessoaDTO = vacinacaoPessoaMapper.toDto(vacinacaoPessoa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVacinacaoPessoaMockMvc.perform(put("/api/vacinacao-pessoas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoPessoaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VacinacaoPessoa in the database
        List<VacinacaoPessoa> vacinacaoPessoaList = vacinacaoPessoaRepository.findAll();
        assertThat(vacinacaoPessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVacinacaoPessoa() throws Exception {
        // Initialize the database
        vacinacaoPessoaRepository.saveAndFlush(vacinacaoPessoa);

        int databaseSizeBeforeDelete = vacinacaoPessoaRepository.findAll().size();

        // Delete the vacinacaoPessoa
        restVacinacaoPessoaMockMvc.perform(delete("/api/vacinacao-pessoas/{id}", vacinacaoPessoa.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VacinacaoPessoa> vacinacaoPessoaList = vacinacaoPessoaRepository.findAll();
        assertThat(vacinacaoPessoaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
