package br.ufpa.castanhal.es2.vacinacao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vacina.
 */
@Entity
@Table(name = "vacina")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vacina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "criada")
    private ZonedDateTime criada;

    @ManyToOne
    @JsonIgnoreProperties(value = "vacinas", allowSetters = true)
    private Doenca doenca;

    @ManyToMany(mappedBy = "vacinas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Fabricante> fabricantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Vacina nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ZonedDateTime getCriada() {
        return criada;
    }

    public Vacina criada(ZonedDateTime criada) {
        this.criada = criada;
        return this;
    }

    public void setCriada(ZonedDateTime criada) {
        this.criada = criada;
    }

    public Doenca getDoenca() {
        return doenca;
    }

    public Vacina doenca(Doenca doenca) {
        this.doenca = doenca;
        return this;
    }

    public void setDoenca(Doenca doenca) {
        this.doenca = doenca;
    }

    public Set<Fabricante> getFabricantes() {
        return fabricantes;
    }

    public Vacina fabricantes(Set<Fabricante> fabricantes) {
        this.fabricantes = fabricantes;
        return this;
    }

    public Vacina addFabricantes(Fabricante fabricante) {
        this.fabricantes.add(fabricante);
        fabricante.getVacinas().add(this);
        return this;
    }

    public Vacina removeFabricantes(Fabricante fabricante) {
        this.fabricantes.remove(fabricante);
        fabricante.getVacinas().remove(this);
        return this;
    }

    public void setFabricantes(Set<Fabricante> fabricantes) {
        this.fabricantes = fabricantes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vacina)) {
            return false;
        }
        return id != null && id.equals(((Vacina) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vacina{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", criada='" + getCriada() + "'" +
            "}";
    }
}
