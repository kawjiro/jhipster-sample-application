package com.primeiraaplicacao.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Manutencao.
 */
@Entity
@Table(name = "manutencao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "manutencao")
public class Manutencao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "estado_equipamento")
    private String estadoEquipamento;

    @ManyToOne
    @JsonIgnoreProperties("manutencaos")
    private Equipamento equipamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstadoEquipamento() {
        return estadoEquipamento;
    }

    public Manutencao estadoEquipamento(String estadoEquipamento) {
        this.estadoEquipamento = estadoEquipamento;
        return this;
    }

    public void setEstadoEquipamento(String estadoEquipamento) {
        this.estadoEquipamento = estadoEquipamento;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public Manutencao equipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
        return this;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Manutencao manutencao = (Manutencao) o;
        if (manutencao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), manutencao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Manutencao{" +
            "id=" + getId() +
            ", estadoEquipamento='" + getEstadoEquipamento() + "'" +
            "}";
    }
}
