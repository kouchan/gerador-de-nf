package br.com.itau.geradornotafiscal.core.usecase.impl;

import br.com.itau.geradornotafiscal.core.model.*;

import java.util.List;

public class TestUtils {
    public static Pedido gerarPedidoPJ(RegimeTributacaoPJ regimeTributacaoPJ, Finalidade finalidade, 
                                Regiao regiao, double valorUnitario, int quantidadeItem,
                                       double valorTotalEsperado, double valorFrete) {
        // Create and add Endereco to the Destinatario
        Endereco endereco = Endereco.builder().
                finalidade(finalidade)
                .regiao(regiao)
                .build();
        
        Destinatario destinatario = Destinatario.builder()
                .tipoPessoa(TipoPessoa.JURIDICA)
                .regimeTributacaoPJ(regimeTributacaoPJ)
                .enderecos(List.of(endereco))
                .build();

        // Create and add items to the Pedido
        Item item = Item.builder().valorUnitario(valorUnitario).quantidade(quantidadeItem).build();

        Pedido pedido = Pedido.builder()
                .valorTotalItens(valorTotalEsperado)
                .valorFrete(valorFrete)
                .destinatario(destinatario)
                .itens(List.of(item)).build();

        return pedido;
    }

    public static Pedido gerarPedidoPF( Finalidade finalidade,
                                       Regiao regiao, double valorUnitario, int quantidadeItem,
                                       double valorTotalEsperado, double valorFrete) {
        // Create and add Endereco to the Destinatario
        Endereco endereco = Endereco.builder().
                finalidade(finalidade)
                .regiao(regiao)
                .build();

        Destinatario destinatario = Destinatario.builder()
                .tipoPessoa(TipoPessoa.FISICA)
                .enderecos(List.of(endereco))
                .build();

        // Create and add items to the Pedido
        Item item = Item.builder().valorUnitario(valorUnitario).quantidade(quantidadeItem).build();

        Pedido pedido = Pedido.builder()
                .valorTotalItens(valorTotalEsperado)
                .valorFrete(valorFrete)
                .destinatario(destinatario)
                .itens(List.of(item)).build();

        return pedido;
    }
}
