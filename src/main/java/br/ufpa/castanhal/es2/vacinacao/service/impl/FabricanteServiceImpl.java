package br.ufpa.castanhal.es2.vacinacao.service.impl;

import br.ufpa.castanhal.es2.vacinacao.service.FabricanteService;
import br.ufpa.castanhal.es2.vacinacao.domain.Fabricante;
import br.ufpa.castanhal.es2.vacinacao.repository.FabricanteRepository;
import br.ufpa.castanhal.es2.vacinacao.service.dto.FabricanteDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.FabricanteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Fabricante}.
 */
@Service
@Transactional
public class FabricanteServiceImpl implements FabricanteService {

    private final Logger log = LoggerFactory.getLogger(FabricanteServiceImpl.class);

    private final FabricanteRepository fabricanteRepository;

    private final FabricanteMapper fabricanteMapper;

    public FabricanteServiceImpl(FabricanteRepository fabricanteRepository, FabricanteMapper fabricanteMapper) {
        this.fabricanteRepository = fabricanteRepository;
        this.fabricanteMapper = fabricanteMapper;
    }

    @Override
    public FabricanteDTO save(FabricanteDTO fabricanteDTO) {
        log.debug("Request to save Fabricante : {}", fabricanteDTO);
        Fabricante fabricante = fabricanteMapper.toEntity(fabricanteDTO);
        fabricante = fabricanteRepository.save(fabricante);
        return fabricanteMapper.toDto(fabricante);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FabricanteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fabricantes");
        return fabricanteRepository.findAll(pageable)
            .map(fabricanteMapper::toDto);
    }


    public Page<FabricanteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fabricanteRepository.findAllWithEagerRelationships(pageable).map(fabricanteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FabricanteDTO> findOne(Long id) {
        log.debug("Request to get Fabricante : {}", id);
        return fabricanteRepository.findOneWithEagerRelationships(id)
            .map(fabricanteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fabricante : {}", id);
        fabricanteRepository.deleteById(id);
    }
}
