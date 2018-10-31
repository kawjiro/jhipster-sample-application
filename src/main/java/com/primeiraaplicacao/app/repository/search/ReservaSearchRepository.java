package com.primeiraaplicacao.app.repository.search;

import com.primeiraaplicacao.app.domain.Reserva;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reserva entity.
 */
public interface ReservaSearchRepository extends ElasticsearchRepository<Reserva, Long> {
}
