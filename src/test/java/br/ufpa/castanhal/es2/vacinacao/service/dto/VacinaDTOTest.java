package br.ufpa.castanhal.es2.vacinacao.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.ufpa.castanhal.es2.vacinacao.web.rest.TestUtil;

public class VacinaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VacinaDTO.class);
        VacinaDTO vacinaDTO1 = new VacinaDTO();
        vacinaDTO1.setId(1L);
        VacinaDTO vacinaDTO2 = new VacinaDTO();
        assertThat(vacinaDTO1).isNotEqualTo(vacinaDTO2);
        vacinaDTO2.setId(vacinaDTO1.getId());
        assertThat(vacinaDTO1).isEqualTo(vacinaDTO2);
        vacinaDTO2.setId(2L);
        assertThat(vacinaDTO1).isNotEqualTo(vacinaDTO2);
        vacinaDTO1.setId(null);
        assertThat(vacinaDTO1).isNotEqualTo(vacinaDTO2);
    }
}
