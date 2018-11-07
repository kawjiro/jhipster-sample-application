package com.primeiraaplicacao.app.web.rest;

import com.primeiraaplicacao.app.PrimeiraAplicacaoApp;

import com.primeiraaplicacao.app.domain.Equipamento;
import com.primeiraaplicacao.app.repository.EquipamentoRepository;
import com.primeiraaplicacao.app.repository.search.EquipamentoSearchRepository;
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

import com.primeiraaplicacao.app.domain.enumeration.StatusEquipamento;
/**
 * Test class for the EquipamentoResource REST controller.
 *
 * @see EquipamentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeiraAplicacaoApp.class)
public class EquipamentoResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_PATRIMONIO = 1;
    private static final Integer UPDATED_NUM_PATRIMONIO = 2;

    private static final StatusEquipamento DEFAULT_STATUS = StatusEquipamento.Funcionando;
    private static final StatusEquipamento UPDATED_STATUS = StatusEquipamento.Defeito;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    /**
     * This repository is mocked in the com.primeiraaplicacao.app.repository.search test package.
     *
     * @see com.primeiraaplicacao.app.repository.search.EquipamentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private EquipamentoSearchRepository mockEquipamentoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEquipamentoMockMvc;

    private Equipamento equipamento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EquipamentoResource equipamentoResource = new EquipamentoResource(equipamentoRepository, mockEquipamentoSearchRepository);
        this.restEquipamentoMockMvc = MockMvcBuilders.standaloneSetup(equipamentoResource)
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
    public static Equipamento createEntity(EntityManager em) {
        Equipamento equipamento = new Equipamento()
            .descricao(DEFAULT_DESCRICAO)
            .numPatrimonio(DEFAULT_NUM_PATRIMONIO)
            .status(DEFAULT_STATUS);
        return equipamento;
    }

    @Before
    public void initTest() {
        equipamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipamento() throws Exception {
        int databaseSizeBeforeCreate = equipamentoRepository.findAll().size();

        // Create the Equipamento
        restEquipamentoMockMvc.perform(post("/api/equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipamento)))
            .andExpect(status().isCreated());

        // Validate the Equipamento in the database
        List<Equipamento> equipamentoList = equipamentoRepository.findAll();
        assertThat(equipamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Equipamento testEquipamento = equipamentoList.get(equipamentoList.size() - 1);
        assertThat(testEquipamento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testEquipamento.getNumPatrimonio()).isEqualTo(DEFAULT_NUM_PATRIMONIO);
        assertThat(testEquipamento.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Equipamento in Elasticsearch
        verify(mockEquipamentoSearchRepository, times(1)).save(testEquipamento);
    }

    @Test
    @Transactional
    public void createEquipamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipamentoRepository.findAll().size();

        // Create the Equipamento with an existing ID
        equipamento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipamentoMockMvc.perform(post("/api/equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipamento)))
            .andExpect(status().isBadRequest());

        // Validate the Equipamento in the database
        List<Equipamento> equipamentoList = equipamentoRepository.findAll();
        assertThat(equipamentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Equipamento in Elasticsearch
        verify(mockEquipamentoSearchRepository, times(0)).save(equipamento);
    }

    @Test
    @Transactional
    public void getAllEquipamentos() throws Exception {
        // Initialize the database
        equipamentoRepository.saveAndFlush(equipamento);

        // Get all the equipamentoList
        restEquipamentoMockMvc.perform(get("/api/equipamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].numPatrimonio").value(hasItem(DEFAULT_NUM_PATRIMONIO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipamento() throws Exception {
        // Initialize the database
        equipamentoRepository.saveAndFlush(equipamento);

        // Get the equipamento
        restEquipamentoMockMvc.perform(get("/api/equipamentos/{id}", equipamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipamento.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.numPatrimonio").value(DEFAULT_NUM_PATRIMONIO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEquipamento() throws Exception {
        // Get the equipamento
        restEquipamentoMockMvc.perform(get("/api/equipamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipamento() throws Exception {
        // Initialize the database
        equipamentoRepository.saveAndFlush(equipamento);

        int databaseSizeBeforeUpdate = equipamentoRepository.findAll().size();

        // Update the equipamento
        Equipamento updatedEquipamento = equipamentoRepository.findById(equipamento.getId()).get();
        // Disconnect from session so that the updates on updatedEquipamento are not directly saved in db
        em.detach(updatedEquipamento);
        updatedEquipamento
            .descricao(UPDATED_DESCRICAO)
            .numPatrimonio(UPDATED_NUM_PATRIMONIO)
            .status(UPDATED_STATUS);

        restEquipamentoMockMvc.perform(put("/api/equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEquipamento)))
            .andExpect(status().isOk());

        // Validate the Equipamento in the database
        List<Equipamento> equipamentoList = equipamentoRepository.findAll();
        assertThat(equipamentoList).hasSize(databaseSizeBeforeUpdate);
        Equipamento testEquipamento = equipamentoList.get(equipamentoList.size() - 1);
        assertThat(testEquipamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testEquipamento.getNumPatrimonio()).isEqualTo(UPDATED_NUM_PATRIMONIO);
        assertThat(testEquipamento.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Equipamento in Elasticsearch
        verify(mockEquipamentoSearchRepository, times(1)).save(testEquipamento);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipamento() throws Exception {
        int databaseSizeBeforeUpdate = equipamentoRepository.findAll().size();

        // Create the Equipamento

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipamentoMockMvc.perform(put("/api/equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipamento)))
            .andExpect(status().isBadRequest());

        // Validate the Equipamento in the database
        List<Equipamento> equipamentoList = equipamentoRepository.findAll();
        assertThat(equipamentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Equipamento in Elasticsearch
        verify(mockEquipamentoSearchRepository, times(0)).save(equipamento);
    }

    @Test
    @Transactional
    public void deleteEquipamento() throws Exception {
        // Initialize the database
        equipamentoRepository.saveAndFlush(equipamento);

        int databaseSizeBeforeDelete = equipamentoRepository.findAll().size();

        // Get the equipamento
        restEquipamentoMockMvc.perform(delete("/api/equipamentos/{id}", equipamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Equipamento> equipamentoList = equipamentoRepository.findAll();
        assertThat(equipamentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Equipamento in Elasticsearch
        verify(mockEquipamentoSearchRepository, times(1)).deleteById(equipamento.getId());
    }

    @Test
    @Transactional
    public void searchEquipamento() throws Exception {
        // Initialize the database
        equipamentoRepository.saveAndFlush(equipamento);
        when(mockEquipamentoSearchRepository.search(queryStringQuery("id:" + equipamento.getId())))
            .thenReturn(Collections.singletonList(equipamento));
        // Search the equipamento
        restEquipamentoMockMvc.perform(get("/api/_search/equipamentos?query=id:" + equipamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].numPatrimonio").value(hasItem(DEFAULT_NUM_PATRIMONIO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipamento.class);
        Equipamento equipamento1 = new Equipamento();
        equipamento1.setId(1L);
        Equipamento equipamento2 = new Equipamento();
        equipamento2.setId(equipamento1.getId());
        assertThat(equipamento1).isEqualTo(equipamento2);
        equipamento2.setId(2L);
        assertThat(equipamento1).isNotEqualTo(equipamento2);
        equipamento1.setId(null);
        assertThat(equipamento1).isNotEqualTo(equipamento2);
    }
}
