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
 * Criteria class for the {@link br.ufpa.castanhal.es2.vacinacao.domain.Vacina} entity. This class is used
 * in {@link br.ufpa.castanhal.es2.vacinacao.web.rest.VacinaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vacinas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VacinaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private ZonedDateTimeFilter criada;

    private LongFilter doencaId;

    private LongFilter fabricantesId;

    public VacinaCriteria() {
    }

    public VacinaCriteria(VacinaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.criada = other.criada == null ? null : other.criada.copy();
        this.doencaId = other.doencaId == null ? null : other.doencaId.copy();
        this.fabricantesId = other.fabricantesId == null ? null : other.fabricantesId.copy();
    }

    @Override
    public VacinaCriteria copy() {
        return new VacinaCriteria(this);
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

    public ZonedDateTimeFilter getCriada() {
        return criada;
    }

    public void setCriada(ZonedDateTimeFilter criada) {
        this.criada = criada;
    }

    public LongFilter getDoencaId() {
        return doencaId;
    }

    public void setDoencaId(LongFilter doencaId) {
        this.doencaId = doencaId;
    }

    public LongFilter getFabricantesId() {
        return fabricantesId;
    }

    public void setFabricantesId(LongFilter fabricantesId) {
        this.fabricantesId = fabricantesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VacinaCriteria that = (VacinaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(criada, that.criada) &&
            Objects.equals(doencaId, that.doencaId) &&
            Objects.equals(fabricantesId, that.fabricantesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        criada,
        doencaId,
        fabricantesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VacinaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (criada != null ? "criada=" + criada + ", " : "") +
                (doencaId != null ? "doencaId=" + doencaId + ", " : "") +
                (fabricantesId != null ? "fabricantesId=" + fabricantesId + ", " : "") +
            "}";
    }

}
