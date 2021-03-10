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

import br.ufpa.castanhal.es2.vacinacao.domain.Vacina;
import br.ufpa.castanhal.es2.vacinacao.domain.*; // for static metamodels
import br.ufpa.castanhal.es2.vacinacao.repository.VacinaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.VacinaMapper;

/**
 * Service for executing complex queries for {@link Vacina} entities in the database.
 * The main input is a {@link VacinaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VacinaDTO} or a {@link Page} of {@link VacinaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VacinaQueryService extends QueryService<Vacina> {

    private final Logger log = LoggerFactory.getLogger(VacinaQueryService.class);

    private final VacinaRepository vacinaRepository;

    private final VacinaMapper vacinaMapper;

    public VacinaQueryService(VacinaRepository vacinaRepository, VacinaMapper vacinaMapper) {
        this.vacinaRepository = vacinaRepository;
        this.vacinaMapper = vacinaMapper;
    }

    /**
     * Return a {@link List} of {@link VacinaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VacinaDTO> findByCriteria(VacinaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vacina> specification = createSpecification(criteria);
        return vacinaMapper.toDto(vacinaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VacinaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VacinaDTO> findByCriteria(VacinaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vacina> specification = createSpecification(criteria);
        return vacinaRepository.findAll(specification, page)
            .map(vacinaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VacinaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vacina> specification = createSpecification(criteria);
        return vacinaRepository.count(specification);
    }

    /**
     * Function to convert {@link VacinaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vacina> createSpecification(VacinaCriteria criteria) {
        Specification<Vacina> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vacina_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Vacina_.nome));
            }
            if (criteria.getCriada() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriada(), Vacina_.criada));
            }
            if (criteria.getDoencaId() != null) {
                specification = specification.and(buildSpecification(criteria.getDoencaId(),
                    root -> root.join(Vacina_.doenca, JoinType.LEFT).get(Doenca_.id)));
            }
            if (criteria.getFabricantesId() != null) {
                specification = specification.and(buildSpecification(criteria.getFabricantesId(),
                    root -> root.join(Vacina_.fabricantes, JoinType.LEFT).get(Fabricante_.id)));
            }
        }
        return specification;
    }
}
