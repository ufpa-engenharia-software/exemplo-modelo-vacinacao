package br.ufpa.castanhal.es2.vacinacao.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Fabricante.
 */
@Entity
@Table(name = "fabricante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fabricante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @ManyToOne
    @JsonIgnoreProperties(value = "fabricantes", allowSetters = true)
    private Pais pais;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "fabricante_vacinas",
               joinColumns = @JoinColumn(name = "fabricante_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "vacinas_id", referencedColumnName = "id"))
    private Set<Vacina> vacinas = new HashSet<>();

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

    public Fabricante nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public Fabricante criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Pais getPais() {
        return pais;
    }

    public Fabricante pais(Pais pais) {
        this.pais = pais;
        return this;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Set<Vacina> getVacinas() {
        return vacinas;
    }

    public Fabricante vacinas(Set<Vacina> vacinas) {
        this.vacinas = vacinas;
        return this;
    }

    public Fabricante addVacinas(Vacina vacina) {
        this.vacinas.add(vacina);
        vacina.getFabricantes().add(this);
        return this;
    }

    public Fabricante removeVacinas(Vacina vacina) {
        this.vacinas.remove(vacina);
        vacina.getFabricantes().remove(this);
        return this;
    }

    public void setVacinas(Set<Vacina> vacinas) {
        this.vacinas = vacinas;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fabricante)) {
            return false;
        }
        return id != null && id.equals(((Fabricante) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fabricante{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
