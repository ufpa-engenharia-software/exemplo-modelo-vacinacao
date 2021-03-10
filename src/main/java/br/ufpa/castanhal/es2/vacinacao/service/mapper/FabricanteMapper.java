package br.ufpa.castanhal.es2.vacinacao.service.mapper;


import br.ufpa.castanhal.es2.vacinacao.domain.*;
import br.ufpa.castanhal.es2.vacinacao.service.dto.FabricanteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fabricante} and its DTO {@link FabricanteDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaisMapper.class, VacinaMapper.class})
public interface FabricanteMapper extends EntityMapper<FabricanteDTO, Fabricante> {

    @Mapping(source = "pais.id", target = "paisId")
    @Mapping(source = "pais.nome", target = "paisNome")
    FabricanteDTO toDto(Fabricante fabricante);

    @Mapping(source = "paisId", target = "pais")
    @Mapping(target = "removeVacinas", ignore = true)
    Fabricante toEntity(FabricanteDTO fabricanteDTO);

    default Fabricante fromId(Long id) {
        if (id == null) {
            return null;
        }
        Fabricante fabricante = new Fabricante();
        fabricante.setId(id);
        return fabricante;
    }
}
