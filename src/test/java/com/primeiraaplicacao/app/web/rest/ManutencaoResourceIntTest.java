package com.primeiraaplicacao.app.web.rest;

import com.primeiraaplicacao.app.PrimeiraAplicacaoApp;

import com.primeiraaplicacao.app.domain.Manutencao;
import com.primeiraaplicacao.app.repository.ManutencaoRepository;
import com.primeiraaplicacao.app.repository.search.ManutencaoSearchRepository;
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
 * Test class for the ManutencaoResource REST controller.
 *
 * @see ManutencaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeiraAplicacaoApp.class)
public class ManutencaoResourceIntTest {

    private static final String DEFAULT_ESTADO_EQUIPAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO_EQUIPAMENTO = "BBBBBBBBBB";

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    /**
     * This repository is mocked in the com.primeiraaplicacao.app.repository.search test package.
     *
     * @see com.primeiraaplicacao.app.repository.search.ManutencaoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ManutencaoSearchRepository mockManutencaoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restManutencaoMockMvc;

    private Manutencao manutencao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ManutencaoResource manutencaoResource = new ManutencaoResource(manutencaoRepository, mockManutencaoSearchRepository);
        this.restManutencaoMockMvc = MockMvcBuilders.standaloneSetup(manutencaoResource)
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
    public static Manutencao createEntity(EntityManager em) {
        Manutencao manutencao = new Manutencao()
            .estadoEquipamento(DEFAULT_ESTADO_EQUIPAMENTO);
        return manutencao;
    }

    @Before
    public void initTest() {
        manutencao = createEntity(em);
    }

    @Test
    @Transactional
    public void createManutencao() throws Exception {
        int databaseSizeBeforeCreate = manutencaoRepository.findAll().size();

        // Create the Manutencao
        restManutencaoMockMvc.perform(post("/api/manutencaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manutencao)))
            .andExpect(status().isCreated());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeCreate + 1);
        Manutencao testManutencao = manutencaoList.get(manutencaoList.size() - 1);
        assertThat(testManutencao.getEstadoEquipamento()).isEqualTo(DEFAULT_ESTADO_EQUIPAMENTO);

        // Validate the Manutencao in Elasticsearch
        verify(mockManutencaoSearchRepository, times(1)).save(testManutencao);
    }

    @Test
    @Transactional
    public void createManutencaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = manutencaoRepository.findAll().size();

        // Create the Manutencao with an existing ID
        manutencao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restManutencaoMockMvc.perform(post("/api/manutencaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manutencao)))
            .andExpect(status().isBadRequest());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Manutencao in Elasticsearch
        verify(mockManutencaoSearchRepository, times(0)).save(manutencao);
    }

    @Test
    @Transactional
    public void getAllManutencaos() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList
        restManutencaoMockMvc.perform(get("/api/manutencaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manutencao.getId().intValue())))
            .andExpect(jsonPath("$.[*].estadoEquipamento").value(hasItem(DEFAULT_ESTADO_EQUIPAMENTO.toString())));
    }
    
    @Test
    @Transactional
    public void getManutencao() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get the manutencao
        restManutencaoMockMvc.perform(get("/api/manutencaos/{id}", manutencao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(manutencao.getId().intValue()))
            .andExpect(jsonPath("$.estadoEquipamento").value(DEFAULT_ESTADO_EQUIPAMENTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingManutencao() throws Exception {
        // Get the manutencao
        restManutencaoMockMvc.perform(get("/api/manutencaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManutencao() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();

        // Update the manutencao
        Manutencao updatedManutencao = manutencaoRepository.findById(manutencao.getId()).get();
        // Disconnect from session so that the updates on updatedManutencao are not directly saved in db
        em.detach(updatedManutencao);
        updatedManutencao
            .estadoEquipamento(UPDATED_ESTADO_EQUIPAMENTO);

        restManutencaoMockMvc.perform(put("/api/manutencaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedManutencao)))
            .andExpect(status().isOk());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
        Manutencao testManutencao = manutencaoList.get(manutencaoList.size() - 1);
        assertThat(testManutencao.getEstadoEquipamento()).isEqualTo(UPDATED_ESTADO_EQUIPAMENTO);

        // Validate the Manutencao in Elasticsearch
        verify(mockManutencaoSearchRepository, times(1)).save(testManutencao);
    }

    @Test
    @Transactional
    public void updateNonExistingManutencao() throws Exception {
        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();

        // Create the Manutencao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManutencaoMockMvc.perform(put("/api/manutencaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manutencao)))
            .andExpect(status().isBadRequest());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Manutencao in Elasticsearch
        verify(mockManutencaoSearchRepository, times(0)).save(manutencao);
    }

    @Test
    @Transactional
    public void deleteManutencao() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        int databaseSizeBeforeDelete = manutencaoRepository.findAll().size();

        // Get the manutencao
        restManutencaoMockMvc.perform(delete("/api/manutencaos/{id}", manutencao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Manutencao in Elasticsearch
        verify(mockManutencaoSearchRepository, times(1)).deleteById(manutencao.getId());
    }

    @Test
    @Transactional
    public void searchManutencao() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);
        when(mockManutencaoSearchRepository.search(queryStringQuery("id:" + manutencao.getId())))
            .thenReturn(Collections.singletonList(manutencao));
        // Search the manutencao
        restManutencaoMockMvc.perform(get("/api/_search/manutencaos?query=id:" + manutencao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manutencao.getId().intValue())))
            .andExpect(jsonPath("$.[*].estadoEquipamento").value(hasItem(DEFAULT_ESTADO_EQUIPAMENTO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manutencao.class);
        Manutencao manutencao1 = new Manutencao();
        manutencao1.setId(1L);
        Manutencao manutencao2 = new Manutencao();
        manutencao2.setId(manutencao1.getId());
        assertThat(manutencao1).isEqualTo(manutencao2);
        manutencao2.setId(2L);
        assertThat(manutencao1).isNotEqualTo(manutencao2);
        manutencao1.setId(null);
        assertThat(manutencao1).isNotEqualTo(manutencao2);
    }
}
