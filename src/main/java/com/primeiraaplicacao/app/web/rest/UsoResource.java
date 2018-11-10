package com.primeiraaplicacao.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.primeiraaplicacao.app.domain.Uso;
import com.primeiraaplicacao.app.repository.UsoRepository;
import com.primeiraaplicacao.app.repository.search.UsoSearchRepository;
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
 * REST controller for managing Uso.
 */
@RestController
@RequestMapping("/api")
public class UsoResource {

    private final Logger log = LoggerFactory.getLogger(UsoResource.class);

    private static final String ENTITY_NAME = "uso";

    private final UsoRepository usoRepository;

    private final UsoSearchRepository usoSearchRepository;

    public UsoResource(UsoRepository usoRepository, UsoSearchRepository usoSearchRepository) {
        this.usoRepository = usoRepository;
        this.usoSearchRepository = usoSearchRepository;
    }

    /**
     * POST  /usos : Create a new uso.
     *
     * @param uso the uso to create
     * @return the ResponseEntity with status 201 (Created) and with body the new uso, or with status 400 (Bad Request) if the uso has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/usos")
    @Timed
    public ResponseEntity<Uso> createUso(@RequestBody Uso uso) throws URISyntaxException {
        log.debug("REST request to save Uso : {}", uso);
        if (uso.getId() != null) {
            throw new BadRequestAlertException("A new uso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Uso result = usoRepository.save(uso);
        usoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/usos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /usos : Updates an existing uso.
     *
     * @param uso the uso to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated uso,
     * or with status 400 (Bad Request) if the uso is not valid,
     * or with status 500 (Internal Server Error) if the uso couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/usos")
    @Timed
    public ResponseEntity<Uso> updateUso(@RequestBody Uso uso) throws URISyntaxException {
        log.debug("REST request to update Uso : {}", uso);
        if (uso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Uso result = usoRepository.save(uso);
        usoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, uso.getId().toString()))
            .body(result);
    }

    /**
     * GET  /usos : get all the usos.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of usos in body
     */
    @GetMapping("/usos")
    @Timed
    public List<Uso> getAllUsos(@RequestParam(required = false) String filter) {
        if ("servidor-is-null".equals(filter)) {
            log.debug("REST request to get all Usos where servidor is null");
            return StreamSupport
                .stream(usoRepository.findAll().spliterator(), false)
                .filter(uso -> uso.getServidor() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Usos");
        return usoRepository.findAll();
    }

    /**
     * GET  /usos/:id : get the "id" uso.
     *
     * @param id the id of the uso to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the uso, or with status 404 (Not Found)
     */
    @GetMapping("/usos/{id}")
    @Timed
    public ResponseEntity<Uso> getUso(@PathVariable Long id) {
        log.debug("REST request to get Uso : {}", id);
        Optional<Uso> uso = usoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(uso);
    }

    /**
     * DELETE  /usos/:id : delete the "id" uso.
     *
     * @param id the id of the uso to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/usos/{id}")
    @Timed
    public ResponseEntity<Void> deleteUso(@PathVariable Long id) {
        log.debug("REST request to delete Uso : {}", id);

        usoRepository.deleteById(id);
        usoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/usos?query=:query : search for the uso corresponding
     * to the query.
     *
     * @param query the query of the uso search
     * @return the result of the search
     */
    @GetMapping("/_search/usos")
    @Timed
    public List<Uso> searchUsos(@RequestParam String query) {
        log.debug("REST request to search Usos for query {}", query);
        return StreamSupport
            .stream(usoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
