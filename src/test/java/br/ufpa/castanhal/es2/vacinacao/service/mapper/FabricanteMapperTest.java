package br.ufpa.castanhal.es2.vacinacao.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FabricanteMapperTest {

    private FabricanteMapper fabricanteMapper;

    @BeforeEach
    public void setUp() {
        fabricanteMapper = new FabricanteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(fabricanteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fabricanteMapper.fromId(null)).isNull();
    }
}
