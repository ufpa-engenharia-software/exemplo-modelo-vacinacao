package br.ufpa.castanhal.es2.vacinacao.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaisMapperTest {

    private PaisMapper paisMapper;

    @BeforeEach
    public void setUp() {
        paisMapper = new PaisMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paisMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paisMapper.fromId(null)).isNull();
    }
}
