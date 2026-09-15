/**
 * Condicional.java   ( p → q  /  p -> q )  -- "SE-ENTAO"
 * So e FALSA em um unico caso: quando o antecedente (esquerda) e
 * verdadeiro e o consequente (direita) e falso. Em todos os outros
 * casos e verdadeira (inclusive quando o antecedente e falso).
 */
public class Condicional extends NoBinario {
    public Condicional(NoFormula esquerda, NoFormula direita) {
        super(esquerda, direita);
    }
    public boolean avaliar(TabelaValores valores) {
        return (!esquerda.avaliar(valores)) || direita.avaliar(valores);
    }
}
