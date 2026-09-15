/**
 * Bicondicional.java   ( p ↔ q  /  p <-> q )  -- "SE E SOMENTE SE"
 * Verdadeira quando os dois lados tem o MESMO valor logico
 * (os dois verdadeiros ou os dois falsos).
 */
public class Bicondicional extends NoBinario {
    public Bicondicional(NoFormula esquerda, NoFormula direita) {
        super(esquerda, direita);
    }
    public boolean avaliar(TabelaValores valores) {
        return esquerda.avaliar(valores) == direita.avaliar(valores);
    }
}
