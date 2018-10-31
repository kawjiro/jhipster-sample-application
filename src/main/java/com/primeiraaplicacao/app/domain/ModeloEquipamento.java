package com.primeiraaplicacao.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ModeloEquipamento.
 */
@Entity
@Table(name = "modelo_equipamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "modeloequipamento")
public class ModeloEquipamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome_modelo")
    private String nomeModelo;

    @Column(name = "num_patrimonio")
    private Integer numPatrimonio;

    @ManyToOne
    @JsonIgnoreProperties("modeloEquipamentos")
    private Equipamento equipamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeModelo() {
        return nomeModelo;
    }

    public ModeloEquipamento nomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
        return this;
    }

    public void setNomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
    }

    public Integer getNumPatrimonio() {
        return numPatrimonio;
    }

    public ModeloEquipamento numPatrimonio(Integer numPatrimonio) {
        this.numPatrimonio = numPatrimonio;
        return this;
    }

    public void setNumPatrimonio(Integer numPatrimonio) {
        this.numPatrimonio = numPatrimonio;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public ModeloEquipamento equipamento(Equipamento equipamento) {
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
        ModeloEquipamento modeloEquipamento = (ModeloEquipamento) o;
        if (modeloEquipamento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), modeloEquipamento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ModeloEquipamento{" +
            "id=" + getId() +
            ", nomeModelo='" + getNomeModelo() + "'" +
            ", numPatrimonio=" + getNumPatrimonio() +
            "}";
    }
}
