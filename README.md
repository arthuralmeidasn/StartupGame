# Projeto Final — Refatoração Startup Game

## Sobre o Projeto
Este é o projeto final entregue para a atividade de refatoração do Startup Game.
O objetivo foi transformar um código monolítico em uma arquitetura robusta, aplicando:
- **Programação Orientada a Objetos** (Encapsulamento, Polimorfismo).
- **Padrão de Projeto Strategy** para as decisões de negócio.
- **Value Objects (VOs)** para manipulação segura de dinheiro e atributos.
- **Persistência de Dados** com banco de dados H2 e JDBC.

**Versionamento no Git**:
O projeto foi versionado com commits incrementais e semânticos, documentando a evolução da refatoração.

---

## Estrutura do Projeto (Implementada)
A organização final dos pacotes segue a arquitetura MVC/Layers solicitada:

```text
src/
  config/Config.java           # Leitura do game.properties
  model/
    Startup.java               # Entidade principal
    Deltas.java                # Record para transporte de dados (Strategy)
    enums/TipoDecisao.java     # Enum das opções de jogo
    vo/                        # Value Objects (Dinheiro, Humor, Percentual)
  actions/
    DecisaoStrategy.java       # Interface do padrão Strategy
    MarketingStrategy.java     # Implementação Concreta
    EquipeStrategy.java        # Implementação Concreta
    ProdutoStrategy.java       # Implementação Concreta
    InvestidoresStrategy.java  # Implementação Concreta
    CortarCustosStrategy.java  # Implementação Concreta
  persistence/
    DataSourceProvider.java    # Conexão JDBC com H2 (modo file)
    StartupRepository.java     # Salvar/Carregar (MERGE/SELECT)
  engine/GameEngine.java       # Motor do jogo e loop principal
  ui/ConsoleApp.java           # Interface com usuário
  Main.java                    # Ponto de entrada
resources/
  game.properties              # Configurações (8 rodadas, 3 decisões)
  schema.sql                   # Criação das tabelas
````

-----

## Como Rodar

### Pré-requisitos

  - Java JDK 17+.
  - Driver do H2 Database (arquivo `.jar`) adicionado ao projeto.

### Opção 1: VS Code

O projeto contém o arquivo `.vscode/launch.json` configurado para rodar no Terminal Integrado.

1.  Abra o arquivo `src/Main.java`.
2.  Pressione `F5` ou clique em **Run**.

### Opção 2: Terminal

Para rodar manualmente, certifique-se de incluir a biblioteca do H2 no classpath (`-cp`).

**Compilar:**

```bash
# Crie a pasta para os arquivos compilados
mkdir bin

# Compile incluindo o jar do H2 (ajuste o nome do arquivo jar conforme sua versão)
javac -d bin -cp "src;lib/h2-2.2.224.jar" src/Main.java src/config/*.java src/model/*.java src/model/vo/*.java src/model/enums/*.java src/engine/*.java src/ui/*.java src/persistence/*.java src/actions/*.java
```

**Executar:**

```bash
java -cp "bin;lib/h2-2.2.224.jar" Main
```

-----

## Configurações

O arquivo `src/game.properties` define as regras:

  - `total.rodadas=8`
  - `max.decisoes.por.rodada=3`

-----

## Banco de Dados e Persistência

O sistema de persistência foi implementado com sucesso.

  - **Banco H2 (arquivo)**: Os dados são salvos na pasta `./data`.
  - **Autosave**: O jogo salva o progresso automaticamente ao final de cada rodada.
  - **Continuar Jogo**: Ao abrir o programa, ele verifica se existe um save. Se sim, carrega as startups e continua da rodada onde parou.
  - **Reset**: Para reiniciar, basta apagar a pasta `data` ou o arquivo `.db`.

-----

## Funcionalidades Entregues

  - Separação de responsabilidades (Main, Engine, Model).
  - Leitura de `game.properties`.
  - Persistência completa (Salvar/Carregar) com H2.
  - Padrão Strategy para todas as 5 decisões.
  - Uso obrigatório de Value Objects (Dinheiro, Humor).
  - Tratamento de Exceções.
  - Interface de Console robusta.

