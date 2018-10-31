package com.primeiraaplicacao.app.repository;

import com.primeiraaplicacao.app.domain.Equipamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Equipamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

}
