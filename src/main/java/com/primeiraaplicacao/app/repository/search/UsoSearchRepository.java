package com.primeiraaplicacao.app.repository.search;

import com.primeiraaplicacao.app.domain.Uso;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Uso entity.
 */
public interface UsoSearchRepository extends ElasticsearchRepository<Uso, Long> {
}
