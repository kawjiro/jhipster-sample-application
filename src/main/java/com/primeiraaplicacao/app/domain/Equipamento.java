package com.primeiraaplicacao.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.primeiraaplicacao.app.domain.enumeration.StatusEquipamento;

/**
 * A Equipamento.
 */
@Entity
@Table(name = "equipamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "equipamento")
public class Equipamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "num_patrimonio")
    private Integer numPatrimonio;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEquipamento status;

    @OneToMany(mappedBy = "equipamento")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ModeloEquipamento> modeloEquipamentos = new HashSet<>();
    @OneToMany(mappedBy = "equipamento")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Manutencao> manutencaos = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("equipamentos")
    private Reserva reserva;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Equipamento descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNumPatrimonio() {
        return numPatrimonio;
    }

    public Equipamento numPatrimonio(Integer numPatrimonio) {
        this.numPatrimonio = numPatrimonio;
        return this;
    }

    public void setNumPatrimonio(Integer numPatrimonio) {
        this.numPatrimonio = numPatrimonio;
    }

    public StatusEquipamento getStatus() {
        return status;
    }

    public Equipamento status(StatusEquipamento status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusEquipamento status) {
        this.status = status;
    }

    public Set<ModeloEquipamento> getModeloEquipamentos() {
        return modeloEquipamentos;
    }

    public Equipamento modeloEquipamentos(Set<ModeloEquipamento> modeloEquipamentos) {
        this.modeloEquipamentos = modeloEquipamentos;
        return this;
    }

    public Equipamento addModeloEquipamento(ModeloEquipamento modeloEquipamento) {
        this.modeloEquipamentos.add(modeloEquipamento);
        modeloEquipamento.setEquipamento(this);
        return this;
    }

    public Equipamento removeModeloEquipamento(ModeloEquipamento modeloEquipamento) {
        this.modeloEquipamentos.remove(modeloEquipamento);
        modeloEquipamento.setEquipamento(null);
        return this;
    }

    public void setModeloEquipamentos(Set<ModeloEquipamento> modeloEquipamentos) {
        this.modeloEquipamentos = modeloEquipamentos;
    }

    public Set<Manutencao> getManutencaos() {
        return manutencaos;
    }

    public Equipamento manutencaos(Set<Manutencao> manutencaos) {
        this.manutencaos = manutencaos;
        return this;
    }

    public Equipamento addManutencao(Manutencao manutencao) {
        this.manutencaos.add(manutencao);
        manutencao.setEquipamento(this);
        return this;
    }

    public Equipamento removeManutencao(Manutencao manutencao) {
        this.manutencaos.remove(manutencao);
        manutencao.setEquipamento(null);
        return this;
    }

    public void setManutencaos(Set<Manutencao> manutencaos) {
        this.manutencaos = manutencaos;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public Equipamento reserva(Reserva reserva) {
        this.reserva = reserva;
        return this;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
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
        Equipamento equipamento = (Equipamento) o;
        if (equipamento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), equipamento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Equipamento{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", numPatrimonio=" + getNumPatrimonio() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
