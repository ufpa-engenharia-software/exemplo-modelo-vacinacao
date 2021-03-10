package br.ufpa.castanhal.es2.vacinacao.service.mapper;


import br.ufpa.castanhal.es2.vacinacao.domain.*;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinacaoPessoaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link VacinacaoPessoa} and its DTO {@link VacinacaoPessoaDTO}.
 */
@Mapper(componentModel = "spring", uses = {PessoaMapper.class, VacinaMapper.class, FabricanteMapper.class})
public interface VacinacaoPessoaMapper extends EntityMapper<VacinacaoPessoaDTO, VacinacaoPessoa> {

    @Mapping(source = "pessoa.id", target = "pessoaId")
    @Mapping(source = "pessoa.nome", target = "pessoaNome")
    @Mapping(source = "vacina.id", target = "vacinaId")
    @Mapping(source = "vacina.nome", target = "vacinaNome")
    @Mapping(source = "fabricante.id", target = "fabricanteId")
    @Mapping(source = "fabricante.nome", target = "fabricanteNome")
    VacinacaoPessoaDTO toDto(VacinacaoPessoa vacinacaoPessoa);

    @Mapping(source = "pessoaId", target = "pessoa")
    @Mapping(source = "vacinaId", target = "vacina")
    @Mapping(source = "fabricanteId", target = "fabricante")
    VacinacaoPessoa toEntity(VacinacaoPessoaDTO vacinacaoPessoaDTO);

    default VacinacaoPessoa fromId(Long id) {
        if (id == null) {
            return null;
        }
        VacinacaoPessoa vacinacaoPessoa = new VacinacaoPessoa();
        vacinacaoPessoa.setId(id);
        return vacinacaoPessoa;
    }
}
