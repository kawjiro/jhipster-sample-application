package com.primeiraaplicacao.app.web.rest;

import com.primeiraaplicacao.app.PrimeiraAplicacaoApp;

import com.primeiraaplicacao.app.domain.Servidor;
import com.primeiraaplicacao.app.repository.ServidorRepository;
import com.primeiraaplicacao.app.repository.search.ServidorSearchRepository;
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
 * Test class for the ServidorResource REST controller.
 *
 * @see ServidorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeiraAplicacaoApp.class)
public class ServidorResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_NUM_MATRICULA = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO = "AAAAAAAAAA";
    private static final String UPDATED_CARGO = "BBBBBBBBBB";

    @Autowired
    private ServidorRepository servidorRepository;

    /**
     * This repository is mocked in the com.primeiraaplicacao.app.repository.search test package.
     *
     * @see com.primeiraaplicacao.app.repository.search.ServidorSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServidorSearchRepository mockServidorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServidorMockMvc;

    private Servidor servidor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServidorResource servidorResource = new ServidorResource(servidorRepository, mockServidorSearchRepository);
        this.restServidorMockMvc = MockMvcBuilders.standaloneSetup(servidorResource)
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
    public static Servidor createEntity(EntityManager em) {
        Servidor servidor = new Servidor()
            .nome(DEFAULT_NOME)
            .numMatricula(DEFAULT_NUM_MATRICULA)
            .cargo(DEFAULT_CARGO);
        return servidor;
    }

    @Before
    public void initTest() {
        servidor = createEntity(em);
    }

    @Test
    @Transactional
    public void createServidor() throws Exception {
        int databaseSizeBeforeCreate = servidorRepository.findAll().size();

        // Create the Servidor
        restServidorMockMvc.perform(post("/api/servidors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servidor)))
            .andExpect(status().isCreated());

        // Validate the Servidor in the database
        List<Servidor> servidorList = servidorRepository.findAll();
        assertThat(servidorList).hasSize(databaseSizeBeforeCreate + 1);
        Servidor testServidor = servidorList.get(servidorList.size() - 1);
        assertThat(testServidor.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testServidor.getNumMatricula()).isEqualTo(DEFAULT_NUM_MATRICULA);
        assertThat(testServidor.getCargo()).isEqualTo(DEFAULT_CARGO);

        // Validate the Servidor in Elasticsearch
        verify(mockServidorSearchRepository, times(1)).save(testServidor);
    }

    @Test
    @Transactional
    public void createServidorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = servidorRepository.findAll().size();

        // Create the Servidor with an existing ID
        servidor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServidorMockMvc.perform(post("/api/servidors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servidor)))
            .andExpect(status().isBadRequest());

        // Validate the Servidor in the database
        List<Servidor> servidorList = servidorRepository.findAll();
        assertThat(servidorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Servidor in Elasticsearch
        verify(mockServidorSearchRepository, times(0)).save(servidor);
    }

    @Test
    @Transactional
    public void getAllServidors() throws Exception {
        // Initialize the database
        servidorRepository.saveAndFlush(servidor);

        // Get all the servidorList
        restServidorMockMvc.perform(get("/api/servidors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servidor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].numMatricula").value(hasItem(DEFAULT_NUM_MATRICULA.toString())))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO.toString())));
    }
    
    @Test
    @Transactional
    public void getServidor() throws Exception {
        // Initialize the database
        servidorRepository.saveAndFlush(servidor);

        // Get the servidor
        restServidorMockMvc.perform(get("/api/servidors/{id}", servidor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(servidor.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.numMatricula").value(DEFAULT_NUM_MATRICULA.toString()))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServidor() throws Exception {
        // Get the servidor
        restServidorMockMvc.perform(get("/api/servidors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServidor() throws Exception {
        // Initialize the database
        servidorRepository.saveAndFlush(servidor);

        int databaseSizeBeforeUpdate = servidorRepository.findAll().size();

        // Update the servidor
        Servidor updatedServidor = servidorRepository.findById(servidor.getId()).get();
        // Disconnect from session so that the updates on updatedServidor are not directly saved in db
        em.detach(updatedServidor);
        updatedServidor
            .nome(UPDATED_NOME)
            .numMatricula(UPDATED_NUM_MATRICULA)
            .cargo(UPDATED_CARGO);

        restServidorMockMvc.perform(put("/api/servidors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServidor)))
            .andExpect(status().isOk());

        // Validate the Servidor in the database
        List<Servidor> servidorList = servidorRepository.findAll();
        assertThat(servidorList).hasSize(databaseSizeBeforeUpdate);
        Servidor testServidor = servidorList.get(servidorList.size() - 1);
        assertThat(testServidor.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testServidor.getNumMatricula()).isEqualTo(UPDATED_NUM_MATRICULA);
        assertThat(testServidor.getCargo()).isEqualTo(UPDATED_CARGO);

        // Validate the Servidor in Elasticsearch
        verify(mockServidorSearchRepository, times(1)).save(testServidor);
    }

    @Test
    @Transactional
    public void updateNonExistingServidor() throws Exception {
        int databaseSizeBeforeUpdate = servidorRepository.findAll().size();

        // Create the Servidor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServidorMockMvc.perform(put("/api/servidors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servidor)))
            .andExpect(status().isBadRequest());

        // Validate the Servidor in the database
        List<Servidor> servidorList = servidorRepository.findAll();
        assertThat(servidorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Servidor in Elasticsearch
        verify(mockServidorSearchRepository, times(0)).save(servidor);
    }

    @Test
    @Transactional
    public void deleteServidor() throws Exception {
        // Initialize the database
        servidorRepository.saveAndFlush(servidor);

        int databaseSizeBeforeDelete = servidorRepository.findAll().size();

        // Get the servidor
        restServidorMockMvc.perform(delete("/api/servidors/{id}", servidor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Servidor> servidorList = servidorRepository.findAll();
        assertThat(servidorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Servidor in Elasticsearch
        verify(mockServidorSearchRepository, times(1)).deleteById(servidor.getId());
    }

    @Test
    @Transactional
    public void searchServidor() throws Exception {
        // Initialize the database
        servidorRepository.saveAndFlush(servidor);
        when(mockServidorSearchRepository.search(queryStringQuery("id:" + servidor.getId())))
            .thenReturn(Collections.singletonList(servidor));
        // Search the servidor
        restServidorMockMvc.perform(get("/api/_search/servidors?query=id:" + servidor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servidor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].numMatricula").value(hasItem(DEFAULT_NUM_MATRICULA)))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servidor.class);
        Servidor servidor1 = new Servidor();
        servidor1.setId(1L);
        Servidor servidor2 = new Servidor();
        servidor2.setId(servidor1.getId());
        assertThat(servidor1).isEqualTo(servidor2);
        servidor2.setId(2L);
        assertThat(servidor1).isNotEqualTo(servidor2);
        servidor1.setId(null);
        assertThat(servidor1).isNotEqualTo(servidor2);
    }
}
