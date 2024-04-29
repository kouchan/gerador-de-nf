package br.com.itau.geradornotafiscal.core.service.aliquota;

import br.com.itau.geradornotafiscal.core.model.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.core.service.TaxaAliquotaService;
import org.springframework.stereotype.Component;

@Component
public class RegimeTributacaoLucroReal implements TaxaAliquotaService {
    @Override
    public Boolean regimeTributario(RegimeTributacaoPJ regimeTributacaoPJ) {
        return RegimeTributacaoPJ.LUCRO_REAL.equals(regimeTributacaoPJ);
    }

    @Override
    public Double calculaPercentualAliquota(Double valorTotalItens) {
        if (valorTotalItens < 1000) {
            return 0.03;
        } else if (valorTotalItens <= 2000) {
            return 0.09;
        } else if (valorTotalItens <= 5000) {
            return 0.15;
        } else {
            return 0.20;
        }
    }
}
