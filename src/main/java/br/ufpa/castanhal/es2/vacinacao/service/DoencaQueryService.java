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

import br.ufpa.castanhal.es2.vacinacao.domain.Doenca;
import br.ufpa.castanhal.es2.vacinacao.domain.*; // for static metamodels
import br.ufpa.castanhal.es2.vacinacao.repository.DoencaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaCriteria;
import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.DoencaMapper;

/**
 * Service for executing complex queries for {@link Doenca} entities in the database.
 * The main input is a {@link DoencaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DoencaDTO} or a {@link Page} of {@link DoencaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoencaQueryService extends QueryService<Doenca> {

    private final Logger log = LoggerFactory.getLogger(DoencaQueryService.class);

    private final DoencaRepository doencaRepository;

    private final DoencaMapper doencaMapper;

    public DoencaQueryService(DoencaRepository doencaRepository, DoencaMapper doencaMapper) {
        this.doencaRepository = doencaRepository;
        this.doencaMapper = doencaMapper;
    }

    /**
     * Return a {@link List} of {@link DoencaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DoencaDTO> findByCriteria(DoencaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Doenca> specification = createSpecification(criteria);
        return doencaMapper.toDto(doencaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DoencaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoencaDTO> findByCriteria(DoencaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Doenca> specification = createSpecification(criteria);
        return doencaRepository.findAll(specification, page)
            .map(doencaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoencaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Doenca> specification = createSpecification(criteria);
        return doencaRepository.count(specification);
    }

    /**
     * Function to convert {@link DoencaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Doenca> createSpecification(DoencaCriteria criteria) {
        Specification<Doenca> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Doenca_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Doenca_.nome));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Doenca_.criado));
            }
            if (criteria.getDataPrimeiroCaso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataPrimeiroCaso(), Doenca_.dataPrimeiroCaso));
            }
            if (criteria.getLocalPrimeiroCaso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalPrimeiroCaso(), Doenca_.localPrimeiroCaso));
            }
            if (criteria.getPaisPrimeiroCasoId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaisPrimeiroCasoId(),
                    root -> root.join(Doenca_.paisPrimeiroCaso, JoinType.LEFT).get(Pais_.id)));
            }
        }
        return specification;
    }
}
