package com.primeiraaplicacao.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Justificativa.
 */
@Entity
@Table(name = "justificativa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "justificativa")
public class Justificativa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "descricao_atividade")
    private String descricaoAtividade;

    @OneToOne(mappedBy = "justificativa")
    @JsonIgnore
    private Servidor servidor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricaoAtividade() {
        return descricaoAtividade;
    }

    public Justificativa descricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
        return this;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public Justificativa servidor(Servidor servidor) {
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
        Justificativa justificativa = (Justificativa) o;
        if (justificativa.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), justificativa.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Justificativa{" +
            "id=" + getId() +
            ", descricaoAtividade='" + getDescricaoAtividade() + "'" +
            "}";
    }
}
