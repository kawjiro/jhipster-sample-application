package com.primeiraaplicacao.app.web.rest;

import com.primeiraaplicacao.app.PrimeiraAplicacaoApp;

import com.primeiraaplicacao.app.domain.OrgaoPublico;
import com.primeiraaplicacao.app.repository.OrgaoPublicoRepository;
import com.primeiraaplicacao.app.repository.search.OrgaoPublicoSearchRepository;
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
 * Test class for the OrgaoPublicoResource REST controller.
 *
 * @see OrgaoPublicoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeiraAplicacaoApp.class)
public class OrgaoPublicoResourceIntTest {

    private static final String DEFAULT_NOME_ORGAO_PUBLICO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_ORGAO_PUBLICO = "BBBBBBBBBB";

    @Autowired
    private OrgaoPublicoRepository orgaoPublicoRepository;

    /**
     * This repository is mocked in the com.primeiraaplicacao.app.repository.search test package.
     *
     * @see com.primeiraaplicacao.app.repository.search.OrgaoPublicoSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrgaoPublicoSearchRepository mockOrgaoPublicoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrgaoPublicoMockMvc;

    private OrgaoPublico orgaoPublico;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrgaoPublicoResource orgaoPublicoResource = new OrgaoPublicoResource(orgaoPublicoRepository, mockOrgaoPublicoSearchRepository);
        this.restOrgaoPublicoMockMvc = MockMvcBuilders.standaloneSetup(orgaoPublicoResource)
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
    public static OrgaoPublico createEntity(EntityManager em) {
        OrgaoPublico orgaoPublico = new OrgaoPublico()
            .nomeOrgaoPublico(DEFAULT_NOME_ORGAO_PUBLICO);
        return orgaoPublico;
    }

    @Before
    public void initTest() {
        orgaoPublico = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrgaoPublico() throws Exception {
        int databaseSizeBeforeCreate = orgaoPublicoRepository.findAll().size();

        // Create the OrgaoPublico
        restOrgaoPublicoMockMvc.perform(post("/api/orgao-publicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgaoPublico)))
            .andExpect(status().isCreated());

        // Validate the OrgaoPublico in the database
        List<OrgaoPublico> orgaoPublicoList = orgaoPublicoRepository.findAll();
        assertThat(orgaoPublicoList).hasSize(databaseSizeBeforeCreate + 1);
        OrgaoPublico testOrgaoPublico = orgaoPublicoList.get(orgaoPublicoList.size() - 1);
        assertThat(testOrgaoPublico.getNomeOrgaoPublico()).isEqualTo(DEFAULT_NOME_ORGAO_PUBLICO);

        // Validate the OrgaoPublico in Elasticsearch
        verify(mockOrgaoPublicoSearchRepository, times(1)).save(testOrgaoPublico);
    }

    @Test
    @Transactional
    public void createOrgaoPublicoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orgaoPublicoRepository.findAll().size();

        // Create the OrgaoPublico with an existing ID
        orgaoPublico.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgaoPublicoMockMvc.perform(post("/api/orgao-publicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgaoPublico)))
            .andExpect(status().isBadRequest());

        // Validate the OrgaoPublico in the database
        List<OrgaoPublico> orgaoPublicoList = orgaoPublicoRepository.findAll();
        assertThat(orgaoPublicoList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrgaoPublico in Elasticsearch
        verify(mockOrgaoPublicoSearchRepository, times(0)).save(orgaoPublico);
    }

    @Test
    @Transactional
    public void getAllOrgaoPublicos() throws Exception {
        // Initialize the database
        orgaoPublicoRepository.saveAndFlush(orgaoPublico);

        // Get all the orgaoPublicoList
        restOrgaoPublicoMockMvc.perform(get("/api/orgao-publicos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgaoPublico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeOrgaoPublico").value(hasItem(DEFAULT_NOME_ORGAO_PUBLICO.toString())));
    }
    
    @Test
    @Transactional
    public void getOrgaoPublico() throws Exception {
        // Initialize the database
        orgaoPublicoRepository.saveAndFlush(orgaoPublico);

        // Get the orgaoPublico
        restOrgaoPublicoMockMvc.perform(get("/api/orgao-publicos/{id}", orgaoPublico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orgaoPublico.getId().intValue()))
            .andExpect(jsonPath("$.nomeOrgaoPublico").value(DEFAULT_NOME_ORGAO_PUBLICO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrgaoPublico() throws Exception {
        // Get the orgaoPublico
        restOrgaoPublicoMockMvc.perform(get("/api/orgao-publicos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrgaoPublico() throws Exception {
        // Initialize the database
        orgaoPublicoRepository.saveAndFlush(orgaoPublico);

        int databaseSizeBeforeUpdate = orgaoPublicoRepository.findAll().size();

        // Update the orgaoPublico
        OrgaoPublico updatedOrgaoPublico = orgaoPublicoRepository.findById(orgaoPublico.getId()).get();
        // Disconnect from session so that the updates on updatedOrgaoPublico are not directly saved in db
        em.detach(updatedOrgaoPublico);
        updatedOrgaoPublico
            .nomeOrgaoPublico(UPDATED_NOME_ORGAO_PUBLICO);

        restOrgaoPublicoMockMvc.perform(put("/api/orgao-publicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrgaoPublico)))
            .andExpect(status().isOk());

        // Validate the OrgaoPublico in the database
        List<OrgaoPublico> orgaoPublicoList = orgaoPublicoRepository.findAll();
        assertThat(orgaoPublicoList).hasSize(databaseSizeBeforeUpdate);
        OrgaoPublico testOrgaoPublico = orgaoPublicoList.get(orgaoPublicoList.size() - 1);
        assertThat(testOrgaoPublico.getNomeOrgaoPublico()).isEqualTo(UPDATED_NOME_ORGAO_PUBLICO);

        // Validate the OrgaoPublico in Elasticsearch
        verify(mockOrgaoPublicoSearchRepository, times(1)).save(testOrgaoPublico);
    }

    @Test
    @Transactional
    public void updateNonExistingOrgaoPublico() throws Exception {
        int databaseSizeBeforeUpdate = orgaoPublicoRepository.findAll().size();

        // Create the OrgaoPublico

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgaoPublicoMockMvc.perform(put("/api/orgao-publicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgaoPublico)))
            .andExpect(status().isBadRequest());

        // Validate the OrgaoPublico in the database
        List<OrgaoPublico> orgaoPublicoList = orgaoPublicoRepository.findAll();
        assertThat(orgaoPublicoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgaoPublico in Elasticsearch
        verify(mockOrgaoPublicoSearchRepository, times(0)).save(orgaoPublico);
    }

    @Test
    @Transactional
    public void deleteOrgaoPublico() throws Exception {
        // Initialize the database
        orgaoPublicoRepository.saveAndFlush(orgaoPublico);

        int databaseSizeBeforeDelete = orgaoPublicoRepository.findAll().size();

        // Get the orgaoPublico
        restOrgaoPublicoMockMvc.perform(delete("/api/orgao-publicos/{id}", orgaoPublico.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrgaoPublico> orgaoPublicoList = orgaoPublicoRepository.findAll();
        assertThat(orgaoPublicoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrgaoPublico in Elasticsearch
        verify(mockOrgaoPublicoSearchRepository, times(1)).deleteById(orgaoPublico.getId());
    }

    @Test
    @Transactional
    public void searchOrgaoPublico() throws Exception {
        // Initialize the database
        orgaoPublicoRepository.saveAndFlush(orgaoPublico);
        when(mockOrgaoPublicoSearchRepository.search(queryStringQuery("id:" + orgaoPublico.getId())))
            .thenReturn(Collections.singletonList(orgaoPublico));
        // Search the orgaoPublico
        restOrgaoPublicoMockMvc.perform(get("/api/_search/orgao-publicos?query=id:" + orgaoPublico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgaoPublico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeOrgaoPublico").value(hasItem(DEFAULT_NOME_ORGAO_PUBLICO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgaoPublico.class);
        OrgaoPublico orgaoPublico1 = new OrgaoPublico();
        orgaoPublico1.setId(1L);
        OrgaoPublico orgaoPublico2 = new OrgaoPublico();
        orgaoPublico2.setId(orgaoPublico1.getId());
        assertThat(orgaoPublico1).isEqualTo(orgaoPublico2);
        orgaoPublico2.setId(2L);
        assertThat(orgaoPublico1).isNotEqualTo(orgaoPublico2);
        orgaoPublico1.setId(null);
        assertThat(orgaoPublico1).isNotEqualTo(orgaoPublico2);
    }
}
