/**
 * Conjuncao.java   ( p ∧ q  /  p & q )  -- "E"
 * Verdadeira somente quando os DOIS lados sao verdadeiros.
 */
public class Conjuncao extends NoBinario {
    public Conjuncao(NoFormula esquerda, NoFormula direita) {
        super(esquerda, direita);
    }
    public boolean avaliar(TabelaValores valores) {
        return esquerda.avaliar(valores) && direita.avaliar(valores);
    }
}
