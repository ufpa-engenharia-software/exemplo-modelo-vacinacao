package br.ufpa.castanhal.es2.vacinacao.service.dto;

import java.time.LocalDate;
import java.io.Serializable;

/**
 * A DTO for the {@link br.ufpa.castanhal.es2.vacinacao.domain.Pessoa} entity.
 */
public class PessoaDTO implements Serializable {
    
    private Long id;

    private String nome;

    private LocalDate dataNascimento;

    
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PessoaDTO)) {
            return false;
        }

        return id != null && id.equals(((PessoaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PessoaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            "}";
    }
}
