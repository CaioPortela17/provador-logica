/**
 * FormulaInvalidaException.java
 * ---------------------------------------------------------------------
 * Excecao propria do projeto (heranca: estende a classe Exception do
 * Java). E lancada pelo AnalisadorSintatico (Etapa II) sempre que a
 * sequencia de tokens NAO forma uma Formula Bem Formada (FBF) -- por
 * exemplo parenteses que nao fecham, dois conectivos seguidos, ou uma
 * formula que termina no meio.
 *
 * Usar uma excecao propria (em vez de RuntimeException generica) deixa
 * claro, so pelo tipo, que o erro e de natureza SINTATICA, e permite
 * capturar exatamente esse tipo de erro no bloco try/catch da Etapa II.
 * ---------------------------------------------------------------------
 */
public class FormulaInvalidaException extends Exception {
    public FormulaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
