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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.ufpa.castanhal.es2.vacinacao.domain.Fabricante} entity. This class is used
 * in {@link br.ufpa.castanhal.es2.vacinacao.web.rest.FabricanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fabricantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FabricanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private ZonedDateTimeFilter criado;

    private LongFilter paisId;

    private LongFilter vacinasId;

    public FabricanteCriteria() {
    }

    public FabricanteCriteria(FabricanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.paisId = other.paisId == null ? null : other.paisId.copy();
        this.vacinasId = other.vacinasId == null ? null : other.vacinasId.copy();
    }

    @Override
    public FabricanteCriteria copy() {
        return new FabricanteCriteria(this);
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

    public LongFilter getPaisId() {
        return paisId;
    }

    public void setPaisId(LongFilter paisId) {
        this.paisId = paisId;
    }

    public LongFilter getVacinasId() {
        return vacinasId;
    }

    public void setVacinasId(LongFilter vacinasId) {
        this.vacinasId = vacinasId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FabricanteCriteria that = (FabricanteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(paisId, that.paisId) &&
            Objects.equals(vacinasId, that.vacinasId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        criado,
        paisId,
        vacinasId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FabricanteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (criado != null ? "criado=" + criado + ", " : "") +
                (paisId != null ? "paisId=" + paisId + ", " : "") +
                (vacinasId != null ? "vacinasId=" + vacinasId + ", " : "") +
            "}";
    }

}
