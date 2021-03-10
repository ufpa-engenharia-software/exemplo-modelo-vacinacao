package br.ufpa.castanhal.es2.vacinacao.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link br.ufpa.castanhal.es2.vacinacao.domain.Fabricante} entity.
 */
public class FabricanteDTO implements Serializable {
    
    private Long id;

    private String nome;

    private ZonedDateTime criado;


    private Long paisId;

    private String paisNome;
    private Set<VacinaDTO> vacinas = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Long getPaisId() {
        return paisId;
    }

    public void setPaisId(Long paisId) {
        this.paisId = paisId;
    }

    public String getPaisNome() {
        return paisNome;
    }

    public void setPaisNome(String paisNome) {
        this.paisNome = paisNome;
    }

    public Set<VacinaDTO> getVacinas() {
        return vacinas;
    }

    public void setVacinas(Set<VacinaDTO> vacinas) {
        this.vacinas = vacinas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FabricanteDTO)) {
            return false;
        }

        return id != null && id.equals(((FabricanteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FabricanteDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", criado='" + getCriado() + "'" +
            ", paisId=" + getPaisId() +
            ", paisNome='" + getPaisNome() + "'" +
            ", vacinas='" + getVacinas() + "'" +
            "}";
    }
}
