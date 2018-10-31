package com.primeiraaplicacao.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.primeiraaplicacao.app.domain.Equipamento;
import com.primeiraaplicacao.app.repository.EquipamentoRepository;
import com.primeiraaplicacao.app.repository.search.EquipamentoSearchRepository;
import com.primeiraaplicacao.app.web.rest.errors.BadRequestAlertException;
import com.primeiraaplicacao.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Equipamento.
 */
@RestController
@RequestMapping("/api")
public class EquipamentoResource {

    private final Logger log = LoggerFactory.getLogger(EquipamentoResource.class);

    private static final String ENTITY_NAME = "equipamento";

    private EquipamentoRepository equipamentoRepository;

    private EquipamentoSearchRepository equipamentoSearchRepository;

    public EquipamentoResource(EquipamentoRepository equipamentoRepository, EquipamentoSearchRepository equipamentoSearchRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.equipamentoSearchRepository = equipamentoSearchRepository;
    }

    /**
     * POST  /equipamentos : Create a new equipamento.
     *
     * @param equipamento the equipamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipamento, or with status 400 (Bad Request) if the equipamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/equipamentos")
    @Timed
    public ResponseEntity<Equipamento> createEquipamento(@RequestBody Equipamento equipamento) throws URISyntaxException {
        log.debug("REST request to save Equipamento : {}", equipamento);
        if (equipamento.getId() != null) {
            throw new BadRequestAlertException("A new equipamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Equipamento result = equipamentoRepository.save(equipamento);
        equipamentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/equipamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipamentos : Updates an existing equipamento.
     *
     * @param equipamento the equipamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipamento,
     * or with status 400 (Bad Request) if the equipamento is not valid,
     * or with status 500 (Internal Server Error) if the equipamento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/equipamentos")
    @Timed
    public ResponseEntity<Equipamento> updateEquipamento(@RequestBody Equipamento equipamento) throws URISyntaxException {
        log.debug("REST request to update Equipamento : {}", equipamento);
        if (equipamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Equipamento result = equipamentoRepository.save(equipamento);
        equipamentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, equipamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipamentos : get all the equipamentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of equipamentos in body
     */
    @GetMapping("/equipamentos")
    @Timed
    public List<Equipamento> getAllEquipamentos() {
        log.debug("REST request to get all Equipamentos");
        return equipamentoRepository.findAll();
    }

    /**
     * GET  /equipamentos/:id : get the "id" equipamento.
     *
     * @param id the id of the equipamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipamento, or with status 404 (Not Found)
     */
    @GetMapping("/equipamentos/{id}")
    @Timed
    public ResponseEntity<Equipamento> getEquipamento(@PathVariable Long id) {
        log.debug("REST request to get Equipamento : {}", id);
        Optional<Equipamento> equipamento = equipamentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(equipamento);
    }

    /**
     * DELETE  /equipamentos/:id : delete the "id" equipamento.
     *
     * @param id the id of the equipamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/equipamentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteEquipamento(@PathVariable Long id) {
        log.debug("REST request to delete Equipamento : {}", id);

        equipamentoRepository.deleteById(id);
        equipamentoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/equipamentos?query=:query : search for the equipamento corresponding
     * to the query.
     *
     * @param query the query of the equipamento search
     * @return the result of the search
     */
    @GetMapping("/_search/equipamentos")
    @Timed
    public List<Equipamento> searchEquipamentos(@RequestParam String query) {
        log.debug("REST request to search Equipamentos for query {}", query);
        return StreamSupport
            .stream(equipamentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
