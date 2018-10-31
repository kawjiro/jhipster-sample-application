package com.primeiraaplicacao.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ManutencaoSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ManutencaoSearchRepositoryMockConfiguration {

    @MockBean
    private ManutencaoSearchRepository mockManutencaoSearchRepository;

}
