package com.primeiraaplicacao.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.primeiraaplicacao.app.domain.OrgaoPublico;
import com.primeiraaplicacao.app.repository.OrgaoPublicoRepository;
import com.primeiraaplicacao.app.repository.search.OrgaoPublicoSearchRepository;
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
 * REST controller for managing OrgaoPublico.
 */
@RestController
@RequestMapping("/api")
public class OrgaoPublicoResource {

    private final Logger log = LoggerFactory.getLogger(OrgaoPublicoResource.class);

    private static final String ENTITY_NAME = "orgaoPublico";

    private final OrgaoPublicoRepository orgaoPublicoRepository;

    private final OrgaoPublicoSearchRepository orgaoPublicoSearchRepository;

    public OrgaoPublicoResource(OrgaoPublicoRepository orgaoPublicoRepository, OrgaoPublicoSearchRepository orgaoPublicoSearchRepository) {
        this.orgaoPublicoRepository = orgaoPublicoRepository;
        this.orgaoPublicoSearchRepository = orgaoPublicoSearchRepository;
    }

    /**
     * POST  /orgao-publicos : Create a new orgaoPublico.
     *
     * @param orgaoPublico the orgaoPublico to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orgaoPublico, or with status 400 (Bad Request) if the orgaoPublico has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orgao-publicos")
    @Timed
    public ResponseEntity<OrgaoPublico> createOrgaoPublico(@RequestBody OrgaoPublico orgaoPublico) throws URISyntaxException {
        log.debug("REST request to save OrgaoPublico : {}", orgaoPublico);
        if (orgaoPublico.getId() != null) {
            throw new BadRequestAlertException("A new orgaoPublico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgaoPublico result = orgaoPublicoRepository.save(orgaoPublico);
        orgaoPublicoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/orgao-publicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orgao-publicos : Updates an existing orgaoPublico.
     *
     * @param orgaoPublico the orgaoPublico to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orgaoPublico,
     * or with status 400 (Bad Request) if the orgaoPublico is not valid,
     * or with status 500 (Internal Server Error) if the orgaoPublico couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orgao-publicos")
    @Timed
    public ResponseEntity<OrgaoPublico> updateOrgaoPublico(@RequestBody OrgaoPublico orgaoPublico) throws URISyntaxException {
        log.debug("REST request to update OrgaoPublico : {}", orgaoPublico);
        if (orgaoPublico.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrgaoPublico result = orgaoPublicoRepository.save(orgaoPublico);
        orgaoPublicoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orgaoPublico.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orgao-publicos : get all the orgaoPublicos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orgaoPublicos in body
     */
    @GetMapping("/orgao-publicos")
    @Timed
    public List<OrgaoPublico> getAllOrgaoPublicos() {
        log.debug("REST request to get all OrgaoPublicos");
        return orgaoPublicoRepository.findAll();
    }

    /**
     * GET  /orgao-publicos/:id : get the "id" orgaoPublico.
     *
     * @param id the id of the orgaoPublico to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orgaoPublico, or with status 404 (Not Found)
     */
    @GetMapping("/orgao-publicos/{id}")
    @Timed
    public ResponseEntity<OrgaoPublico> getOrgaoPublico(@PathVariable Long id) {
        log.debug("REST request to get OrgaoPublico : {}", id);
        Optional<OrgaoPublico> orgaoPublico = orgaoPublicoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orgaoPublico);
    }

    /**
     * DELETE  /orgao-publicos/:id : delete the "id" orgaoPublico.
     *
     * @param id the id of the orgaoPublico to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orgao-publicos/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrgaoPublico(@PathVariable Long id) {
        log.debug("REST request to delete OrgaoPublico : {}", id);

        orgaoPublicoRepository.deleteById(id);
        orgaoPublicoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/orgao-publicos?query=:query : search for the orgaoPublico corresponding
     * to the query.
     *
     * @param query the query of the orgaoPublico search
     * @return the result of the search
     */
    @GetMapping("/_search/orgao-publicos")
    @Timed
    public List<OrgaoPublico> searchOrgaoPublicos(@RequestParam String query) {
        log.debug("REST request to search OrgaoPublicos for query {}", query);
        return StreamSupport
            .stream(orgaoPublicoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
