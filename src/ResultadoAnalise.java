import java.util.ArrayList;
import java.util.List;

/**
 * ResultadoAnalise.java  (NOVA CLASSE)
 * ---------------------------------------------------------------------
 * "Pacote" com tudo o que a analise de UMA formula produziu: se passou
 * na Etapa I, se passou na Etapa II, a tabela verdade da Etapa III e a
 * classificacao final. E so um objeto para GUARDAR dados (nao tem
 * logica de calculo) -- o MotorProvador preenche esse objeto e devolve
 * pronto para a InterfaceGrafica exibir na janela (tabela, labels de
 * status, cor da classificacao etc.), sem precisar recalcular nada.
 * ---------------------------------------------------------------------
 */
public class ResultadoAnalise {

    private String formulaOriginal;

    // ETAPA I - lexico
    private boolean lexicoOk;
    private List<Character> simbolosInvalidos;

    // ETAPA II - sintatico
    private boolean sintaticoOk;
    private String mensagemErroSintatico;

    // ETAPA III - provador / tabela verdade
    private List<String> proposicoes;      // nomes das proposicoes, em ordem alfabetica
    private List<boolean[]> linhasEntrada; // cada item = uma linha (V/F de cada proposicao)
    private List<Boolean> resultados;      // resultado da formula em cada linha
    private String classificacao;          // "TAUTOLOGIA" | "CONTINGENCIA" | "CONTRADICAO"

    public ResultadoAnalise(String formulaOriginal) {
        this.formulaOriginal = formulaOriginal;
        this.lexicoOk = false;
        this.simbolosInvalidos = new ArrayList<Character>();
        this.sintaticoOk = false;
        this.mensagemErroSintatico = null;
        this.proposicoes = new ArrayList<String>();
        this.linhasEntrada = new ArrayList<boolean[]>();
        this.resultados = new ArrayList<Boolean>();
        this.classificacao = null;
    }

    public String getFormulaOriginal() {
        return formulaOriginal;
    }

    public boolean isLexicoOk() {
        return lexicoOk;
    }

    public void setLexicoOk(boolean lexicoOk) {
        this.lexicoOk = lexicoOk;
    }

    public List<Character> getSimbolosInvalidos() {
        return simbolosInvalidos;
    }

    public boolean isSintaticoOk() {
        return sintaticoOk;
    }

    public void setSintaticoOk(boolean sintaticoOk) {
        this.sintaticoOk = sintaticoOk;
    }

    public String getMensagemErroSintatico() {
        return mensagemErroSintatico;
    }

    public void setMensagemErroSintatico(String mensagemErroSintatico) {
        this.mensagemErroSintatico = mensagemErroSintatico;
    }

    public List<String> getProposicoes() {
        return proposicoes;
    }

    public List<boolean[]> getLinhasEntrada() {
        return linhasEntrada;
    }

    public List<Boolean> getResultados() {
        return resultados;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    // conveniencia: quantas proposicoes atomicas essa formula tem
    public int getQuantidadeProposicoes() {
        return proposicoes.size();
    }

    // conveniencia: quantas linhas a tabela verdade tem (2 ^ quantidade)
    public int getTotalLinhas() {
        return linhasEntrada.size();
    }
}
