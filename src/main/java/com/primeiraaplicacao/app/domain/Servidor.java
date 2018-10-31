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

/**
 * A Servidor.
 */
@Entity
@Table(name = "servidor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "servidor")
public class Servidor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "num_matricula")
    private String numMatricula;

    @Column(name = "cargo")
    private String cargo;

    @OneToOne    @JoinColumn(unique = true)
    private Uso uso;

    @OneToOne    @JoinColumn(unique = true)
    private Justificativa justificativa;

    @OneToMany(mappedBy = "servidor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reserva> reservas = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("servidors")
    private OrgaoPublico orgaoPublico;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Servidor nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumMatricula() {
        return numMatricula;
    }

    public Servidor numMatricula(String numMatricula) {
        this.numMatricula = numMatricula;
        return this;
    }

    public void setNumMatricula(String numMatricula) {
        this.numMatricula = numMatricula;
    }

    public String getCargo() {
        return cargo;
    }

    public Servidor cargo(String cargo) {
        this.cargo = cargo;
        return this;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Uso getUso() {
        return uso;
    }

    public Servidor uso(Uso uso) {
        this.uso = uso;
        return this;
    }

    public void setUso(Uso uso) {
        this.uso = uso;
    }

    public Justificativa getJustificativa() {
        return justificativa;
    }

    public Servidor justificativa(Justificativa justificativa) {
        this.justificativa = justificativa;
        return this;
    }

    public void setJustificativa(Justificativa justificativa) {
        this.justificativa = justificativa;
    }

    public Set<Reserva> getReservas() {
        return reservas;
    }

    public Servidor reservas(Set<Reserva> reservas) {
        this.reservas = reservas;
        return this;
    }

    public Servidor addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setServidor(this);
        return this;
    }

    public Servidor removeReserva(Reserva reserva) {
        this.reservas.remove(reserva);
        reserva.setServidor(null);
        return this;
    }

    public void setReservas(Set<Reserva> reservas) {
        this.reservas = reservas;
    }

    public OrgaoPublico getOrgaoPublico() {
        return orgaoPublico;
    }

    public Servidor orgaoPublico(OrgaoPublico orgaoPublico) {
        this.orgaoPublico = orgaoPublico;
        return this;
    }

    public void setOrgaoPublico(OrgaoPublico orgaoPublico) {
        this.orgaoPublico = orgaoPublico;
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
        Servidor servidor = (Servidor) o;
        if (servidor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), servidor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Servidor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", numMatricula='" + getNumMatricula() + "'" +
            ", cargo='" + getCargo() + "'" +
            "}";
    }
}
