package com.temnafesta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tem na Festa API")
                        .version("1.0.0")
                        .description("""
                                <img src='/images/logo.png' width='150'/>

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
                                """)

                )
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação Geral do Projeto")
                        .url("https://bandteccom.sharepoint.com/:w:/s/Grupo3-1Semestre/IQAd6brXv5wrQaafqbpnFvm8ASw_-zbYWbATua5o_eBaybw?e=fMQnDx"))
                .tags(List.of(
                        new Tag().name("Usuários").description("Gerenciamento de usuários do sistema"),
                        new Tag().name("Clientes").description("Cadastro e gerenciamento de clientes"),
                        new Tag().name("Endereços").description("Endereços vinculados aos clientes"),
                        new Tag().name("Pedidos").description("Gestão de pedidos de encomenda"),
                        new Tag().name("Pedido-Produto").description("Itens (produtos) vinculados a um pedido"),
                        new Tag().name("Produtos").description("Cadastro de produtos disponíveis"),
                        new Tag().name("Cardápios").description("Cardápios agrupadores de produtos"),
                        new Tag().name("Cardápio-Produto").description("Vínculo entre cardápios e produtos"),
                        new Tag().name("Campanhas").description("Campanhas promocionais vinculadas a cardápios"),
                        new Tag().name("Pagamentos").description("Pagamentos associados aos pedidos")
                ));
    }
}