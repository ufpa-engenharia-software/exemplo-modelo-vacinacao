package br.ufpa.castanhal.es2.vacinacao.service.impl;

import br.ufpa.castanhal.es2.vacinacao.service.VacinacaoPessoaService;
import br.ufpa.castanhal.es2.vacinacao.domain.VacinacaoPessoa;
import br.ufpa.castanhal.es2.vacinacao.repository.VacinacaoPessoaRepository;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaDTO;
import br.ufpa.castanhal.es2.vacinacao.service.mapper.VacinacaoPessoaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link VacinacaoPessoa}.
 */
@Service
@Transactional
public class VacinacaoPessoaServiceImpl implements VacinacaoPessoaService {

    private final Logger log = LoggerFactory.getLogger(VacinacaoPessoaServiceImpl.class);

    private final VacinacaoPessoaRepository vacinacaoPessoaRepository;

    private final VacinacaoPessoaMapper vacinacaoPessoaMapper;

    public VacinacaoPessoaServiceImpl(VacinacaoPessoaRepository vacinacaoPessoaRepository, VacinacaoPessoaMapper vacinacaoPessoaMapper) {
        this.vacinacaoPessoaRepository = vacinacaoPessoaRepository;
        this.vacinacaoPessoaMapper = vacinacaoPessoaMapper;
    }

    @Override
    public VacinacaoPessoaDTO save(VacinacaoPessoaDTO vacinacaoPessoaDTO) {
        log.debug("Request to save VacinacaoPessoa : {}", vacinacaoPessoaDTO);
        VacinacaoPessoa vacinacaoPessoa = vacinacaoPessoaMapper.toEntity(vacinacaoPessoaDTO);
        vacinacaoPessoa = vacinacaoPessoaRepository.save(vacinacaoPessoa);
        return vacinacaoPessoaMapper.toDto(vacinacaoPessoa);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VacinacaoPessoaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VacinacaoPessoas");
        return vacinacaoPessoaRepository.findAll(pageable)
            .map(vacinacaoPessoaMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<VacinacaoPessoaDTO> findOne(Long id) {
        log.debug("Request to get VacinacaoPessoa : {}", id);
        return vacinacaoPessoaRepository.findById(id)
            .map(vacinacaoPessoaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VacinacaoPessoa : {}", id);
        vacinacaoPessoaRepository.deleteById(id);
    }
}
