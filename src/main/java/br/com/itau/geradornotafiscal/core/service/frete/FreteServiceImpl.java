package br.com.itau.geradornotafiscal.core.service.frete;

import br.com.itau.geradornotafiscal.core.model.Pedido;
import br.com.itau.geradornotafiscal.core.model.enums.Regiao;
import br.com.itau.geradornotafiscal.core.service.FreteService;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class FreteServiceImpl implements FreteService {

    @Override
    public Double calculaValorFreteComPercentual(Pedido pedido, Regiao regiao) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.00", symbols);
        double valorFrete = pedido.getValorFrete();
        double valorFreteComPercentual = 0;

        if (regiao == Regiao.NORTE) {
            valorFreteComPercentual = valorFrete * 1.08;
        } else if (regiao == Regiao.NORDESTE) {
            valorFreteComPercentual = valorFrete * 1.085;
        } else if (regiao == Regiao.CENTRO_OESTE) {
            valorFreteComPercentual = valorFrete * 1.07;
        } else if (regiao == Regiao.SUDESTE) {
            valorFreteComPercentual = valorFrete * 1.048;
        } else if (regiao == Regiao.SUL) {
            valorFreteComPercentual = valorFrete * 1.06;
        }
        return Double.parseDouble(decimalFormat.format(valorFreteComPercentual));
    }
}
