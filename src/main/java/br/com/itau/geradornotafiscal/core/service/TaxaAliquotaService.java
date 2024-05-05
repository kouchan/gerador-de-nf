package br.com.itau.geradornotafiscal.core.service;

import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacao;
import br.com.itau.geradornotafiscal.core.model.enums.TipoPessoa;

public interface TaxaAliquotaService {
    Boolean regimeTributario(RegimeTributacao regimeTributacao, TipoPessoa tipoPessoa);
    Double calculaPercentualAliquota(Double valorTotalItens);
}
