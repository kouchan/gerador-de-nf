package br.com.itau.geradornotafiscal.core.service.frete;

import br.com.itau.geradornotafiscal.core.model.Pedido;
import br.com.itau.geradornotafiscal.core.service.FreteService;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Component
public class FreteRegiaoSulService implements FreteService {
    @Override
    public Double calculaValorFreteComPercentual(Pedido pedido) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.00", symbols);
        double valorFrete = pedido.getValorFrete();

        return Double.parseDouble(decimalFormat.format(valorFrete * 1.06));
    }
}
