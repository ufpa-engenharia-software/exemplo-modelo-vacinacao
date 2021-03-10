package br.ufpa.castanhal.es2.vacinacao.service.mapper;


import br.ufpa.castanhal.es2.vacinacao.domain.*;
import br.ufpa.castanhal.es2.vacinacao.service.dto.DoencaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Doenca} and its DTO {@link DoencaDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaisMapper.class})
public interface DoencaMapper extends EntityMapper<DoencaDTO, Doenca> {

    @Mapping(source = "paisPrimeiroCaso.id", target = "paisPrimeiroCasoId")
    @Mapping(source = "paisPrimeiroCaso.nome", target = "paisPrimeiroCasoNome")
    DoencaDTO toDto(Doenca doenca);

    @Mapping(source = "paisPrimeiroCasoId", target = "paisPrimeiroCaso")
    Doenca toEntity(DoencaDTO doencaDTO);

    default Doenca fromId(Long id) {
        if (id == null) {
            return null;
        }
        Doenca doenca = new Doenca();
        doenca.setId(id);
        return doenca;
    }
}
