package br.ufpa.castanhal.es2.vacinacao.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DoencaMapperTest {

    private DoencaMapper doencaMapper;

    @BeforeEach
    public void setUp() {
        doencaMapper = new DoencaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(doencaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(doencaMapper.fromId(null)).isNull();
    }
}
