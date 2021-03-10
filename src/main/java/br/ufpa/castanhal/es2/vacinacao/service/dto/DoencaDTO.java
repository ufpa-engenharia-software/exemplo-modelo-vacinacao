package br.ufpa.castanhal.es2.vacinacao.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * A DTO for the {@link br.ufpa.castanhal.es2.vacinacao.domain.Doenca} entity.
 */
public class DoencaDTO implements Serializable {
    
    private Long id;

    private String nome;

    private ZonedDateTime criado;

    private LocalDate dataPrimeiroCaso;

    private LocalDate localPrimeiroCaso;


    private Long paisPrimeiroCasoId;

    private String paisPrimeiroCasoNome;
    
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

    public LocalDate getDataPrimeiroCaso() {
        return dataPrimeiroCaso;
    }

    public void setDataPrimeiroCaso(LocalDate dataPrimeiroCaso) {
        this.dataPrimeiroCaso = dataPrimeiroCaso;
    }

    public LocalDate getLocalPrimeiroCaso() {
        return localPrimeiroCaso;
    }

    public void setLocalPrimeiroCaso(LocalDate localPrimeiroCaso) {
        this.localPrimeiroCaso = localPrimeiroCaso;
    }

    public Long getPaisPrimeiroCasoId() {
        return paisPrimeiroCasoId;
    }

    public void setPaisPrimeiroCasoId(Long paisId) {
        this.paisPrimeiroCasoId = paisId;
    }

    public String getPaisPrimeiroCasoNome() {
        return paisPrimeiroCasoNome;
    }

    public void setPaisPrimeiroCasoNome(String paisNome) {
        this.paisPrimeiroCasoNome = paisNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoencaDTO)) {
            return false;
        }

        return id != null && id.equals(((DoencaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoencaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", criado='" + getCriado() + "'" +
            ", dataPrimeiroCaso='" + getDataPrimeiroCaso() + "'" +
            ", localPrimeiroCaso='" + getLocalPrimeiroCaso() + "'" +
            ", paisPrimeiroCasoId=" + getPaisPrimeiroCasoId() +
            ", paisPrimeiroCasoNome='" + getPaisPrimeiroCasoNome() + "'" +
            "}";
    }
}
