package br.ufpa.castanhal.es2.vacinacao.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.ufpa.castanhal.es2.vacinacao.web.rest.TestUtil;

public class VacinacaoPessoaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VacinacaoPessoaDTO.class);
        VacinacaoPessoaDTO vacinacaoPessoaDTO1 = new VacinacaoPessoaDTO();
        vacinacaoPessoaDTO1.setId(1L);
        VacinacaoPessoaDTO vacinacaoPessoaDTO2 = new VacinacaoPessoaDTO();
        assertThat(vacinacaoPessoaDTO1).isNotEqualTo(vacinacaoPessoaDTO2);
        vacinacaoPessoaDTO2.setId(vacinacaoPessoaDTO1.getId());
        assertThat(vacinacaoPessoaDTO1).isEqualTo(vacinacaoPessoaDTO2);
        vacinacaoPessoaDTO2.setId(2L);
        assertThat(vacinacaoPessoaDTO1).isNotEqualTo(vacinacaoPessoaDTO2);
        vacinacaoPessoaDTO1.setId(null);
        assertThat(vacinacaoPessoaDTO1).isNotEqualTo(vacinacaoPessoaDTO2);
    }
}
