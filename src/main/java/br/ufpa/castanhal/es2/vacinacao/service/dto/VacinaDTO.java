package br.ufpa.castanhal.es2.vacinacao.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * A DTO for the {@link br.ufpa.castanhal.es2.vacinacao.domain.Vacina} entity.
 */
public class VacinaDTO implements Serializable {
    
    private Long id;

    private String nome;

    private ZonedDateTime criada;


    private Long doencaId;

    private String doencaNome;
    
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

    public ZonedDateTime getCriada() {
        return criada;
    }

    public void setCriada(ZonedDateTime criada) {
        this.criada = criada;
    }

    public Long getDoencaId() {
        return doencaId;
    }

    public void setDoencaId(Long doencaId) {
        this.doencaId = doencaId;
    }

    public String getDoencaNome() {
        return doencaNome;
    }

    public void setDoencaNome(String doencaNome) {
        this.doencaNome = doencaNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VacinaDTO)) {
            return false;
        }

        return id != null && id.equals(((VacinaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VacinaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", criada='" + getCriada() + "'" +
            ", doencaId=" + getDoencaId() +
            ", doencaNome='" + getDoencaNome() + "'" +
            "}";
    }
}
