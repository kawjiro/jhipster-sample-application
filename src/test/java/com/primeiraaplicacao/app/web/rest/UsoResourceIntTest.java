package com.primeiraaplicacao.app.web.rest;

import com.primeiraaplicacao.app.PrimeiraAplicacaoApp;

import com.primeiraaplicacao.app.domain.Uso;
import com.primeiraaplicacao.app.repository.UsoRepository;
import com.primeiraaplicacao.app.repository.search.UsoSearchRepository;
import com.primeiraaplicacao.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.primeiraaplicacao.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UsoResource REST controller.
 *
 * @see UsoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeiraAplicacaoApp.class)
public class UsoResourceIntTest {

    private static final LocalDate DEFAULT_DATA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_FIM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_FIM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TIPO_EQUIPAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_EQUIPAMENTO = "BBBBBBBBBB";

    @Autowired
    private UsoRepository usoRepository;

    /**
     * This repository is mocked in the com.primeiraaplicacao.app.repository.search test package.
     *
     * @see com.primeiraaplicacao.app.repository.search.UsoSearchRepositoryMockConfiguration
     */
    @Autowired
    private UsoSearchRepository mockUsoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUsoMockMvc;

    private Uso uso;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UsoResource usoResource = new UsoResource(usoRepository, mockUsoSearchRepository);
        this.restUsoMockMvc = MockMvcBuilders.standaloneSetup(usoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uso createEntity(EntityManager em) {
        Uso uso = new Uso()
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM)
            .tipoEquipamento(DEFAULT_TIPO_EQUIPAMENTO);
        return uso;
    }

    @Before
    public void initTest() {
        uso = createEntity(em);
    }

    @Test
    @Transactional
    public void createUso() throws Exception {
        int databaseSizeBeforeCreate = usoRepository.findAll().size();

        // Create the Uso
        restUsoMockMvc.perform(post("/api/usos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uso)))
            .andExpect(status().isCreated());

        // Validate the Uso in the database
        List<Uso> usoList = usoRepository.findAll();
        assertThat(usoList).hasSize(databaseSizeBeforeCreate + 1);
        Uso testUso = usoList.get(usoList.size() - 1);
        assertThat(testUso.getDataInicio()).isEqualTo(DEFAULT_DATA_INICIO);
        assertThat(testUso.getDataFim()).isEqualTo(DEFAULT_DATA_FIM);
        assertThat(testUso.getTipoEquipamento()).isEqualTo(DEFAULT_TIPO_EQUIPAMENTO);

        // Validate the Uso in Elasticsearch
        verify(mockUsoSearchRepository, times(1)).save(testUso);
    }

    @Test
    @Transactional
    public void createUsoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = usoRepository.findAll().size();

        // Create the Uso with an existing ID
        uso.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsoMockMvc.perform(post("/api/usos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uso)))
            .andExpect(status().isBadRequest());

        // Validate the Uso in the database
        List<Uso> usoList = usoRepository.findAll();
        assertThat(usoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Uso in Elasticsearch
        verify(mockUsoSearchRepository, times(0)).save(uso);
    }

    @Test
    @Transactional
    public void getAllUsos() throws Exception {
        // Initialize the database
        usoRepository.saveAndFlush(uso);

        // Get all the usoList
        restUsoMockMvc.perform(get("/api/usos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uso.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].tipoEquipamento").value(hasItem(DEFAULT_TIPO_EQUIPAMENTO.toString())));
    }
    
    @Test
    @Transactional
    public void getUso() throws Exception {
        // Initialize the database
        usoRepository.saveAndFlush(uso);

        // Get the uso
        restUsoMockMvc.perform(get("/api/usos/{id}", uso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(uso.getId().intValue()))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO.toString()))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM.toString()))
            .andExpect(jsonPath("$.tipoEquipamento").value(DEFAULT_TIPO_EQUIPAMENTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUso() throws Exception {
        // Get the uso
        restUsoMockMvc.perform(get("/api/usos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUso() throws Exception {
        // Initialize the database
        usoRepository.saveAndFlush(uso);

        int databaseSizeBeforeUpdate = usoRepository.findAll().size();

        // Update the uso
        Uso updatedUso = usoRepository.findById(uso.getId()).get();
        // Disconnect from session so that the updates on updatedUso are not directly saved in db
        em.detach(updatedUso);
        updatedUso
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .tipoEquipamento(UPDATED_TIPO_EQUIPAMENTO);

        restUsoMockMvc.perform(put("/api/usos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUso)))
            .andExpect(status().isOk());

        // Validate the Uso in the database
        List<Uso> usoList = usoRepository.findAll();
        assertThat(usoList).hasSize(databaseSizeBeforeUpdate);
        Uso testUso = usoList.get(usoList.size() - 1);
        assertThat(testUso.getDataInicio()).isEqualTo(UPDATED_DATA_INICIO);
        assertThat(testUso.getDataFim()).isEqualTo(UPDATED_DATA_FIM);
        assertThat(testUso.getTipoEquipamento()).isEqualTo(UPDATED_TIPO_EQUIPAMENTO);

        // Validate the Uso in Elasticsearch
        verify(mockUsoSearchRepository, times(1)).save(testUso);
    }

    @Test
    @Transactional
    public void updateNonExistingUso() throws Exception {
        int databaseSizeBeforeUpdate = usoRepository.findAll().size();

        // Create the Uso

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsoMockMvc.perform(put("/api/usos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uso)))
            .andExpect(status().isBadRequest());

        // Validate the Uso in the database
        List<Uso> usoList = usoRepository.findAll();
        assertThat(usoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Uso in Elasticsearch
        verify(mockUsoSearchRepository, times(0)).save(uso);
    }

    @Test
    @Transactional
    public void deleteUso() throws Exception {
        // Initialize the database
        usoRepository.saveAndFlush(uso);

        int databaseSizeBeforeDelete = usoRepository.findAll().size();

        // Get the uso
        restUsoMockMvc.perform(delete("/api/usos/{id}", uso.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Uso> usoList = usoRepository.findAll();
        assertThat(usoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Uso in Elasticsearch
        verify(mockUsoSearchRepository, times(1)).deleteById(uso.getId());
    }

    @Test
    @Transactional
    public void searchUso() throws Exception {
        // Initialize the database
        usoRepository.saveAndFlush(uso);
        when(mockUsoSearchRepository.search(queryStringQuery("id:" + uso.getId())))
            .thenReturn(Collections.singletonList(uso));
        // Search the uso
        restUsoMockMvc.perform(get("/api/_search/usos?query=id:" + uso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uso.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].tipoEquipamento").value(hasItem(DEFAULT_TIPO_EQUIPAMENTO)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Uso.class);
        Uso uso1 = new Uso();
        uso1.setId(1L);
        Uso uso2 = new Uso();
        uso2.setId(uso1.getId());
        assertThat(uso1).isEqualTo(uso2);
        uso2.setId(2L);
        assertThat(uso1).isNotEqualTo(uso2);
        uso1.setId(null);
        assertThat(uso1).isNotEqualTo(uso2);
    }
}
