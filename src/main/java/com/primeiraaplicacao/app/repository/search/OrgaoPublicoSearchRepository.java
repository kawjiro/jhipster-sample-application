package com.primeiraaplicacao.app.repository.search;

import com.primeiraaplicacao.app.domain.OrgaoPublico;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrgaoPublico entity.
 */
public interface OrgaoPublicoSearchRepository extends ElasticsearchRepository<OrgaoPublico, Long> {
}
