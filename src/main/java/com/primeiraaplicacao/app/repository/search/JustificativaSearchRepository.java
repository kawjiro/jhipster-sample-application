package com.primeiraaplicacao.app.repository.search;

import com.primeiraaplicacao.app.domain.Justificativa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Justificativa entity.
 */
public interface JustificativaSearchRepository extends ElasticsearchRepository<Justificativa, Long> {
}
