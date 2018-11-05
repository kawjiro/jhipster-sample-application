package com.primeiraaplicacao.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.primeiraaplicacao.app.domain.Manutencao;
import com.primeiraaplicacao.app.repository.ManutencaoRepository;
import com.primeiraaplicacao.app.repository.search.ManutencaoSearchRepository;
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
 * REST controller for managing Manutencao.
 */
@RestController
@RequestMapping("/api")
public class ManutencaoResource {

    private final Logger log = LoggerFactory.getLogger(ManutencaoResource.class);

    private static final String ENTITY_NAME = "manutencao";

    private final ManutencaoRepository manutencaoRepository;

    private final ManutencaoSearchRepository manutencaoSearchRepository;

    public ManutencaoResource(ManutencaoRepository manutencaoRepository, ManutencaoSearchRepository manutencaoSearchRepository) {
        this.manutencaoRepository = manutencaoRepository;
        this.manutencaoSearchRepository = manutencaoSearchRepository;
    }

    /**
     * POST  /manutencaos : Create a new manutencao.
     *
     * @param manutencao the manutencao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new manutencao, or with status 400 (Bad Request) if the manutencao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/manutencaos")
    @Timed
    public ResponseEntity<Manutencao> createManutencao(@RequestBody Manutencao manutencao) throws URISyntaxException {
        log.debug("REST request to save Manutencao : {}", manutencao);
        if (manutencao.getId() != null) {
            throw new BadRequestAlertException("A new manutencao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Manutencao result = manutencaoRepository.save(manutencao);
        manutencaoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/manutencaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /manutencaos : Updates an existing manutencao.
     *
     * @param manutencao the manutencao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated manutencao,
     * or with status 400 (Bad Request) if the manutencao is not valid,
     * or with status 500 (Internal Server Error) if the manutencao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/manutencaos")
    @Timed
    public ResponseEntity<Manutencao> updateManutencao(@RequestBody Manutencao manutencao) throws URISyntaxException {
        log.debug("REST request to update Manutencao : {}", manutencao);
        if (manutencao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Manutencao result = manutencaoRepository.save(manutencao);
        manutencaoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, manutencao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /manutencaos : get all the manutencaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of manutencaos in body
     */
    @GetMapping("/manutencaos")
    @Timed
    public List<Manutencao> getAllManutencaos() {
        log.debug("REST request to get all Manutencaos");
        return manutencaoRepository.findAll();
    }

    /**
     * GET  /manutencaos/:id : get the "id" manutencao.
     *
     * @param id the id of the manutencao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the manutencao, or with status 404 (Not Found)
     */
    @GetMapping("/manutencaos/{id}")
    @Timed
    public ResponseEntity<Manutencao> getManutencao(@PathVariable Long id) {
        log.debug("REST request to get Manutencao : {}", id);
        Optional<Manutencao> manutencao = manutencaoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(manutencao);
    }

    /**
     * DELETE  /manutencaos/:id : delete the "id" manutencao.
     *
     * @param id the id of the manutencao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/manutencaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteManutencao(@PathVariable Long id) {
        log.debug("REST request to delete Manutencao : {}", id);

        manutencaoRepository.deleteById(id);
        manutencaoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/manutencaos?query=:query : search for the manutencao corresponding
     * to the query.
     *
     * @param query the query of the manutencao search
     * @return the result of the search
     */
    @GetMapping("/_search/manutencaos")
    @Timed
    public List<Manutencao> searchManutencaos(@RequestParam String query) {
        log.debug("REST request to search Manutencaos for query {}", query);
        return StreamSupport
            .stream(manutencaoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
