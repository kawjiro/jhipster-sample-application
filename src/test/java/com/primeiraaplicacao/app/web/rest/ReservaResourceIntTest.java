package com.primeiraaplicacao.app.web.rest;

import com.primeiraaplicacao.app.PrimeiraAplicacaoApp;

import com.primeiraaplicacao.app.domain.Reserva;
import com.primeiraaplicacao.app.repository.ReservaRepository;
import com.primeiraaplicacao.app.repository.search.ReservaSearchRepository;
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
 * Test class for the ReservaResource REST controller.
 *
 * @see ReservaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeiraAplicacaoApp.class)
public class ReservaResourceIntTest {

    private static final LocalDate DEFAULT_DATA_HORA_RESERVA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_HORA_RESERVA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_HORA_RETIRDA_PREV = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_HORA_RETIRDA_PREV = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_HORA_DEVOLUCAO_PREV = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_HORA_DEVOLUCAO_PREV = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * This repository is mocked in the com.primeiraaplicacao.app.repository.search test package.
     *
     * @see com.primeiraaplicacao.app.repository.search.ReservaSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReservaSearchRepository mockReservaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReservaMockMvc;

    private Reserva reserva;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservaResource reservaResource = new ReservaResource(reservaRepository, mockReservaSearchRepository);
        this.restReservaMockMvc = MockMvcBuilders.standaloneSetup(reservaResource)
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
    public static Reserva createEntity(EntityManager em) {
        Reserva reserva = new Reserva()
            .dataHoraReserva(DEFAULT_DATA_HORA_RESERVA)
            .dataHoraRetirdaPrev(DEFAULT_DATA_HORA_RETIRDA_PREV)
            .dataHoraDevolucaoPrev(DEFAULT_DATA_HORA_DEVOLUCAO_PREV);
        return reserva;
    }

    @Before
    public void initTest() {
        reserva = createEntity(em);
    }

    @Test
    @Transactional
    public void createReserva() throws Exception {
        int databaseSizeBeforeCreate = reservaRepository.findAll().size();

        // Create the Reserva
        restReservaMockMvc.perform(post("/api/reservas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isCreated());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate + 1);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getDataHoraReserva()).isEqualTo(DEFAULT_DATA_HORA_RESERVA);
        assertThat(testReserva.getDataHoraRetirdaPrev()).isEqualTo(DEFAULT_DATA_HORA_RETIRDA_PREV);
        assertThat(testReserva.getDataHoraDevolucaoPrev()).isEqualTo(DEFAULT_DATA_HORA_DEVOLUCAO_PREV);

        // Validate the Reserva in Elasticsearch
        verify(mockReservaSearchRepository, times(1)).save(testReserva);
    }

    @Test
    @Transactional
    public void createReservaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservaRepository.findAll().size();

        // Create the Reserva with an existing ID
        reserva.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservaMockMvc.perform(post("/api/reservas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Reserva in Elasticsearch
        verify(mockReservaSearchRepository, times(0)).save(reserva);
    }

    @Test
    @Transactional
    public void getAllReservas() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList
        restReservaMockMvc.perform(get("/api/reservas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserva.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraReserva").value(hasItem(DEFAULT_DATA_HORA_RESERVA.toString())))
            .andExpect(jsonPath("$.[*].dataHoraRetirdaPrev").value(hasItem(DEFAULT_DATA_HORA_RETIRDA_PREV.toString())))
            .andExpect(jsonPath("$.[*].dataHoraDevolucaoPrev").value(hasItem(DEFAULT_DATA_HORA_DEVOLUCAO_PREV.toString())));
    }
    
    @Test
    @Transactional
    public void getReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get the reserva
        restReservaMockMvc.perform(get("/api/reservas/{id}", reserva.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reserva.getId().intValue()))
            .andExpect(jsonPath("$.dataHoraReserva").value(DEFAULT_DATA_HORA_RESERVA.toString()))
            .andExpect(jsonPath("$.dataHoraRetirdaPrev").value(DEFAULT_DATA_HORA_RETIRDA_PREV.toString()))
            .andExpect(jsonPath("$.dataHoraDevolucaoPrev").value(DEFAULT_DATA_HORA_DEVOLUCAO_PREV.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReserva() throws Exception {
        // Get the reserva
        restReservaMockMvc.perform(get("/api/reservas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Update the reserva
        Reserva updatedReserva = reservaRepository.findById(reserva.getId()).get();
        // Disconnect from session so that the updates on updatedReserva are not directly saved in db
        em.detach(updatedReserva);
        updatedReserva
            .dataHoraReserva(UPDATED_DATA_HORA_RESERVA)
            .dataHoraRetirdaPrev(UPDATED_DATA_HORA_RETIRDA_PREV)
            .dataHoraDevolucaoPrev(UPDATED_DATA_HORA_DEVOLUCAO_PREV);

        restReservaMockMvc.perform(put("/api/reservas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReserva)))
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getDataHoraReserva()).isEqualTo(UPDATED_DATA_HORA_RESERVA);
        assertThat(testReserva.getDataHoraRetirdaPrev()).isEqualTo(UPDATED_DATA_HORA_RETIRDA_PREV);
        assertThat(testReserva.getDataHoraDevolucaoPrev()).isEqualTo(UPDATED_DATA_HORA_DEVOLUCAO_PREV);

        // Validate the Reserva in Elasticsearch
        verify(mockReservaSearchRepository, times(1)).save(testReserva);
    }

    @Test
    @Transactional
    public void updateNonExistingReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Create the Reserva

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservaMockMvc.perform(put("/api/reservas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reserva in Elasticsearch
        verify(mockReservaSearchRepository, times(0)).save(reserva);
    }

    @Test
    @Transactional
    public void deleteReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeDelete = reservaRepository.findAll().size();

        // Get the reserva
        restReservaMockMvc.perform(delete("/api/reservas/{id}", reserva.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Reserva in Elasticsearch
        verify(mockReservaSearchRepository, times(1)).deleteById(reserva.getId());
    }

    @Test
    @Transactional
    public void searchReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);
        when(mockReservaSearchRepository.search(queryStringQuery("id:" + reserva.getId())))
            .thenReturn(Collections.singletonList(reserva));
        // Search the reserva
        restReservaMockMvc.perform(get("/api/_search/reservas?query=id:" + reserva.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserva.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraReserva").value(hasItem(DEFAULT_DATA_HORA_RESERVA.toString())))
            .andExpect(jsonPath("$.[*].dataHoraRetirdaPrev").value(hasItem(DEFAULT_DATA_HORA_RETIRDA_PREV.toString())))
            .andExpect(jsonPath("$.[*].dataHoraDevolucaoPrev").value(hasItem(DEFAULT_DATA_HORA_DEVOLUCAO_PREV.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reserva.class);
        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);
        Reserva reserva2 = new Reserva();
        reserva2.setId(reserva1.getId());
        assertThat(reserva1).isEqualTo(reserva2);
        reserva2.setId(2L);
        assertThat(reserva1).isNotEqualTo(reserva2);
        reserva1.setId(null);
        assertThat(reserva1).isNotEqualTo(reserva2);
    }
}
