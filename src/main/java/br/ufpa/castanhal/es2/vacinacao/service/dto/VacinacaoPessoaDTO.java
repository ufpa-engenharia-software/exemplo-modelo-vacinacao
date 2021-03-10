package br.ufpa.castanhal.es2.vacinacao.service.dto;

import java.time.LocalDate;
import java.io.Serializable;

/**
 * A DTO for the {@link br.ufpa.castanhal.es2.vacinacao.domain.VacinacaoPessoa} entity.
 */
public class VacinacaoPessoaDTO implements Serializable {
    
    private Long id;

    private LocalDate quando;

    private String cns;

    private String codigoProfissional;


    private Long pessoaId;

    private String pessoaNome;

    private Long vacinaId;

    private String vacinaNome;

    private Long fabricanteId;

    private String fabricanteNome;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getQuando() {
        return quando;
    }

    public void setQuando(LocalDate quando) {
        this.quando = quando;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getCodigoProfissional() {
        return codigoProfissional;
    }

    public void setCodigoProfissional(String codigoProfissional) {
        this.codigoProfissional = codigoProfissional;
    }

    public Long getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Long pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getPessoaNome() {
        return pessoaNome;
    }

    public void setPessoaNome(String pessoaNome) {
        this.pessoaNome = pessoaNome;
    }

    public Long getVacinaId() {
        return vacinaId;
    }

    public void setVacinaId(Long vacinaId) {
        this.vacinaId = vacinaId;
    }

    public String getVacinaNome() {
        return vacinaNome;
    }

    public void setVacinaNome(String vacinaNome) {
        this.vacinaNome = vacinaNome;
    }

    public Long getFabricanteId() {
        return fabricanteId;
    }

    public void setFabricanteId(Long fabricanteId) {
        this.fabricanteId = fabricanteId;
    }

    public String getFabricanteNome() {
        return fabricanteNome;
    }

    public void setFabricanteNome(String fabricanteNome) {
        this.fabricanteNome = fabricanteNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VacinacaoPessoaDTO)) {
            return false;
        }

        return id != null && id.equals(((VacinacaoPessoaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VacinacaoPessoaDTO{" +
            "id=" + getId() +
            ", quando='" + getQuando() + "'" +
            ", cns='" + getCns() + "'" +
            ", codigoProfissional='" + getCodigoProfissional() + "'" +
            ", pessoaId=" + getPessoaId() +
            ", pessoaNome='" + getPessoaNome() + "'" +
            ", vacinaId=" + getVacinaId() +
            ", vacinaNome='" + getVacinaNome() + "'" +
            ", fabricanteId=" + getFabricanteId() +
            ", fabricanteNome='" + getFabricanteNome() + "'" +
            "}";
    }
}
