import java.util.ArrayList;

/**
 * Negacao.java  ( ~p  /  ¬p )
 * ---------------------------------------------------------------------
 * Unico conectivo UNARIO do projeto (tem um so operando). Inverte o
 * valor logico do que estiver "dentro" dela.
 * ---------------------------------------------------------------------
 */
public class Negacao extends NoFormula {
    private NoFormula operando;

    public Negacao(NoFormula operando) {
        this.operando = operando;
    }

    public boolean avaliar(TabelaValores valores) {
        return !operando.avaliar(valores);
    }

    public void obterProposicoes(ArrayList<String> destino) {
        operando.obterProposicoes(destino);
    }
}
