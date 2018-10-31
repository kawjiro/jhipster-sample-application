package com.primeiraaplicacao.app.web.rest;

import com.primeiraaplicacao.app.PrimeiraAplicacaoApp;

import com.primeiraaplicacao.app.domain.ModeloEquipamento;
import com.primeiraaplicacao.app.repository.ModeloEquipamentoRepository;
import com.primeiraaplicacao.app.repository.search.ModeloEquipamentoSearchRepository;
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
 * Test class for the ModeloEquipamentoResource REST controller.
 *
 * @see ModeloEquipamentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeiraAplicacaoApp.class)
public class ModeloEquipamentoResourceIntTest {

    private static final String DEFAULT_NOME_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_MODELO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_PATRIMONIO = 1;
    private static final Integer UPDATED_NUM_PATRIMONIO = 2;

    @Autowired
    private ModeloEquipamentoRepository modeloEquipamentoRepository;

    /**
     * This repository is mocked in the com.primeiraaplicacao.app.repository.search test package.
     *
     * @see com.primeiraaplicacao.app.repository.search.ModeloEquipamentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ModeloEquipamentoSearchRepository mockModeloEquipamentoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restModeloEquipamentoMockMvc;

    private ModeloEquipamento modeloEquipamento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModeloEquipamentoResource modeloEquipamentoResource = new ModeloEquipamentoResource(modeloEquipamentoRepository, mockModeloEquipamentoSearchRepository);
        this.restModeloEquipamentoMockMvc = MockMvcBuilders.standaloneSetup(modeloEquipamentoResource)
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
    public static ModeloEquipamento createEntity(EntityManager em) {
        ModeloEquipamento modeloEquipamento = new ModeloEquipamento()
            .nomeModelo(DEFAULT_NOME_MODELO)
            .numPatrimonio(DEFAULT_NUM_PATRIMONIO);
        return modeloEquipamento;
    }

    @Before
    public void initTest() {
        modeloEquipamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createModeloEquipamento() throws Exception {
        int databaseSizeBeforeCreate = modeloEquipamentoRepository.findAll().size();

        // Create the ModeloEquipamento
        restModeloEquipamentoMockMvc.perform(post("/api/modelo-equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modeloEquipamento)))
            .andExpect(status().isCreated());

        // Validate the ModeloEquipamento in the database
        List<ModeloEquipamento> modeloEquipamentoList = modeloEquipamentoRepository.findAll();
        assertThat(modeloEquipamentoList).hasSize(databaseSizeBeforeCreate + 1);
        ModeloEquipamento testModeloEquipamento = modeloEquipamentoList.get(modeloEquipamentoList.size() - 1);
        assertThat(testModeloEquipamento.getNomeModelo()).isEqualTo(DEFAULT_NOME_MODELO);
        assertThat(testModeloEquipamento.getNumPatrimonio()).isEqualTo(DEFAULT_NUM_PATRIMONIO);

        // Validate the ModeloEquipamento in Elasticsearch
        verify(mockModeloEquipamentoSearchRepository, times(1)).save(testModeloEquipamento);
    }

    @Test
    @Transactional
    public void createModeloEquipamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modeloEquipamentoRepository.findAll().size();

        // Create the ModeloEquipamento with an existing ID
        modeloEquipamento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModeloEquipamentoMockMvc.perform(post("/api/modelo-equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modeloEquipamento)))
            .andExpect(status().isBadRequest());

        // Validate the ModeloEquipamento in the database
        List<ModeloEquipamento> modeloEquipamentoList = modeloEquipamentoRepository.findAll();
        assertThat(modeloEquipamentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the ModeloEquipamento in Elasticsearch
        verify(mockModeloEquipamentoSearchRepository, times(0)).save(modeloEquipamento);
    }

    @Test
    @Transactional
    public void getAllModeloEquipamentos() throws Exception {
        // Initialize the database
        modeloEquipamentoRepository.saveAndFlush(modeloEquipamento);

        // Get all the modeloEquipamentoList
        restModeloEquipamentoMockMvc.perform(get("/api/modelo-equipamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modeloEquipamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeModelo").value(hasItem(DEFAULT_NOME_MODELO.toString())))
            .andExpect(jsonPath("$.[*].numPatrimonio").value(hasItem(DEFAULT_NUM_PATRIMONIO)));
    }
    
    @Test
    @Transactional
    public void getModeloEquipamento() throws Exception {
        // Initialize the database
        modeloEquipamentoRepository.saveAndFlush(modeloEquipamento);

        // Get the modeloEquipamento
        restModeloEquipamentoMockMvc.perform(get("/api/modelo-equipamentos/{id}", modeloEquipamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modeloEquipamento.getId().intValue()))
            .andExpect(jsonPath("$.nomeModelo").value(DEFAULT_NOME_MODELO.toString()))
            .andExpect(jsonPath("$.numPatrimonio").value(DEFAULT_NUM_PATRIMONIO));
    }

    @Test
    @Transactional
    public void getNonExistingModeloEquipamento() throws Exception {
        // Get the modeloEquipamento
        restModeloEquipamentoMockMvc.perform(get("/api/modelo-equipamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModeloEquipamento() throws Exception {
        // Initialize the database
        modeloEquipamentoRepository.saveAndFlush(modeloEquipamento);

        int databaseSizeBeforeUpdate = modeloEquipamentoRepository.findAll().size();

        // Update the modeloEquipamento
        ModeloEquipamento updatedModeloEquipamento = modeloEquipamentoRepository.findById(modeloEquipamento.getId()).get();
        // Disconnect from session so that the updates on updatedModeloEquipamento are not directly saved in db
        em.detach(updatedModeloEquipamento);
        updatedModeloEquipamento
            .nomeModelo(UPDATED_NOME_MODELO)
            .numPatrimonio(UPDATED_NUM_PATRIMONIO);

        restModeloEquipamentoMockMvc.perform(put("/api/modelo-equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModeloEquipamento)))
            .andExpect(status().isOk());

        // Validate the ModeloEquipamento in the database
        List<ModeloEquipamento> modeloEquipamentoList = modeloEquipamentoRepository.findAll();
        assertThat(modeloEquipamentoList).hasSize(databaseSizeBeforeUpdate);
        ModeloEquipamento testModeloEquipamento = modeloEquipamentoList.get(modeloEquipamentoList.size() - 1);
        assertThat(testModeloEquipamento.getNomeModelo()).isEqualTo(UPDATED_NOME_MODELO);
        assertThat(testModeloEquipamento.getNumPatrimonio()).isEqualTo(UPDATED_NUM_PATRIMONIO);

        // Validate the ModeloEquipamento in Elasticsearch
        verify(mockModeloEquipamentoSearchRepository, times(1)).save(testModeloEquipamento);
    }

    @Test
    @Transactional
    public void updateNonExistingModeloEquipamento() throws Exception {
        int databaseSizeBeforeUpdate = modeloEquipamentoRepository.findAll().size();

        // Create the ModeloEquipamento

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeloEquipamentoMockMvc.perform(put("/api/modelo-equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modeloEquipamento)))
            .andExpect(status().isBadRequest());

        // Validate the ModeloEquipamento in the database
        List<ModeloEquipamento> modeloEquipamentoList = modeloEquipamentoRepository.findAll();
        assertThat(modeloEquipamentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ModeloEquipamento in Elasticsearch
        verify(mockModeloEquipamentoSearchRepository, times(0)).save(modeloEquipamento);
    }

    @Test
    @Transactional
    public void deleteModeloEquipamento() throws Exception {
        // Initialize the database
        modeloEquipamentoRepository.saveAndFlush(modeloEquipamento);

        int databaseSizeBeforeDelete = modeloEquipamentoRepository.findAll().size();

        // Get the modeloEquipamento
        restModeloEquipamentoMockMvc.perform(delete("/api/modelo-equipamentos/{id}", modeloEquipamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ModeloEquipamento> modeloEquipamentoList = modeloEquipamentoRepository.findAll();
        assertThat(modeloEquipamentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ModeloEquipamento in Elasticsearch
        verify(mockModeloEquipamentoSearchRepository, times(1)).deleteById(modeloEquipamento.getId());
    }

    @Test
    @Transactional
    public void searchModeloEquipamento() throws Exception {
        // Initialize the database
        modeloEquipamentoRepository.saveAndFlush(modeloEquipamento);
        when(mockModeloEquipamentoSearchRepository.search(queryStringQuery("id:" + modeloEquipamento.getId())))
            .thenReturn(Collections.singletonList(modeloEquipamento));
        // Search the modeloEquipamento
        restModeloEquipamentoMockMvc.perform(get("/api/_search/modelo-equipamentos?query=id:" + modeloEquipamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modeloEquipamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeModelo").value(hasItem(DEFAULT_NOME_MODELO.toString())))
            .andExpect(jsonPath("$.[*].numPatrimonio").value(hasItem(DEFAULT_NUM_PATRIMONIO)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModeloEquipamento.class);
        ModeloEquipamento modeloEquipamento1 = new ModeloEquipamento();
        modeloEquipamento1.setId(1L);
        ModeloEquipamento modeloEquipamento2 = new ModeloEquipamento();
        modeloEquipamento2.setId(modeloEquipamento1.getId());
        assertThat(modeloEquipamento1).isEqualTo(modeloEquipamento2);
        modeloEquipamento2.setId(2L);
        assertThat(modeloEquipamento1).isNotEqualTo(modeloEquipamento2);
        modeloEquipamento1.setId(null);
        assertThat(modeloEquipamento1).isNotEqualTo(modeloEquipamento2);
    }
}
