import java.util.ArrayList;

/**
 * AnalisadorSintatico.java  (ETAPA II do trabalho)
 * ---------------------------------------------------------------------
 * Recebe a lista de Tokens produzida pela Etapa I e verifica se eles
 * formam uma Formula Bem Formada (FBF), montando ao mesmo tempo a
 * arvore de objetos NoFormula que a Etapa III vai usar para calcular a
 * tabela verdade.
 *
 * Tecnica usada: "descida recursiva" (recursive descent parser). Cada
 * nivel de precedencia dos conectivos vira um metodo, do de MENOR
 * precedencia (bicondicional, testado primeiro) ate o de MAIOR
 * precedencia (negacao, testado por ultimo, "mais perto" das letras).
 * Isso resolve sozinho o problema de "qual conectivo calcular primeiro"
 * sem precisar escrever regras de precedencia na mao.
 *
 * GRAMATICA (da menor para a maior precedencia; NOT e o mais "forte"):
 *   bicondicional := condicional ( '<->' condicional )?          (dir.)
 *   condicional   := disjuncao   ( '->'  condicional )?          (dir.)
 *   disjuncao     := conjuncao   ( ('|'|'^') conjuncao )*        (esq.)
 *   conjuncao     := negacao     ( '&' negacao )*                (esq.)
 *   negacao       := '~' negacao | atomo
 *   atomo         := PROP | '(' bicondicional ')'
 *
 * (OR e XOR ficam no mesmo nivel de precedencia, por serem variantes de
 *  disjuncao.)
 * ---------------------------------------------------------------------
 */
public class AnalisadorSintatico {
    private ArrayList<Token> tokens;
    private int posicao; // indice do proximo token ainda nao consumido

    public AnalisadorSintatico(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.posicao = 0;
    }

    // olha o proximo token SEM consumi-lo (null se acabaram os tokens)
    private Token proximoToken() {
        if (posicao < tokens.size()) {
            return tokens.get(posicao);
        }
        return null;
    }

    // consome (avanca) o token atual e o devolve
    private Token avancar() {
        Token atual = proximoToken();
        posicao++;
        return atual;
    }

    /**
     * Ponto de entrada da Etapa II. Se a formula inteira for consumida
     * corretamente e nao sobrar (nem faltar) nenhum token, devolve a
     * raiz da arvore sintatica. Caso contrario, lanca
     * FormulaInvalidaException explicando o motivo.
     */
    public NoFormula analisar() throws FormulaInvalidaException {
        if (tokens.isEmpty()) {
            throw new FormulaInvalidaException("formula vazia.");
        }
        NoFormula no = bicondicional();
        // se sobrou token depois de fechar a formula, ha algo mal
        // posicionado (ex.: parenteses fechando mais vezes do que abriu)
        if (posicao != tokens.size()) {
            String sobra = tokens.get(posicao).getLexema();
            throw new FormulaInvalidaException(
                "simbolos em excesso a partir de '" + sobra +
                "' (parenteses ou conectivos mal posicionados).");
        }
        return no;
    }

    // menor precedencia: bicondicional (associa para a direita)
    private NoFormula bicondicional() throws FormulaInvalidaException {
        NoFormula esquerda = condicional();
        Token token = proximoToken();
        if (token != null && token.getTipo().equals("BICOND")) {
            avancar();
            NoFormula direita = bicondicional(); // recursao = associa p/ direita
            return new Bicondicional(esquerda, direita);
        }
        return esquerda;
    }

    private NoFormula condicional() throws FormulaInvalidaException {
        NoFormula esquerda = disjuncao();
        Token token = proximoToken();
        if (token != null && token.getTipo().equals("COND")) {
            avancar();
            NoFormula direita = condicional(); // recursao = associa p/ direita
            return new Condicional(esquerda, direita);
        }
        return esquerda;
    }

    // OR e XOR moram no mesmo nivel de precedencia (associam p/ esquerda,
    // por isso usamos um "while" em vez de recursao aqui)
    private NoFormula disjuncao() throws FormulaInvalidaException {
        NoFormula esquerda = conjuncao();
        Token token = proximoToken();
        while (token != null &&
               (token.getTipo().equals("OR") || token.getTipo().equals("XOR"))) {
            avancar();
            NoFormula direita = conjuncao();
            if (token.getTipo().equals("OR")) {
                esquerda = new Disjuncao(esquerda, direita);
            } else {
                esquerda = new Xor(esquerda, direita);
            }
            token = proximoToken();
        }
        return esquerda;
    }

    private NoFormula conjuncao() throws FormulaInvalidaException {
        NoFormula esquerda = negacao();
        Token token = proximoToken();
        while (token != null && token.getTipo().equals("AND")) {
            avancar();
            NoFormula direita = negacao();
            esquerda = new Conjuncao(esquerda, direita);
            token = proximoToken();
        }
        return esquerda;
    }

    // maior precedencia antes do atomo: negacao pode se repetir (~~p)
    private NoFormula negacao() throws FormulaInvalidaException {
        Token token = proximoToken();
        if (token != null && token.getTipo().equals("NOT")) {
            avancar();
            NoFormula operando = negacao(); // recursao permite ~~~p
            return new Negacao(operando);
        }
        return atomo();
    }

    // caso base da recursao: uma proposicao isolada OU uma sub-formula
    // inteira entre parenteses
    private NoFormula atomo() throws FormulaInvalidaException {
        Token token = proximoToken();
        if (token == null) {
            throw new FormulaInvalidaException("termino inesperado da formula (esperava um operando).");
        }
        if (token.getTipo().equals("PROP")) {
            avancar();
            return new Proposicao(token.getLexema());
        }
        if (token.getTipo().equals("LPAREN")) {
            avancar();
            NoFormula no = bicondicional(); // volta pro topo da gramatica
            Token fechamento = proximoToken();
            if (fechamento == null || !fechamento.getTipo().equals("RPAREN")) {
                throw new FormulaInvalidaException("parenteses nao fecham corretamente.");
            }
            avancar();
            return no;
        }
        // chegou aqui: token que nao pode iniciar um operando
        // (ex.: dois conectivos seguidos, ou ')' sem '(' correspondente)
        throw new FormulaInvalidaException(
            "token inesperado '" + token.getLexema() +
            "' (dois conectivos seguidos ou operando ausente).");
    }
}
