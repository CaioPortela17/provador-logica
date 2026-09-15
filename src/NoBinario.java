import java.util.ArrayList;

/**
 * NoBinario.java
 * ---------------------------------------------------------------------
 * Classe abstrata-BASE para todo conectivo BINARIO (que liga dois
 * operandos: esquerda e direita) -- ou seja, para Conjuncao, Disjuncao,
 * Xor, Condicional e Bicondicional.
 *
 * HERANCA: obterProposicoes() e IGUAL para todos os conectivos binarios
 * (sempre precisa perguntar pros dois lados), entao fica pronta aqui uma
 * unica vez, e as 6 subclasses herdam de graca. Ja avaliar() continua
 * abstrato, porque a REGRA logica (V/F) e diferente para cada um --
 * isso e o mesmo POLIMORFISMO explicado em NoFormula.java, aplicado
 * apenas a regra que realmente muda entre os conectivos.
 * ---------------------------------------------------------------------
 */
public abstract class NoBinario extends NoFormula {
    protected NoFormula esquerda;
    protected NoFormula direita;

    public NoBinario(NoFormula esquerda, NoFormula direita) {
        this.esquerda = esquerda;
        this.direita = direita;
    }

    public void obterProposicoes(ArrayList<String> destino) {
        esquerda.obterProposicoes(destino);
        direita.obterProposicoes(destino);
    }
}
