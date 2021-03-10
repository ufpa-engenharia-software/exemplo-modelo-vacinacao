package br.ufpa.castanhal.es2.vacinacao.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.ufpa.castanhal.es2.vacinacao.domain.Doenca} entity. This class is used
 * in {@link br.ufpa.castanhal.es2.vacinacao.web.rest.DoencaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doencas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DoencaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private ZonedDateTimeFilter criado;

    private LocalDateFilter dataPrimeiroCaso;

    private LocalDateFilter localPrimeiroCaso;

    private LongFilter paisPrimeiroCasoId;

    public DoencaCriteria() {
    }

    public DoencaCriteria(DoencaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.dataPrimeiroCaso = other.dataPrimeiroCaso == null ? null : other.dataPrimeiroCaso.copy();
        this.localPrimeiroCaso = other.localPrimeiroCaso == null ? null : other.localPrimeiroCaso.copy();
        this.paisPrimeiroCasoId = other.paisPrimeiroCasoId == null ? null : other.paisPrimeiroCasoId.copy();
    }

    @Override
    public DoencaCriteria copy() {
        return new DoencaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public ZonedDateTimeFilter getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTimeFilter criado) {
        this.criado = criado;
    }

    public LocalDateFilter getDataPrimeiroCaso() {
        return dataPrimeiroCaso;
    }

    public void setDataPrimeiroCaso(LocalDateFilter dataPrimeiroCaso) {
        this.dataPrimeiroCaso = dataPrimeiroCaso;
    }

    public LocalDateFilter getLocalPrimeiroCaso() {
        return localPrimeiroCaso;
    }

    public void setLocalPrimeiroCaso(LocalDateFilter localPrimeiroCaso) {
        this.localPrimeiroCaso = localPrimeiroCaso;
    }

    public LongFilter getPaisPrimeiroCasoId() {
        return paisPrimeiroCasoId;
    }

    public void setPaisPrimeiroCasoId(LongFilter paisPrimeiroCasoId) {
        this.paisPrimeiroCasoId = paisPrimeiroCasoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DoencaCriteria that = (DoencaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(dataPrimeiroCaso, that.dataPrimeiroCaso) &&
            Objects.equals(localPrimeiroCaso, that.localPrimeiroCaso) &&
            Objects.equals(paisPrimeiroCasoId, that.paisPrimeiroCasoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        criado,
        dataPrimeiroCaso,
        localPrimeiroCaso,
        paisPrimeiroCasoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoencaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (criado != null ? "criado=" + criado + ", " : "") +
                (dataPrimeiroCaso != null ? "dataPrimeiroCaso=" + dataPrimeiroCaso + ", " : "") +
                (localPrimeiroCaso != null ? "localPrimeiroCaso=" + localPrimeiroCaso + ", " : "") +
                (paisPrimeiroCasoId != null ? "paisPrimeiroCasoId=" + paisPrimeiroCasoId + ", " : "") +
            "}";
    }

}
