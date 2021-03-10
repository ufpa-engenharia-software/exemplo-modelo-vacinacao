package br.ufpa.castanhal.es2.vacinacao.service.impl;

import br.ufpa.castanhal.es2.vacinacao.service.DoencaService;
import br.ufpa.castanhal.es2.vacinacao.domain.Doenca;
import br.ufpa.castanhal.es2.vacinacao.repository.DoencaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.DoencaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Doenca}.
 */
@Service
@Transactional
public class DoencaServiceImpl implements DoencaService {

    private final Logger log = LoggerFactory.getLogger(DoencaServiceImpl.class);

    private final DoencaRepository doencaRepository;

    private final DoencaMapper doencaMapper;

    public DoencaServiceImpl(DoencaRepository doencaRepository, DoencaMapper doencaMapper) {
        this.doencaRepository = doencaRepository;
        this.doencaMapper = doencaMapper;
    }

    @Override
    public DoencaDTO save(DoencaDTO doencaDTO) {
        log.debug("Request to save Doenca : {}", doencaDTO);
        Doenca doenca = doencaMapper.toEntity(doencaDTO);
        doenca = doencaRepository.save(doenca);
        return doencaMapper.toDto(doenca);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoencaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Doencas");
        return doencaRepository.findAll(pageable)
            .map(doencaMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<DoencaDTO> findOne(Long id) {
        log.debug("Request to get Doenca : {}", id);
        return doencaRepository.findById(id)
            .map(doencaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Doenca : {}", id);
        doencaRepository.deleteById(id);
    }
}
