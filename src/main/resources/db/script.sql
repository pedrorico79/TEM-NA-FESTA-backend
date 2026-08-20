USE tem_na_festa;

CREATE TABLE endereco (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          cep VARCHAR(8) NOT NULL,
                          logradouro VARCHAR(150) NOT NULL,
                          numero VARCHAR(10) NOT NULL,
                          complemento VARCHAR(100),
                          bairro VARCHAR(100) NOT NULL,
                          cidade VARCHAR(100) NOT NULL,
                          estado CHAR(2) NOT NULL
);

CREATE TABLE cliente (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         telefone VARCHAR(20),
                         whatsapp VARCHAR(20),
                         instagram VARCHAR(50),
                         data_cadastro DATE NOT NULL,
                         anotacoes TEXT,
                         endereco_id INT, -- Endereço principal/residencial
                         is_ativo BOOLEAN NOT NULL DEFAULT TRUE,
                         is_deletado BOOLEAN NOT NULL DEFAULT FALSE,
                         FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE perfil (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(50) NOT NULL,
                        descricao TEXT
);

CREATE TABLE usuario (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         is_ativo BOOLEAN NOT NULL DEFAULT TRUE,
                         is_deletado BOOLEAN NOT NULL DEFAULT FALSE,
                         data_criacao DATETIME NOT NULL,
                         perfil_id INT NOT NULL,
                         FOREIGN KEY (perfil_id) REFERENCES perfil(id)
);

CREATE TABLE evento (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        data_inicio DATE NOT NULL,
                        data_fim DATE NOT NULL,
                        is_ativo BOOLEAN NOT NULL DEFAULT TRUE,
                        is_deletado BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE status_producao (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 nome VARCHAR(50) NOT NULL
);

CREATE TABLE pedido (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        data_pedido DATETIME NOT NULL,
                        data_entrega DATETIME NOT NULL,
                        valor_total DECIMAL(10,2) NOT NULL,
                        taxa_entrega DECIMAL(10,2) DEFAULT 0.00,
                        observacao TEXT,
                        status_producao_id INT NOT NULL,
                        cliente_id INT NOT NULL,
                        usuario_id INT NOT NULL,
                        evento_id INT NULL, -- Opcional: Nem todo pedido pertence a um evento
                        endereco_entrega_id INT NULL, -- Opcional: Pode ser retirada no local
                        is_ativo BOOLEAN NOT NULL DEFAULT TRUE,
                        is_deletado BOOLEAN NOT NULL DEFAULT FALSE,
                        FOREIGN KEY (cliente_id) REFERENCES cliente(id),
                        FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                        FOREIGN KEY (evento_id) REFERENCES evento(id),
                        FOREIGN KEY (status_producao_id) REFERENCES status_producao(id),
                        FOREIGN KEY (endereco_entrega_id) REFERENCES endereco(id)
);

CREATE TABLE metodo_pagamento (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  nome VARCHAR(50) NOT NULL
);

CREATE TABLE pagamento (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           valor DECIMAL(10,2) NOT NULL,
                           data_pagamento DATETIME NOT NULL,
                           tipo_pagamento VARCHAR(30) NOT NULL DEFAULT 'INTEGRAL', -- Ex: SINAL, QUITACAO, INTEGRAL
                           status_pagamento VARCHAR(30) NOT NULL DEFAULT 'CONFIRMADO', -- Ex: PENDENTE, CONFIRMADO, CANCELADO
                           metodo_pagamento_id INT NOT NULL,
                           pedido_id INT NOT NULL,
                           usuario_id INT NOT NULL,
                           FOREIGN KEY (pedido_id) REFERENCES pedido(id),
                           FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                           FOREIGN KEY (metodo_pagamento_id) REFERENCES metodo_pagamento(id)
);

CREATE TABLE historico_status_pedido (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         data_alteracao DATETIME NOT NULL,
                                         observacao TEXT,
                                         status_producao_id INT NOT NULL,
                                         pedido_id INT NOT NULL,
                                         usuario_id INT NOT NULL,
                                         FOREIGN KEY (pedido_id) REFERENCES pedido(id),
                                         FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                                         FOREIGN KEY (status_producao_id) REFERENCES status_producao(id)
);

CREATE TABLE produto (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         descricao TEXT,
                         preco_venda DECIMAL(10,2) NOT NULL,
                         is_ativo BOOLEAN DEFAULT TRUE,
                         is_deletado BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE item_pedido (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             pedido_id INT NOT NULL,
                             produto_id INT NOT NULL,
                             quantidade INT NOT NULL,
                             preco_unitario DECIMAL(10,2) NOT NULL,
                             observacao_item TEXT, -- Ex: "Sem lactose", "Texto: Parabéns Ana"
                             FOREIGN KEY (pedido_id) REFERENCES pedido(id),
                             FOREIGN KEY (produto_id) REFERENCES produto(id)
);

CREATE TABLE lembrete (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          descricao TEXT NOT NULL,
                          data_criacao DATE NOT NULL,
                          data_limite DATE NOT NULL,
                          usuario_id INT NOT NULL,
                          FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);