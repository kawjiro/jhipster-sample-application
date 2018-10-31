package com.primeiraaplicacao.app.repository;

import com.primeiraaplicacao.app.domain.Manutencao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Manutencao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

}
