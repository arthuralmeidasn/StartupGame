CREATE TABLE IF NOT EXISTS startup (
    nome VARCHAR(255) PRIMARY KEY,
    caixa DECIMAL(20, 2) NOT NULL,
    receita_base DECIMAL(20, 2) NOT NULL,
    reputacao INT NOT NULL,
    moral INT NOT NULL
);

CREATE TABLE IF NOT EXISTS rodada (
    id IDENTITY PRIMARY KEY,
    startup_nome VARCHAR(255) NOT NULL,
    numero_rodada INT NOT NULL,
    
    FOREIGN KEY (startup_nome) REFERENCES startup(nome)
);


CREATE TABLE IF NOT EXISTS decisao_aplicada (
    id IDENTITY PRIMARY KEY,
    rodada_id BIGINT NOT NULL,
    tipo_decisao VARCHAR(50) NOT NULL,
    
    FOREIGN KEY (rodada_id) REFERENCES rodada(id)
);