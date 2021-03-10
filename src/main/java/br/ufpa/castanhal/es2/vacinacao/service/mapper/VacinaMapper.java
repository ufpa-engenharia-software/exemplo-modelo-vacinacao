package br.ufpa.castanhal.es2.vacinacao.service.mapper;


import br.ufpa.castanhal.es2.vacinacao.domain.*;
import br.ufpa.castanhal.es2.vacinacao.service.dto.VacinaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vacina} and its DTO {@link VacinaDTO}.
 */
@Mapper(componentModel = "spring", uses = {DoencaMapper.class})
public interface VacinaMapper extends EntityMapper<VacinaDTO, Vacina> {

    @Mapping(source = "doenca.id", target = "doencaId")
    @Mapping(source = "doenca.nome", target = "doencaNome")
    VacinaDTO toDto(Vacina vacina);

    @Mapping(source = "doencaId", target = "doenca")
    @Mapping(target = "fabricantes", ignore = true)
    @Mapping(target = "removeFabricantes", ignore = true)
    Vacina toEntity(VacinaDTO vacinaDTO);

    default Vacina fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vacina vacina = new Vacina();
        vacina.setId(id);
        return vacina;
    }
}
