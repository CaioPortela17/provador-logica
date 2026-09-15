import java.util.ArrayList;

/**
 * NoFormula.java
 * ---------------------------------------------------------------------
 * Classe ABSTRATA que representa um no generico da arvore sintatica de
 * uma formula proposicional (a arvore e montada pelo AnalisadorSintatico
 * na Etapa II). Cada tipo de conectivo (E, OU, NAO, SE-ENTAO, etc.) tem
 * a sua propria subclasse que HERDA de NoFormula.
 *
 * POLIMORFISMO: em vez de um unico metodo gigante cheio de "if/else" ou
 * "switch" perguntando "qual e o conectivo?", cada subclasse sabe se
 * avaliar sozinha. Quando chamamos raiz.avaliar(valores), o Java decide
 * sozinho, em tempo de execucao, qual avaliar() rodar, de acordo com o
 * tipo real do objeto (Conjuncao, Disjuncao, Negacao, ...).
 * ---------------------------------------------------------------------
 */
public abstract class NoFormula {

    // calcula o valor logico (V/F) deste no, dado um "cenario" de valores
    // para cada proposicao atomica (uma linha da tabela verdade)
    public abstract boolean avaliar(TabelaValores valores);

    // percorre a arvore recolhendo os NOMES de todas as proposicoes
    // atomicas usadas na formula (sem repetir), para sabermos quantas
    // colunas a tabela verdade vai ter
    public abstract void obterProposicoes(ArrayList<String> destino);
}
