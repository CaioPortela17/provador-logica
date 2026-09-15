import java.util.ArrayList;

/**
 * Proposicao.java
 * ---------------------------------------------------------------------
 * "No-folha" da arvore sintatica: representa uma proposicao atomica
 * isolada, por exemplo "p" ou "q". E o caso mais simples de NoFormula:
 * nao tem filhos, so um nome.
 * ---------------------------------------------------------------------
 */
public class Proposicao extends NoFormula {
    private String nome;

    public Proposicao(String nome) {
        this.nome = nome;
    }

    // o valor da proposicao e simplesmente o que estiver definido para
    // ela naquela linha da tabela verdade (ex.: p = Verdadeiro)
    public boolean avaliar(TabelaValores valores) {
        return valores.obter(nome);
    }

    public void obterProposicoes(ArrayList<String> destino) {
        if (!destino.contains(nome)) {
            destino.add(nome);
        }
    }
}
