/**
 * Xor.java   ( p ⊻ q  /  p ^ q )  -- "OU EXCLUSIVO" (Disjuncao Exclusiva)
 * ---------------------------------------------------------------------
 * Este e o conectivo "Disjuncao exclusiva" citado no enunciado da
 * atividade (a lista de simbolos do professor pede Negacao, Conjuncao,
 * Disjuncao, Condicional, Bicondicional e Disjuncao Exclusiva). O
 * codigo original do colega ainda nao tinha essa classe -- foi
 * adicionada aqui pra cobrir exatamente o que o enunciado pede.
 *
 * E verdadeira quando os dois lados tem valores DIFERENTES entre si
 * (ou um ou outro e verdadeiro, mas nao os dois e nem nenhum).
 * ---------------------------------------------------------------------
 */
public class Xor extends NoBinario {
    public Xor(NoFormula esquerda, NoFormula direita) {
        super(esquerda, direita);
    }
    public boolean avaliar(TabelaValores valores) {
        return esquerda.avaliar(valores) != direita.avaliar(valores);
    }
}
