package br.com.itau.geradornotafiscal.core.service.aliquota;

import br.com.itau.geradornotafiscal.core.model.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.core.service.TaxaAliquotaService;
import org.springframework.stereotype.Component;

@Component
public class RegimeTributacaoPF implements TaxaAliquotaService{

    @Override
    public Boolean regimeTributario(RegimeTributacaoPJ regimeTributacaoPJ) {
        return false;
    }

    @Override
    public Double calculaPercentualAliquota(Double valorTotalItens) {
        if (valorTotalItens < 500) {
            return 0d;
        } else if (valorTotalItens <= 2000) {
            return  0.12d;
        } else if (valorTotalItens <= 3500) {
            return 0.15d;
        } else {
            return 0.17d;
        }
    }
}
