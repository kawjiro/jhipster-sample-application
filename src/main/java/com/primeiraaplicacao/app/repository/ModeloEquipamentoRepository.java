package com.primeiraaplicacao.app.repository;

import com.primeiraaplicacao.app.domain.ModeloEquipamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ModeloEquipamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModeloEquipamentoRepository extends JpaRepository<ModeloEquipamento, Long> {

}
