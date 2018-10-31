package com.primeiraaplicacao.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Reserva.
 */
@Entity
@Table(name = "reserva")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reserva")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "data_hora_reserva")
    private LocalDate dataHoraReserva;

    @Column(name = "data_hora_retirda_prev")
    private LocalDate dataHoraRetirdaPrev;

    @Column(name = "data_hora_devolucao_prev")
    private LocalDate dataHoraDevolucaoPrev;

    @OneToMany(mappedBy = "reserva")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Equipamento> equipamentos = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("reservas")
    private Servidor servidor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataHoraReserva() {
        return dataHoraReserva;
    }

    public Reserva dataHoraReserva(LocalDate dataHoraReserva) {
        this.dataHoraReserva = dataHoraReserva;
        return this;
    }

    public void setDataHoraReserva(LocalDate dataHoraReserva) {
        this.dataHoraReserva = dataHoraReserva;
    }

    public LocalDate getDataHoraRetirdaPrev() {
        return dataHoraRetirdaPrev;
    }

    public Reserva dataHoraRetirdaPrev(LocalDate dataHoraRetirdaPrev) {
        this.dataHoraRetirdaPrev = dataHoraRetirdaPrev;
        return this;
    }

    public void setDataHoraRetirdaPrev(LocalDate dataHoraRetirdaPrev) {
        this.dataHoraRetirdaPrev = dataHoraRetirdaPrev;
    }

    public LocalDate getDataHoraDevolucaoPrev() {
        return dataHoraDevolucaoPrev;
    }

    public Reserva dataHoraDevolucaoPrev(LocalDate dataHoraDevolucaoPrev) {
        this.dataHoraDevolucaoPrev = dataHoraDevolucaoPrev;
        return this;
    }

    public void setDataHoraDevolucaoPrev(LocalDate dataHoraDevolucaoPrev) {
        this.dataHoraDevolucaoPrev = dataHoraDevolucaoPrev;
    }

    public Set<Equipamento> getEquipamentos() {
        return equipamentos;
    }

    public Reserva equipamentos(Set<Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
        return this;
    }

    public Reserva addEquipamento(Equipamento equipamento) {
        this.equipamentos.add(equipamento);
        equipamento.setReserva(this);
        return this;
    }

    public Reserva removeEquipamento(Equipamento equipamento) {
        this.equipamentos.remove(equipamento);
        equipamento.setReserva(null);
        return this;
    }

    public void setEquipamentos(Set<Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public Reserva servidor(Servidor servidor) {
        this.servidor = servidor;
        return this;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
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
        Reserva reserva = (Reserva) o;
        if (reserva.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reserva.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reserva{" +
            "id=" + getId() +
            ", dataHoraReserva='" + getDataHoraReserva() + "'" +
            ", dataHoraRetirdaPrev='" + getDataHoraRetirdaPrev() + "'" +
            ", dataHoraDevolucaoPrev='" + getDataHoraDevolucaoPrev() + "'" +
            "}";
    }
}
