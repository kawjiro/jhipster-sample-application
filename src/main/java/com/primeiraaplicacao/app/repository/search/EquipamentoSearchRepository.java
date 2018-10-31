package com.primeiraaplicacao.app.repository.search;

import com.primeiraaplicacao.app.domain.Equipamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Equipamento entity.
 */
public interface EquipamentoSearchRepository extends ElasticsearchRepository<Equipamento, Long> {
}
