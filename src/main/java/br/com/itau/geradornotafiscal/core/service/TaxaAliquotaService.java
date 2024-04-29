package br.com.itau.geradornotafiscal.core.service;

import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacaoPJ;

public interface TaxaAliquotaService {
    Boolean regimeTributario(RegimeTributacaoPJ regimeTributacaoPJ);
    Double calculaPercentualAliquota(Double valorTotalItens);
}
