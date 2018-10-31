package com.primeiraaplicacao.app.repository.search;

import com.primeiraaplicacao.app.domain.Manutencao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Manutencao entity.
 */
public interface ManutencaoSearchRepository extends ElasticsearchRepository<Manutencao, Long> {
}
