package com.primeiraaplicacao.app.repository;

import com.primeiraaplicacao.app.domain.Justificativa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Justificativa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JustificativaRepository extends JpaRepository<Justificativa, Long> {

}
