package com.primeiraaplicacao.app.repository;

import com.primeiraaplicacao.app.domain.Uso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Uso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsoRepository extends JpaRepository<Uso, Long> {

}
