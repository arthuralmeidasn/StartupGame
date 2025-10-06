# Projeto Final – Refatoração do Startup Game com POO Avançado

## 1. Descrição do Game
O **Startup Game** é uma simulação em turnos (rodadas) onde cada equipe gerencia uma startup.  
Em cada rodada, os jogadores podem tomar **até N decisões** (configurável) dentre opções como **Marketing**, **Equipe**, **Produto**, **Investidores** e **Cortar Custos**.  
As decisões impactam **Caixa**, **Receita Base**, **Reputação** e **Moral** da startup.  
Após o número total de rodadas, o jogo calcula um **score final** e apresenta o **ranking**.

**Ciclo resumido por rodada:**
1. Mostrar indicadores atuais da startup.
2. Selecionar até `max.decisoes.por.rodada` decisões.
3. Aplicar efeitos (custos, bônus, deltas de reputação/moral).
4. Fechar a rodada (entrar receita, aplicar bônus, atualizar bases).
5. Registrar histórico.
6. Avançar até atingir `total.rodadas` e exibir relatório final.

---

## 2. Descrição do Projeto/Objetivo
Refatorar o jogo **Startup Game** aplicando **boas práticas de Programação Orientada a Objetos** e **padrões de projeto**.  
O código fornecido inicialmente é monolítico (arquivo único) e deve ser transformado em um projeto **modular e testável**, com camadas definidas, persistência de dados, padrão **Strategy** (obrigatório) e uso de **Value Objects (VO)** fornecidos.

⚠️ **Versionamento no Git**:  
O projeto deve ser versionado em um repositório Git. O professor deverá ser incluído como colaborador no repositório. O link do projeto deve constar nos entregáveis. O professor fará o clone do repositório para testes das features e arguição do grupo.  
**A frequência, autoria e qualidade dos commits serão avaliadas como parte da nota.**

---

## 3. Descrição do arquivo Main.java recebido
O arquivo **Main.java** contém duas classes no mesmo arquivo: `public class Main` e `class Startup`.  
Este arquivo já implementa o jogo no terminal, com decisões e pontuação.  
A classe `Startup` mantém o estado da startup e a classe `Main` contém o loop do jogo, lógica de aplicação das decisões e exibição dos resultados.

---

## 4. Detalhamento das Tarefas

### 4.1 Tarefas **Obrigatórias**
- **Separação de responsabilidades** (cada classe em seu arquivo).
- **Leitura de configurações** do arquivo `game.properties` (**obrigatório**) com os parâmetros:
  - `total.rodadas`
  - `max.decisoes.por.rodada`
- **Persistência de dados** utilizando **H2** (salvar startups, rodadas e decisões aplicadas).
- Implementação do padrão **Strategy** para decisões.
- **Uso obrigatório** das classes **VO fornecidas** (`Dinheiro`, `Percentual`, `Humor`).

### 4.2 Tarefas **Opcionais** (escolher 2 a 5)
- Implementar **Observer** (eventos do jogo com listeners).
- Implementar **Command** (undo/replay).
- Implementar **State** (máquina de estados).
- Criar **relatórios** avançados ou **exportação CSV**.
- Criar **Bots** (IA simples) para decisões automáticas.
- **Testes JUnit** e seed determinística para resultados reproduzíveis.

---

## 5. Estrutura Recebida no ZIP
O ZIP fornecido já contém a seguinte **estrutura mínima** com *stubs* e **classes VO prontas**:

```
src/
  config/Config.java
  model/Startup.java
  model/Deltas.java
  model/vo/Dinheiro.java
  model/vo/Percentual.java
  model/vo/Humor.java
  actions/DecisaoStrategy.java
  actions/DecisaoFactory.java
  actions/[estratégias].java
  persistence/DataSourceProvider.java
  persistence/[repositories].java
  engine/GameEngine.java
  engine/ScoreService.java
  ui/ConsoleApp.java
  Main.java
resources/
  game.properties (total.rodadas=8 e max.decisoes.por.rodada=3)
  schema.sql
Enunciado_Projeto.md
README.md
```

---

## 6. Entregáveis
- **Código-fonte completo** no repositório Git.
- **Link do repositório Git** fornecido ao professor.
- `schema.sql` completo com tabelas do H2.
- `README.md` com instruções de build, execução e configuração.
- `RELATORIO.md` indicando quais itens obrigatórios e opcionais foram implementados.
- **Prints de tela/execução** demonstrando funcionamento.
- (Opcional) Relatório `.csv` ou `.txt` com métricas e ranking final.

⚠️ **Versionamento no Git**:  
A **frequência, autoria e qualidade** dos commits (mensagens claras, commits incrementais) **serão avaliadas** como parte da nota.  
Commits únicos ou ausência de histórico podem impactar negativamente a avaliação.

---

## 7. Rubrica de Avaliação

| Critério                                                            |Peso |
|---------------------------------------------------------------------|:---:|
| Arquitetura & POO (SRP/OCP/encapsulamento)                          | 30% |
| Padrões (Strategy obrigatório + opcionais)                          | 25% |
| Persistência & Configuração (H2 + game.properties)                  | 20% |
| Testes & Qualidade                                                  | 15% |
| UX Console & Relatórios                                             | 10% |
| **Controle de versão (Git)** — frequência e autoria dos commits     | Incluído na avaliação geral (impacta nota final) |
