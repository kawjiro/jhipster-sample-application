package com.primeiraaplicacao.app.web.rest;

import com.primeiraaplicacao.app.PrimeiraAplicacaoApp;

import com.primeiraaplicacao.app.domain.Justificativa;
import com.primeiraaplicacao.app.repository.JustificativaRepository;
import com.primeiraaplicacao.app.repository.search.JustificativaSearchRepository;
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
 * Test class for the JustificativaResource REST controller.
 *
 * @see JustificativaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeiraAplicacaoApp.class)
public class JustificativaResourceIntTest {

    private static final String DEFAULT_DESCRICAO_ATIVIDADE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO_ATIVIDADE = "BBBBBBBBBB";

    @Autowired
    private JustificativaRepository justificativaRepository;

    /**
     * This repository is mocked in the com.primeiraaplicacao.app.repository.search test package.
     *
     * @see com.primeiraaplicacao.app.repository.search.JustificativaSearchRepositoryMockConfiguration
     */
    @Autowired
    private JustificativaSearchRepository mockJustificativaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJustificativaMockMvc;

    private Justificativa justificativa;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JustificativaResource justificativaResource = new JustificativaResource(justificativaRepository, mockJustificativaSearchRepository);
        this.restJustificativaMockMvc = MockMvcBuilders.standaloneSetup(justificativaResource)
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
    public static Justificativa createEntity(EntityManager em) {
        Justificativa justificativa = new Justificativa()
            .descricaoAtividade(DEFAULT_DESCRICAO_ATIVIDADE);
        return justificativa;
    }

    @Before
    public void initTest() {
        justificativa = createEntity(em);
    }

    @Test
    @Transactional
    public void createJustificativa() throws Exception {
        int databaseSizeBeforeCreate = justificativaRepository.findAll().size();

        // Create the Justificativa
        restJustificativaMockMvc.perform(post("/api/justificativas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(justificativa)))
            .andExpect(status().isCreated());

        // Validate the Justificativa in the database
        List<Justificativa> justificativaList = justificativaRepository.findAll();
        assertThat(justificativaList).hasSize(databaseSizeBeforeCreate + 1);
        Justificativa testJustificativa = justificativaList.get(justificativaList.size() - 1);
        assertThat(testJustificativa.getDescricaoAtividade()).isEqualTo(DEFAULT_DESCRICAO_ATIVIDADE);

        // Validate the Justificativa in Elasticsearch
        verify(mockJustificativaSearchRepository, times(1)).save(testJustificativa);
    }

    @Test
    @Transactional
    public void createJustificativaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = justificativaRepository.findAll().size();

        // Create the Justificativa with an existing ID
        justificativa.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJustificativaMockMvc.perform(post("/api/justificativas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(justificativa)))
            .andExpect(status().isBadRequest());

        // Validate the Justificativa in the database
        List<Justificativa> justificativaList = justificativaRepository.findAll();
        assertThat(justificativaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Justificativa in Elasticsearch
        verify(mockJustificativaSearchRepository, times(0)).save(justificativa);
    }

    @Test
    @Transactional
    public void getAllJustificativas() throws Exception {
        // Initialize the database
        justificativaRepository.saveAndFlush(justificativa);

        // Get all the justificativaList
        restJustificativaMockMvc.perform(get("/api/justificativas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(justificativa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricaoAtividade").value(hasItem(DEFAULT_DESCRICAO_ATIVIDADE.toString())));
    }
    
    @Test
    @Transactional
    public void getJustificativa() throws Exception {
        // Initialize the database
        justificativaRepository.saveAndFlush(justificativa);

        // Get the justificativa
        restJustificativaMockMvc.perform(get("/api/justificativas/{id}", justificativa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(justificativa.getId().intValue()))
            .andExpect(jsonPath("$.descricaoAtividade").value(DEFAULT_DESCRICAO_ATIVIDADE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJustificativa() throws Exception {
        // Get the justificativa
        restJustificativaMockMvc.perform(get("/api/justificativas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJustificativa() throws Exception {
        // Initialize the database
        justificativaRepository.saveAndFlush(justificativa);

        int databaseSizeBeforeUpdate = justificativaRepository.findAll().size();

        // Update the justificativa
        Justificativa updatedJustificativa = justificativaRepository.findById(justificativa.getId()).get();
        // Disconnect from session so that the updates on updatedJustificativa are not directly saved in db
        em.detach(updatedJustificativa);
        updatedJustificativa
            .descricaoAtividade(UPDATED_DESCRICAO_ATIVIDADE);

        restJustificativaMockMvc.perform(put("/api/justificativas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJustificativa)))
            .andExpect(status().isOk());

        // Validate the Justificativa in the database
        List<Justificativa> justificativaList = justificativaRepository.findAll();
        assertThat(justificativaList).hasSize(databaseSizeBeforeUpdate);
        Justificativa testJustificativa = justificativaList.get(justificativaList.size() - 1);
        assertThat(testJustificativa.getDescricaoAtividade()).isEqualTo(UPDATED_DESCRICAO_ATIVIDADE);

        // Validate the Justificativa in Elasticsearch
        verify(mockJustificativaSearchRepository, times(1)).save(testJustificativa);
    }

    @Test
    @Transactional
    public void updateNonExistingJustificativa() throws Exception {
        int databaseSizeBeforeUpdate = justificativaRepository.findAll().size();

        // Create the Justificativa

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJustificativaMockMvc.perform(put("/api/justificativas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(justificativa)))
            .andExpect(status().isBadRequest());

        // Validate the Justificativa in the database
        List<Justificativa> justificativaList = justificativaRepository.findAll();
        assertThat(justificativaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Justificativa in Elasticsearch
        verify(mockJustificativaSearchRepository, times(0)).save(justificativa);
    }

    @Test
    @Transactional
    public void deleteJustificativa() throws Exception {
        // Initialize the database
        justificativaRepository.saveAndFlush(justificativa);

        int databaseSizeBeforeDelete = justificativaRepository.findAll().size();

        // Get the justificativa
        restJustificativaMockMvc.perform(delete("/api/justificativas/{id}", justificativa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Justificativa> justificativaList = justificativaRepository.findAll();
        assertThat(justificativaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Justificativa in Elasticsearch
        verify(mockJustificativaSearchRepository, times(1)).deleteById(justificativa.getId());
    }

    @Test
    @Transactional
    public void searchJustificativa() throws Exception {
        // Initialize the database
        justificativaRepository.saveAndFlush(justificativa);
        when(mockJustificativaSearchRepository.search(queryStringQuery("id:" + justificativa.getId())))
            .thenReturn(Collections.singletonList(justificativa));
        // Search the justificativa
        restJustificativaMockMvc.perform(get("/api/_search/justificativas?query=id:" + justificativa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(justificativa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricaoAtividade").value(hasItem(DEFAULT_DESCRICAO_ATIVIDADE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Justificativa.class);
        Justificativa justificativa1 = new Justificativa();
        justificativa1.setId(1L);
        Justificativa justificativa2 = new Justificativa();
        justificativa2.setId(justificativa1.getId());
        assertThat(justificativa1).isEqualTo(justificativa2);
        justificativa2.setId(2L);
        assertThat(justificativa1).isNotEqualTo(justificativa2);
        justificativa1.setId(null);
        assertThat(justificativa1).isNotEqualTo(justificativa2);
    }
}
