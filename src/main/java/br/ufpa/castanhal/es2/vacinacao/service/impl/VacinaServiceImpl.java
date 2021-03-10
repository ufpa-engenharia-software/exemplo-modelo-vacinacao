package br.ufpa.castanhal.es2.vacinacao.service.impl;

import br.ufpa.castanhal.es2.vacinacao.service.VacinaService;
import br.ufpa.castanhal.es2.vacinacao.domain.Vacina;
import br.ufpa.castanhal.es2.vacinacao.repository.VacinaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.VacinaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Vacina}.
 */
@Service
@Transactional
public class VacinaServiceImpl implements VacinaService {

    private final Logger log = LoggerFactory.getLogger(VacinaServiceImpl.class);

    private final VacinaRepository vacinaRepository;

    private final VacinaMapper vacinaMapper;

    public VacinaServiceImpl(VacinaRepository vacinaRepository, VacinaMapper vacinaMapper) {
        this.vacinaRepository = vacinaRepository;
        this.vacinaMapper = vacinaMapper;
    }

    @Override
    public VacinaDTO save(VacinaDTO vacinaDTO) {
        log.debug("Request to save Vacina : {}", vacinaDTO);
        Vacina vacina = vacinaMapper.toEntity(vacinaDTO);
        vacina = vacinaRepository.save(vacina);
        return vacinaMapper.toDto(vacina);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VacinaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vacinas");
        return vacinaRepository.findAll(pageable)
            .map(vacinaMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<VacinaDTO> findOne(Long id) {
        log.debug("Request to get Vacina : {}", id);
        return vacinaRepository.findById(id)
            .map(vacinaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vacina : {}", id);
        vacinaRepository.deleteById(id);
    }
}
