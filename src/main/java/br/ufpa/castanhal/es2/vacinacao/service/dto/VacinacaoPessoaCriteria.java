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

/**
 * Criteria class for the {@link br.ufpa.castanhal.es2.vacinacao.domain.VacinacaoPessoa} entity. This class is used
 * in {@link br.ufpa.castanhal.es2.vacinacao.web.rest.VacinacaoPessoaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vacinacao-pessoas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VacinacaoPessoaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter quando;

    private StringFilter cns;

    private StringFilter codigoProfissional;

    private LongFilter pessoaId;

    private LongFilter vacinaId;

    private LongFilter fabricanteId;

    public VacinacaoPessoaCriteria() {
    }

    public VacinacaoPessoaCriteria(VacinacaoPessoaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quando = other.quando == null ? null : other.quando.copy();
        this.cns = other.cns == null ? null : other.cns.copy();
        this.codigoProfissional = other.codigoProfissional == null ? null : other.codigoProfissional.copy();
        this.pessoaId = other.pessoaId == null ? null : other.pessoaId.copy();
        this.vacinaId = other.vacinaId == null ? null : other.vacinaId.copy();
        this.fabricanteId = other.fabricanteId == null ? null : other.fabricanteId.copy();
    }

    @Override
    public VacinacaoPessoaCriteria copy() {
        return new VacinacaoPessoaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getQuando() {
        return quando;
    }

    public void setQuando(LocalDateFilter quando) {
        this.quando = quando;
    }

    public StringFilter getCns() {
        return cns;
    }

    public void setCns(StringFilter cns) {
        this.cns = cns;
    }

    public StringFilter getCodigoProfissional() {
        return codigoProfissional;
    }

    public void setCodigoProfissional(StringFilter codigoProfissional) {
        this.codigoProfissional = codigoProfissional;
    }

    public LongFilter getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(LongFilter pessoaId) {
        this.pessoaId = pessoaId;
    }

    public LongFilter getVacinaId() {
        return vacinaId;
    }

    public void setVacinaId(LongFilter vacinaId) {
        this.vacinaId = vacinaId;
    }

    public LongFilter getFabricanteId() {
        return fabricanteId;
    }

    public void setFabricanteId(LongFilter fabricanteId) {
        this.fabricanteId = fabricanteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VacinacaoPessoaCriteria that = (VacinacaoPessoaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(quando, that.quando) &&
            Objects.equals(cns, that.cns) &&
            Objects.equals(codigoProfissional, that.codigoProfissional) &&
            Objects.equals(pessoaId, that.pessoaId) &&
            Objects.equals(vacinaId, that.vacinaId) &&
            Objects.equals(fabricanteId, that.fabricanteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        quando,
        cns,
        codigoProfissional,
        pessoaId,
        vacinaId,
        fabricanteId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VacinacaoPessoaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (quando != null ? "quando=" + quando + ", " : "") +
                (cns != null ? "cns=" + cns + ", " : "") +
                (codigoProfissional != null ? "codigoProfissional=" + codigoProfissional + ", " : "") +
                (pessoaId != null ? "pessoaId=" + pessoaId + ", " : "") +
                (vacinaId != null ? "vacinaId=" + vacinaId + ", " : "") +
                (fabricanteId != null ? "fabricanteId=" + fabricanteId + ", " : "") +
            "}";
    }

}
