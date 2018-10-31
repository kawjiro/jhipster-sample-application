package com.primeiraaplicacao.app.repository.search;

import com.primeiraaplicacao.app.domain.Servidor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Servidor entity.
 */
public interface ServidorSearchRepository extends ElasticsearchRepository<Servidor, Long> {
}
