package br.com.itau.geradornotafiscal.core.usecase.impl;

import br.com.itau.geradornotafiscal.core.model.*;
import br.com.itau.geradornotafiscal.core.model.enums.Finalidade;
import br.com.itau.geradornotafiscal.core.model.enums.Regiao;
import br.com.itau.geradornotafiscal.core.model.enums.RegimeTributacaoPJ;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;

public class TestUtils {


    @SneakyThrows
    public static Pedido gerarPedidoPJ(RegimeTributacaoPJ regimeTributacaoPJ,
                                       Regiao regiao, int quantidadeItem) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        InputStream inJson = Pedido.class.getResourceAsStream("src/test/resources/pedido/pj/teste-pj-"
                +regimeTributacaoPJ.name().toLowerCase()
                +"-"+quantidadeItem
                +"-"+regiao.name().toLowerCase()
                +".json");
        Pedido pedido = new ObjectMapper().readValue(inJson, Pedido.class);
        return pedido;
    }

    @SneakyThrows
    public static Pedido gerarPedidoPF(Finalidade finalidade, Regiao regiao, Integer quantidadeItem) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        // Create and add Endereco to the Destinatario
        InputStream inJson = Pedido.class.getResourceAsStream("/teste-pf"
                +"-"+quantidadeItem
                +"-"+regiao.name().toLowerCase()
                +".json");
        Pedido pedido = mapper.readValue(inJson, Pedido.class);

        return pedido;
    }
}
