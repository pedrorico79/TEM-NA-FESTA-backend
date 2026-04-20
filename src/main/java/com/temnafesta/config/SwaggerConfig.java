package com.temnafesta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String logoBase64 = "";
        try {
            byte[] imageBytes = getClass()
                    .getResourceAsStream("/static/images/logo.png")
                    .readAllBytes();
            logoBase64 = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            // sem logo se não encontrar
        }

        return new OpenAPI()
                .info(new Info()
                        .title("Tem na Festa API")
                        .version("1.0.0")
                        .description("""
                                <img src='data:image/png;base64,%s' width='150'/>

                                API desenvolvida para a microempresa **Tem na Festa – Chocolate**,
                                com foco na organização das encomendas e na gestão dos pedidos.

                                ## Contexto
                                O sistema foi criado para apoiar a gestão de pedidos realizados por clientes,
                                permitindo a visualização estruturada das demandas produtivas.

                                ## Funcionalidades
                                - Cadastro e gerenciamento de encomendas
                                - Cadastro de clientes e endereços
                                - Organização de pedidos por data de retirada
                                - Visualização das quantidades de produção
                                - Gerenciamento de cardápios e campanhas

                                ## Objetivo
                                Reduzir a sobrecarga da organização manual e melhorar a eficiência
                                na organização dos pedidos da microempresa.
                                """.formatted(logoBase64))

                )
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação Geral do Projeto")
                        .url("https://bandteccom.sharepoint.com/:w:/s/Grupo3-1Semestre/IQAd6brXv5wrQaafqbpnFvm8ASw_-zbYWbATua5o_eBaybw?e=fMQnDx"))
                ;
    }

    @Bean
    public GroupedOpenApi usuarioApi() {
        return GroupedOpenApi.builder()
                .group("1 - Usuários")
                .pathsToMatch("/usuarios/**")
                .addOpenApiMethodFilter(method -> true)
                .build();
    }

    @Bean
    public GroupedOpenApi clienteApi() {
        return GroupedOpenApi.builder()
                .group("2 - Clientes")
                .pathsToMatch("/clientes/**")
                .addOpenApiMethodFilter(method -> true)
                .build();
    }

    @Bean
    public GroupedOpenApi enderecoApi() {
        return GroupedOpenApi.builder()
                .group("3 - Endereços")
                .pathsToMatch("/enderecos/**")
                .addOpenApiMethodFilter(method -> true)
                .build();
    }

    @Bean
    public GroupedOpenApi pedidoApi() {
        return GroupedOpenApi.builder()
                .group("4 - Pedidos")
                .pathsToMatch("/pedidos/**")
                .addOpenApiMethodFilter(method -> true)
                .build();
    }

    @Bean
    public GroupedOpenApi pedidoProdutoApi() {
        return GroupedOpenApi.builder()
                .group("5 - Pedido-Produto")
                .pathsToMatch("/pedido-produto/**")
                .addOpenApiMethodFilter(method -> true)
                .build();
    }

    @Bean
    public GroupedOpenApi produtoApi() {
        return GroupedOpenApi.builder()
                .group("6 - Produtos")
                .pathsToMatch("/produtos/**")
                .addOpenApiMethodFilter(method -> true)
                .build();
    }

    @Bean
    public GroupedOpenApi campanhaApi() {
        return GroupedOpenApi.builder()
                .group("7 - Campanhas")
                .pathsToMatch("/campanhas/**")
                .addOpenApiMethodFilter(method -> true)
                .build();
    }

    @Bean
    public GroupedOpenApi pagamentoApi() {
        return GroupedOpenApi.builder()
                .group("8 - Pagamentos")
                .pathsToMatch("/pagamentos/**")
                .addOpenApiMethodFilter(method -> true)
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("0 - Todos")
                .pathsToMatch("/**")
                .build();
    }
}