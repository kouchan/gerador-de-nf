package br.com.itau.geradornotafiscal.core.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
public class Pedido {
	@JsonProperty("id_pedido")
	private int idPedido;

	@JsonProperty("data")
	private LocalDate data;

	@JsonProperty("valor_total_itens")
	private double valorTotalItens;

	@JsonProperty("valor_frete")
	private double valorFrete;

	@JsonProperty("itens")
	private List<Item> itens;

	@JsonProperty("destinatario")
	private Destinatario destinatario;
}
