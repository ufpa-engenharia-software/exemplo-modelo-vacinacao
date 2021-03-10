package br.ufpa.castanhal.es2.vacinacao.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.ufpa.castanhal.es2.vacinacao.domain.VacinacaoPessoa;
import br.ufpa.castanhal.es2.vacinacao.domain.*; // for static metamodels
import br.ufpa.castanhal.es2.vacinacao.repository.VacinacaoPessoaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.VacinacaoPessoaMapper;

/**
 * Service for executing complex queries for {@link VacinacaoPessoa} entities in the database.
 * The main input is a {@link VacinacaoPessoaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VacinacaoPessoaDTO} or a {@link Page} of {@link VacinacaoPessoaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VacinacaoPessoaQueryService extends QueryService<VacinacaoPessoa> {

    private final Logger log = LoggerFactory.getLogger(VacinacaoPessoaQueryService.class);

    private final VacinacaoPessoaRepository vacinacaoPessoaRepository;

    private final VacinacaoPessoaMapper vacinacaoPessoaMapper;

    public VacinacaoPessoaQueryService(VacinacaoPessoaRepository vacinacaoPessoaRepository, VacinacaoPessoaMapper vacinacaoPessoaMapper) {
        this.vacinacaoPessoaRepository = vacinacaoPessoaRepository;
        this.vacinacaoPessoaMapper = vacinacaoPessoaMapper;
    }

    /**
     * Return a {@link List} of {@link VacinacaoPessoaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VacinacaoPessoaDTO> findByCriteria(VacinacaoPessoaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VacinacaoPessoa> specification = createSpecification(criteria);
        return vacinacaoPessoaMapper.toDto(vacinacaoPessoaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VacinacaoPessoaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VacinacaoPessoaDTO> findByCriteria(VacinacaoPessoaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VacinacaoPessoa> specification = createSpecification(criteria);
        return vacinacaoPessoaRepository.findAll(specification, page)
            .map(vacinacaoPessoaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VacinacaoPessoaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VacinacaoPessoa> specification = createSpecification(criteria);
        return vacinacaoPessoaRepository.count(specification);
    }

    /**
     * Function to convert {@link VacinacaoPessoaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VacinacaoPessoa> createSpecification(VacinacaoPessoaCriteria criteria) {
        Specification<VacinacaoPessoa> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VacinacaoPessoa_.id));
            }
            if (criteria.getQuando() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuando(), VacinacaoPessoa_.quando));
            }
            if (criteria.getCns() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCns(), VacinacaoPessoa_.cns));
            }
            if (criteria.getCodigoProfissional() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigoProfissional(), VacinacaoPessoa_.codigoProfissional));
            }
            if (criteria.getPessoaId() != null) {
                specification = specification.and(buildSpecification(criteria.getPessoaId(),
                    root -> root.join(VacinacaoPessoa_.pessoa, JoinType.LEFT).get(Pessoa_.id)));
            }
            if (criteria.getVacinaId() != null) {
                specification = specification.and(buildSpecification(criteria.getVacinaId(),
                    root -> root.join(VacinacaoPessoa_.vacina, JoinType.LEFT).get(Vacina_.id)));
            }
            if (criteria.getFabricanteId() != null) {
                specification = specification.and(buildSpecification(criteria.getFabricanteId(),
                    root -> root.join(VacinacaoPessoa_.fabricante, JoinType.LEFT).get(Fabricante_.id)));
            }
        }
        return specification;
    }
}
