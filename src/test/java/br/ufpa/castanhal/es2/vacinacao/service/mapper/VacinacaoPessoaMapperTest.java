package br.ufpa.castanhal.es2.vacinacao.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VacinacaoPessoaMapperTest {

    private VacinacaoPessoaMapper vacinacaoPessoaMapper;

    @BeforeEach
    public void setUp() {
        vacinacaoPessoaMapper = new VacinacaoPessoaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(vacinacaoPessoaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(vacinacaoPessoaMapper.fromId(null)).isNull();
    }
}
