package br.com.itau.geradornotafiscal.core.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Destinatario {
	@JsonProperty("nome")
	private String nome;

	@JsonProperty("tipo_pessoa")
	private TipoPessoa tipoPessoa;

	@JsonProperty("regime_tributacao")
	private RegimeTributacaoPJ regimeTributacaoPJ;

	@JsonProperty("documentos")
	private List<Documento> documentos;

	@JsonProperty("enderecos")
	private List<Endereco> enderecos;

}




