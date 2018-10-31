package com.primeiraaplicacao.app.repository.search;

import com.primeiraaplicacao.app.domain.ModeloEquipamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ModeloEquipamento entity.
 */
public interface ModeloEquipamentoSearchRepository extends ElasticsearchRepository<ModeloEquipamento, Long> {
}
