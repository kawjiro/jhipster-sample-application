package com.primeiraaplicacao.app.repository;

import com.primeiraaplicacao.app.domain.OrgaoPublico;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrgaoPublico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgaoPublicoRepository extends JpaRepository<OrgaoPublico, Long> {

}
