/**
 * Disjuncao.java   ( p ∨ q  /  p | q )  -- "OU" (inclusivo)
 * Verdadeira quando PELO MENOS UM dos dois lados e verdadeiro.
 */
public class Disjuncao extends NoBinario {
    public Disjuncao(NoFormula esquerda, NoFormula direita) {
        super(esquerda, direita);
    }
    public boolean avaliar(TabelaValores valores) {
        return esquerda.avaliar(valores) || direita.avaliar(valores);
    }
}
