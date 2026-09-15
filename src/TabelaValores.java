import java.util.ArrayList;

/**
 * TabelaValores.java
 * ---------------------------------------------------------------------
 * Guarda o valor (Verdadeiro/Falso) de CADA proposicao atomica para UMA
 * UNICA linha da tabela verdade (uma "interpretacao"). Por exemplo, para
 * a formula "p -> q" com 2 proposicoes, existem 4 objetos TabelaValores
 * diferentes sendo usados (um para cada uma das 4 linhas):
 *   {p=V, q=V} , {p=V, q=F} , {p=F, q=V} , {p=F, q=F}
 *
 * Encapsulamento: por fora so se acessa via definir()/obter(); a forma
 * como os dados sao guardados por dentro (duas listas paralelas) fica
 * escondida.
 * ---------------------------------------------------------------------
 */
public class TabelaValores {
    private ArrayList<String> nomes;
    private ArrayList<Boolean> valores;

    public TabelaValores() {
        nomes = new ArrayList<String>();
        valores = new ArrayList<Boolean>();
    }

    public void definir(String nome, boolean valor) {
        int indice = nomes.indexOf(nome);
        if (indice >= 0) {
            valores.set(indice, valor);
        } else {
            nomes.add(nome);
            valores.add(valor);
        }
    }

    public boolean obter(String nome) {
        int indice = nomes.indexOf(nome);
        return valores.get(indice);
    }
}
