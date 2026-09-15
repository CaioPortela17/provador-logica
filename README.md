# Provador de Fórmulas Proposicionais

Aplicação Java (Swing) que analisa fórmulas da lógica proposicional em três etapas — léxica, sintática e tabela verdade — e classifica o resultado como **tautologia**, **contradição** ou **contingência**.

## Funcionalidades

- **Etapa I — Análise léxica**: percorre a fórmula caractere a caractere e converte cada trecho reconhecido em um `Token`, reportando símbolos fora do alfabeto da lógica proposicional.
- **Etapa II — Análise sintática**: parser de descida recursiva que valida se a sequência de tokens forma uma Fórmula Bem Formada (FBF) e monta a árvore sintática (`NoFormula`) usada na etapa seguinte.
- **Etapa III — Tabela verdade**: enumera exaustivamente todas as combinações V/F das proposições atômicas (equivalente a uma árvore de tableau totalmente expandida) e classifica a fórmula.
- **Interface gráfica** (`InterfaceGrafica`): campo de entrada, exemplos prontos, status de cada etapa, tabela verdade renderizada e classificação final destacada por cor.

## Símbolos aceitos

Cada conectivo tem uma versão em Unicode e um atalho em ASCII:

| Conectivo             | Símbolos       |
|------------------------|----------------|
| Negação (NOT)          | `~` ou `¬`     |
| Conjunção (AND)        | `&` ou `∧`     |
| Disjunção (OR)         | `\|` ou `∨`    |
| Disjunção exclusiva (XOR) | `^` ou `⊻`  |
| Condicional             | `->` ou `→`   |
| Bicondicional           | `<->` ou `↔`  |
| Proposições atômicas    | qualquer letra (A-Z, a-z) |
| Agrupamento             | `(` `)`        |

## Gramática (menor → maior precedência)

```
bicondicional := condicional ( '<->' condicional )?      (assoc. direita)
condicional   := disjuncao   ( '->'  condicional )?      (assoc. direita)
disjuncao     := conjuncao   ( ('|'|'^') conjuncao )*     (assoc. esquerda)
conjuncao     := negacao     ( '&' negacao )*             (assoc. esquerda)
negacao       := '~' negacao | atomo
atomo         := PROP | '(' bicondicional ')'
```

OR e XOR compartilham o mesmo nível de precedência.

## Estrutura do projeto

```
src/
├── InterfaceGrafica.java       # janela Swing (camada de exibição)
├── MotorProvador.java          # orquestra as 3 etapas e devolve o resultado
├── AnalisadorLexico.java       # Etapa I
├── AnalisadorSintatico.java    # Etapa II (parser recursivo descendente)
├── Token.java                  # tipo + lexema de cada token
├── NoFormula.java               # nó abstrato da árvore sintática
├── NoBinario.java               # base para conectivos binários
├── Proposicao.java              # nó folha (variável proposicional)
├── Negacao.java
├── Conjuncao.java
├── Disjuncao.java
├── Xor.java
├── Condicional.java
├── Bicondicional.java
├── TabelaValores.java           # atribuição V/F usada na avaliação
├── ResultadoAnalise.java        # DTO com o resultado das 3 etapas
└── FormulaInvalidaException.java
```

## Como executar

Requer JDK instalado (o projeto foi configurado no IntelliJ IDEA).

```bash
cd src
javac *.java
java InterfaceGrafica
```

Ou abra a pasta do projeto no IntelliJ IDEA e rode a classe `InterfaceGrafica`.

## Exemplo de uso

1. Digite uma fórmula, por exemplo `(p -> q) & (q -> p)`, ou escolha um exemplo pronto no combo.
2. Clique em **Analisar** (ou pressione Enter).
3. A tela mostra o status das etapas I e II, a tabela verdade completa e a classificação final (tautologia/contradição/contingência).
