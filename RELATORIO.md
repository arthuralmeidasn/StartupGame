# Relatório de Refatoração - Startup Game

## 1. Resumo do Projeto
Este projeto consistiu na refatoração de um jogo monolítico para uma arquitetura modular, orientada a objetos e persistente. O objetivo foi aplicar boas práticas de design de software (SOLID), separar responsabilidades e implementar padrões de projeto exigidos.

## 2. Itens Obrigatórios Implementados

### A. Arquitetura e Separação de Responsabilidades
- **O que foi feito:** O arquivo único `Main.java` foi desmontado.
- **Estrutura:**
  - `model/`: Contém a entidade `Startup` e os Value Objects.
  - `engine/`: Contém a lógica de controle do jogo (`GameEngine`).
  - `ui/`: Contém a interação com o usuário (`ConsoleApp`).
  - `config/`: Centraliza a leitura de configurações.
  - `persistence/`: Gerencia o acesso ao banco de dados.

### B. Padrão Strategy (Decisões)
- **Implementação:** Substituição do `switch` case gigante por classes especialistas.
- **Classes:** Criada a interface `DecisaoStrategy` e as implementações: `MarketingStrategy`, `EquipeStrategy`, `ProdutoStrategy`, `InvestidoresStrategy` e `CortarCustosStrategy`.
- **Benefício:** A lógica de cálculo dos deltas (mudanças) está isolada do motor do jogo.

### C. Persistência de Dados (H2)
- **Implementação:** Uso do banco de dados H2 em modo arquivo (`file:./data/game`).
- **Repositório:** Classe `StartupRepository` implementada para traduzir objetos Java para SQL (comando `MERGE` para salvar/atualizar) e SQL para Java (reconstrução de VOs).
- **Setup:** Criação automática das tabelas via `schema.sql` embutido no código.

### D. Value Objects (VOs)
- **Uso:** Implementação e uso estrito das classes `Dinheiro` e `Humor` para evitar "Obsessão por Tipos Primitivos". Toda a matemática financeira e de atributos é feita através de métodos encapsulados (`.somar`, `.aumentar`, etc.).

### E. Configuração Externa
- **Arquivo:** Leitura do `game.properties` implementada na classe `Config` com tratamento de exceções caso o arquivo não seja encontrado.

## 3. Melhorias Adicionais
- **Tratamento de Exceções:** Adicionado em todas as camadas críticas (Banco de Dados, Configuração e Regras de Negócio).
- **Interface de Console:** Melhoria na robustez da leitura de dados (`Scanner`) para evitar quebras com entradas inválidas.
- **Autosave:** O jogo salva o progresso automaticamente ao fim de cada rodada.