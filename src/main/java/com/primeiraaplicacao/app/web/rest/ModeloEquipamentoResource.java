package com.primeiraaplicacao.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.primeiraaplicacao.app.domain.ModeloEquipamento;
import com.primeiraaplicacao.app.repository.ModeloEquipamentoRepository;
import com.primeiraaplicacao.app.repository.search.ModeloEquipamentoSearchRepository;
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
 * REST controller for managing ModeloEquipamento.
 */
@RestController
@RequestMapping("/api")
public class ModeloEquipamentoResource {

    private final Logger log = LoggerFactory.getLogger(ModeloEquipamentoResource.class);

    private static final String ENTITY_NAME = "modeloEquipamento";

    private ModeloEquipamentoRepository modeloEquipamentoRepository;

    private ModeloEquipamentoSearchRepository modeloEquipamentoSearchRepository;

    public ModeloEquipamentoResource(ModeloEquipamentoRepository modeloEquipamentoRepository, ModeloEquipamentoSearchRepository modeloEquipamentoSearchRepository) {
        this.modeloEquipamentoRepository = modeloEquipamentoRepository;
        this.modeloEquipamentoSearchRepository = modeloEquipamentoSearchRepository;
    }

    /**
     * POST  /modelo-equipamentos : Create a new modeloEquipamento.
     *
     * @param modeloEquipamento the modeloEquipamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modeloEquipamento, or with status 400 (Bad Request) if the modeloEquipamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/modelo-equipamentos")
    @Timed
    public ResponseEntity<ModeloEquipamento> createModeloEquipamento(@RequestBody ModeloEquipamento modeloEquipamento) throws URISyntaxException {
        log.debug("REST request to save ModeloEquipamento : {}", modeloEquipamento);
        if (modeloEquipamento.getId() != null) {
            throw new BadRequestAlertException("A new modeloEquipamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModeloEquipamento result = modeloEquipamentoRepository.save(modeloEquipamento);
        modeloEquipamentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/modelo-equipamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /modelo-equipamentos : Updates an existing modeloEquipamento.
     *
     * @param modeloEquipamento the modeloEquipamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modeloEquipamento,
     * or with status 400 (Bad Request) if the modeloEquipamento is not valid,
     * or with status 500 (Internal Server Error) if the modeloEquipamento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/modelo-equipamentos")
    @Timed
    public ResponseEntity<ModeloEquipamento> updateModeloEquipamento(@RequestBody ModeloEquipamento modeloEquipamento) throws URISyntaxException {
        log.debug("REST request to update ModeloEquipamento : {}", modeloEquipamento);
        if (modeloEquipamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ModeloEquipamento result = modeloEquipamentoRepository.save(modeloEquipamento);
        modeloEquipamentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, modeloEquipamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /modelo-equipamentos : get all the modeloEquipamentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of modeloEquipamentos in body
     */
    @GetMapping("/modelo-equipamentos")
    @Timed
    public List<ModeloEquipamento> getAllModeloEquipamentos() {
        log.debug("REST request to get all ModeloEquipamentos");
        return modeloEquipamentoRepository.findAll();
    }

    /**
     * GET  /modelo-equipamentos/:id : get the "id" modeloEquipamento.
     *
     * @param id the id of the modeloEquipamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modeloEquipamento, or with status 404 (Not Found)
     */
    @GetMapping("/modelo-equipamentos/{id}")
    @Timed
    public ResponseEntity<ModeloEquipamento> getModeloEquipamento(@PathVariable Long id) {
        log.debug("REST request to get ModeloEquipamento : {}", id);
        Optional<ModeloEquipamento> modeloEquipamento = modeloEquipamentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(modeloEquipamento);
    }

    /**
     * DELETE  /modelo-equipamentos/:id : delete the "id" modeloEquipamento.
     *
     * @param id the id of the modeloEquipamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/modelo-equipamentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteModeloEquipamento(@PathVariable Long id) {
        log.debug("REST request to delete ModeloEquipamento : {}", id);

        modeloEquipamentoRepository.deleteById(id);
        modeloEquipamentoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/modelo-equipamentos?query=:query : search for the modeloEquipamento corresponding
     * to the query.
     *
     * @param query the query of the modeloEquipamento search
     * @return the result of the search
     */
    @GetMapping("/_search/modelo-equipamentos")
    @Timed
    public List<ModeloEquipamento> searchModeloEquipamentos(@RequestParam String query) {
        log.debug("REST request to search ModeloEquipamentos for query {}", query);
        return StreamSupport
            .stream(modeloEquipamentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
