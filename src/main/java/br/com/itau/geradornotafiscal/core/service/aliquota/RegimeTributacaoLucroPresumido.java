package br.com.itau.geradornotafiscal.core.service.aliquota;

import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacao;
import br.com.itau.geradornotafiscal.core.model.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.core.service.TaxaAliquotaService;
import org.springframework.stereotype.Component;

@Component
public class RegimeTributacaoLucroPresumido implements TaxaAliquotaService {
    @Override
    public Boolean regimeTributario(RegimeTributacao regimeTributacao, TipoPessoa tipoPessoa) {
        return RegimeTributacao.LUCRO_PRESUMIDO.equals(regimeTributacao) && TipoPessoa.JURIDICA.equals(tipoPessoa);
    }

    @Override
    public Double calculaPercentualAliquota(Double valorTotalItens) {
        if (valorTotalItens < 1000) {
            return 0.03;
        } else if (valorTotalItens <= 2000) {
            return 0.09;
        } else if (valorTotalItens <= 5000) {
            return 0.16;
        } else {
            return 0.20;
        }
    }
}
