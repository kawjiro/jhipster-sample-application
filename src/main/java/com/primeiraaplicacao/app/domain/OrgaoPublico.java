package com.primeiraaplicacao.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OrgaoPublico.
 */
@Entity
@Table(name = "orgao_publico")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orgaopublico")
public class OrgaoPublico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome_orgao_publico")
    private String nomeOrgaoPublico;

    @OneToMany(mappedBy = "orgaoPublico")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Servidor> servidors = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeOrgaoPublico() {
        return nomeOrgaoPublico;
    }

    public OrgaoPublico nomeOrgaoPublico(String nomeOrgaoPublico) {
        this.nomeOrgaoPublico = nomeOrgaoPublico;
        return this;
    }

    public void setNomeOrgaoPublico(String nomeOrgaoPublico) {
        this.nomeOrgaoPublico = nomeOrgaoPublico;
    }

    public Set<Servidor> getServidors() {
        return servidors;
    }

    public OrgaoPublico servidors(Set<Servidor> servidors) {
        this.servidors = servidors;
        return this;
    }

    public OrgaoPublico addServidor(Servidor servidor) {
        this.servidors.add(servidor);
        servidor.setOrgaoPublico(this);
        return this;
    }

    public OrgaoPublico removeServidor(Servidor servidor) {
        this.servidors.remove(servidor);
        servidor.setOrgaoPublico(null);
        return this;
    }

    public void setServidors(Set<Servidor> servidors) {
        this.servidors = servidors;
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
        OrgaoPublico orgaoPublico = (OrgaoPublico) o;
        if (orgaoPublico.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orgaoPublico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrgaoPublico{" +
            "id=" + getId() +
            ", nomeOrgaoPublico='" + getNomeOrgaoPublico() + "'" +
            "}";
    }
}
