package com.primeiraaplicacao.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.primeiraaplicacao.app.domain.Justificativa;
import com.primeiraaplicacao.app.repository.JustificativaRepository;
import com.primeiraaplicacao.app.repository.search.JustificativaSearchRepository;
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
 * REST controller for managing Justificativa.
 */
@RestController
@RequestMapping("/api")
public class JustificativaResource {

    private final Logger log = LoggerFactory.getLogger(JustificativaResource.class);

    private static final String ENTITY_NAME = "justificativa";

    private JustificativaRepository justificativaRepository;

    private JustificativaSearchRepository justificativaSearchRepository;

    public JustificativaResource(JustificativaRepository justificativaRepository, JustificativaSearchRepository justificativaSearchRepository) {
        this.justificativaRepository = justificativaRepository;
        this.justificativaSearchRepository = justificativaSearchRepository;
    }

    /**
     * POST  /justificativas : Create a new justificativa.
     *
     * @param justificativa the justificativa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new justificativa, or with status 400 (Bad Request) if the justificativa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/justificativas")
    @Timed
    public ResponseEntity<Justificativa> createJustificativa(@RequestBody Justificativa justificativa) throws URISyntaxException {
        log.debug("REST request to save Justificativa : {}", justificativa);
        if (justificativa.getId() != null) {
            throw new BadRequestAlertException("A new justificativa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Justificativa result = justificativaRepository.save(justificativa);
        justificativaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/justificativas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /justificativas : Updates an existing justificativa.
     *
     * @param justificativa the justificativa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated justificativa,
     * or with status 400 (Bad Request) if the justificativa is not valid,
     * or with status 500 (Internal Server Error) if the justificativa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/justificativas")
    @Timed
    public ResponseEntity<Justificativa> updateJustificativa(@RequestBody Justificativa justificativa) throws URISyntaxException {
        log.debug("REST request to update Justificativa : {}", justificativa);
        if (justificativa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Justificativa result = justificativaRepository.save(justificativa);
        justificativaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, justificativa.getId().toString()))
            .body(result);
    }

    /**
     * GET  /justificativas : get all the justificativas.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of justificativas in body
     */
    @GetMapping("/justificativas")
    @Timed
    public List<Justificativa> getAllJustificativas(@RequestParam(required = false) String filter) {
        if ("servidor-is-null".equals(filter)) {
            log.debug("REST request to get all Justificativas where servidor is null");
            return StreamSupport
                .stream(justificativaRepository.findAll().spliterator(), false)
                .filter(justificativa -> justificativa.getServidor() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Justificativas");
        return justificativaRepository.findAll();
    }

    /**
     * GET  /justificativas/:id : get the "id" justificativa.
     *
     * @param id the id of the justificativa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the justificativa, or with status 404 (Not Found)
     */
    @GetMapping("/justificativas/{id}")
    @Timed
    public ResponseEntity<Justificativa> getJustificativa(@PathVariable Long id) {
        log.debug("REST request to get Justificativa : {}", id);
        Optional<Justificativa> justificativa = justificativaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(justificativa);
    }

    /**
     * DELETE  /justificativas/:id : delete the "id" justificativa.
     *
     * @param id the id of the justificativa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/justificativas/{id}")
    @Timed
    public ResponseEntity<Void> deleteJustificativa(@PathVariable Long id) {
        log.debug("REST request to delete Justificativa : {}", id);

        justificativaRepository.deleteById(id);
        justificativaSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/justificativas?query=:query : search for the justificativa corresponding
     * to the query.
     *
     * @param query the query of the justificativa search
     * @return the result of the search
     */
    @GetMapping("/_search/justificativas")
    @Timed
    public List<Justificativa> searchJustificativas(@RequestParam String query) {
        log.debug("REST request to search Justificativas for query {}", query);
        return StreamSupport
            .stream(justificativaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
