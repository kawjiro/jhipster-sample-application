package com.primeiraaplicacao.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of JustificativaSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class JustificativaSearchRepositoryMockConfiguration {

    @MockBean
    private JustificativaSearchRepository mockJustificativaSearchRepository;

}
