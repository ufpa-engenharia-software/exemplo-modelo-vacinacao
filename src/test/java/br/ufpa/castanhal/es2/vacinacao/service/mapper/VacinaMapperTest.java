package br.ufpa.castanhal.es2.vacinacao.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VacinaMapperTest {

    private VacinaMapper vacinaMapper;

    @BeforeEach
    public void setUp() {
        vacinaMapper = new VacinaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(vacinaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(vacinaMapper.fromId(null)).isNull();
    }
}
