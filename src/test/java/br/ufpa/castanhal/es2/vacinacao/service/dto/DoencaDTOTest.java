package br.ufpa.castanhal.es2.vacinacao.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.ufpa.castanhal.es2.vacinacao.web.rest.TestUtil;

public class DoencaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoencaDTO.class);
        DoencaDTO doencaDTO1 = new DoencaDTO();
        doencaDTO1.setId(1L);
        DoencaDTO doencaDTO2 = new DoencaDTO();
        assertThat(doencaDTO1).isNotEqualTo(doencaDTO2);
        doencaDTO2.setId(doencaDTO1.getId());
        assertThat(doencaDTO1).isEqualTo(doencaDTO2);
        doencaDTO2.setId(2L);
        assertThat(doencaDTO1).isNotEqualTo(doencaDTO2);
        doencaDTO1.setId(null);
        assertThat(doencaDTO1).isNotEqualTo(doencaDTO2);
    }
}
