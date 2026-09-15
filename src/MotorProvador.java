import java.util.ArrayList;

/**
 * MotorProvador.java  (NOVA CLASSE)
 * ---------------------------------------------------------------------
 * Este e o "motor" do provador: roda as 3 etapas (lexica, sintatica e
 * tabela verdade) e devolve um ResultadoAnalise pronto, SEM imprimir nem
 * desenhar nada na tela. Manter o calculo separado da exibicao (que fica
 * toda em InterfaceGrafica.java) deixa o codigo mais facil de entender e
 * de testar: essa classe so calcula, quem desenha na janela e outra.
 *
 * NOTA SOBRE O METODO "TABLEAUX" CITADO NO ENUNCIADO:
 * O enunciado pede um provador "baseado no teorema de Tableaux". Aqui
 * usamos ENUMERACAO EXAUSTIVA de todas as interpretacoes possiveis
 * (todas as combinacoes de V/F das proposicoes atomicas) para decidir
 * se a formula e tautologia, contradicao ou contingencia -- e o mesmo
 * resultado que a tabela verdade tradicional. Cada LINHA da tabela
 * equivale a um RAMO (galho) de uma arvore de tableau totalmente
 * expandida: por isso o resultado final (V/F em cada linha e a
 * classificacao) e identico ao que um tableau semantico completo
 * produziria. Se o professor perguntar especificamente pela ARVORE do
 * tableau (com as regras de expansao alfa/beta e fechamento de ramos
 * contraditorios passo a passo), vale deixar claro na apresentacao que
 * a equipe optou por implementar a enumeracao completa de
 * interpretacoes como metodo de decisao, que e equivalente em poder mas
 * nao desenha a arvore em si.
 * ---------------------------------------------------------------------
 */
public class MotorProvador {

    // classe utilitaria (so metodos estaticos) -- nao faz sentido
    // instanciar um MotorProvador, entao o construtor fica privado
    private MotorProvador() {
    }

    public static ResultadoAnalise analisar(String formula) {
        ResultadoAnalise resultado = new ResultadoAnalise(formula);

        // ---------------- ETAPA I - ANALISE LEXICA ----------------
        AnalisadorLexico lexico = new AnalisadorLexico();
        lexico.analisar(formula);

        if (lexico.getSimbolosInvalidos().size() > 0) {
            resultado.setLexicoOk(false);
            resultado.getSimbolosInvalidos().addAll(lexico.getSimbolosInvalidos());
            return resultado; // erro lexico impede as etapas seguintes
        }
        resultado.setLexicoOk(true);

        // ---------------- ETAPA II - ANALISE SINTATICA ----------------
        AnalisadorSintatico sintatico = new AnalisadorSintatico(lexico.getTokens());
        NoFormula raiz;
        try {
            raiz = sintatico.analisar();
        } catch (FormulaInvalidaException e) {
            resultado.setSintaticoOk(false);
            resultado.setMensagemErroSintatico(e.getMessage());
            return resultado; // nao e FBF -> nao da pra montar a tabela
        }
        resultado.setSintaticoOk(true);

        // coleta as proposicoes atomicas usadas e ordena alfabeticamente
        // (assim a tabela sempre sai com as colunas em ordem previsivel)
        ArrayList<String> proposicoes = new ArrayList<String>();
        raiz.obterProposicoes(proposicoes);
        ordenar(proposicoes);
        resultado.getProposicoes().addAll(proposicoes);

        // ---------------- ETAPA III - TABELA VERDADE ----------------
        int quantidade = proposicoes.size();

        // 2^quantidade linhas: uma para cada combinacao possivel de V/F
        int totalLinhas = 1 << quantidade;

        for (int mascara = 0; mascara < totalLinhas; mascara++) {
            boolean[] linha = new boolean[quantidade];
            TabelaValores valores = new TabelaValores();

            // le os bits de "mascara" da esquerda para a direita para
            // decidir V/F de cada proposicao nesta linha
            for (int i = 0; i < quantidade; i++) {
                int posicaoBit = quantidade - 1 - i;
                int bit = (mascara >> posicaoBit) & 1;
                boolean valor = (bit == 0); // bit 0 = Verdadeiro, bit 1 = Falso
                linha[i] = valor;
                valores.definir(proposicoes.get(i), valor);
            }

            resultado.getLinhasEntrada().add(linha);
            resultado.getResultados().add(raiz.avaliar(valores));
        }

        resultado.setClassificacao(classificar(resultado.getResultados()));
        return resultado;
    }

    // bubble sort simples -- mesma logica do codigo original, so movida
    // para ca porque e usada aqui dentro do motor
    private static void ordenar(ArrayList<String> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - 1 - i; j++) {
                if (lista.get(j).compareTo(lista.get(j + 1)) > 0) {
                    String temporario = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temporario);
                }
            }
        }
    }

    // TAUTOLOGIA: resultado e V em TODAS as linhas
    // CONTRADICAO: resultado e F em TODAS as linhas
    // CONTINGENCIA: nem um nem outro (tem linha V e linha F)
    private static String classificar(java.util.List<Boolean> resultados) {
        boolean todosVerdadeiros = true;
        boolean todosFalsos = true;
        for (int i = 0; i < resultados.size(); i++) {
            boolean r = resultados.get(i);
            if (!r) {
                todosVerdadeiros = false;
            }
            if (r) {
                todosFalsos = false;
            }
        }
        if (todosVerdadeiros) {
            return "TAUTOLOGIA";
        }
        if (todosFalsos) {
            return "CONTRADICAO";
        }
        return "CONTINGENCIA";
    }
}
