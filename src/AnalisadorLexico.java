import java.util.ArrayList;

/**
 * AnalisadorLexico.java  (ETAPA I do trabalho)
 * ---------------------------------------------------------------------
 * Responsavel por percorrer a String da formula caractere-a-caractere e
 * transformar cada pedaco reconhecido em um Token (ver classe Token).
 *
 * Se encontrar um caractere que NAO pertence ao alfabeto da logica
 * proposicional (ex.: "@", "3", "%"), ele guarda esse caractere na lista
 * de simbolosInvalidos em vez de travar o programa. Assim o restante do
 * sistema (Etapa II e III) consegue decidir o que fazer com esse erro
 * (no nosso caso: avisar o usuario e nao montar a tabela verdade).
 *
 * SIMBOLOS ACEITOS (cada conectivo tem uma versao "bonita" em Unicode e
 * uma versao "atalho" em ASCII, para funcionar mesmo em teclado sem
 * esses caracteres especiais):
 *
 *   Negacao               ( NOT )   :  ~   ou  ¬
 *   Conjuncao "E"         ( AND )   :  ∧   ou  &
 *   Disjuncao "OU"        ( OR  )   :  ∨   ou  |
 *   Disjuncao Exclusiva   ( XOR )   :  ⊻   ou  ^
 *   Condicional           ( COND )  :  →   ou  ->
 *   Bicondicional         ( BICOND):  ↔   ou  <->
 *   Proposicoes atomicas            :  qualquer letra (A-Z, a-z)
 *   Parenteses                      :  ( )
 * ---------------------------------------------------------------------
 */
public class AnalisadorLexico {

    private ArrayList<Token> tokens;
    private ArrayList<Character> simbolosInvalidos;

    public AnalisadorLexico() {
        tokens = new ArrayList<Token>();
        simbolosInvalidos = new ArrayList<Character>();
    }

    /**
     * Metodo principal da Etapa I. Le a formula inteira e preenche as
     * listas "tokens" (o que foi reconhecido) e "simbolosInvalidos"
     * (o que NAO pertence ao alfabeto da logica proposicional).
     */
    public void analisar(String formula) {
        // zera o resultado de uma analise anterior, caso o mesmo objeto
        // seja reaproveitado para analisar outra formula
        tokens = new ArrayList<Token>();
        simbolosInvalidos = new ArrayList<Character>();

        int i = 0;
        int n = formula.length();

        while (i < n) {
            char c = formula.charAt(i);

            // 1) espacos em branco sao apenas separadores visuais, nao
            //    geram token nenhum
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // 2) VARIATION SELECTOR (U+FE0F): alguns teclados de celular
            //    (principalmente iOS/Android) inserem esse caractere
            //    "invisivel" logo depois de simbolos como "↔" para pedir
            //    que ele seja desenhado em estilo emoji. Ele nao faz
            //    parte da logica proposicional, entao apenas ignoramos.
            //    (Sem este tratamento, colar "↔️" copiado do celular
            //    quebrava o analisador lexico -- o proprio codigo
            //    original tinha esse bug, corrigido aqui.)
            if (c == '\uFE0F') {
                i++;
                continue;
            }

            // 3) conectivos formados por MAIS DE UM caractere ASCII
            //    precisam ser verificados ANTES dos de um caractere so,
            //    senao "<->" seria lido como "<", "-", ">" separados.
            if (i + 3 <= n && formula.substring(i, i + 3).equals("<->")) {
                tokens.add(new Token("BICOND", "<->"));
                i = i + 3;
                continue;
            }
            if (i + 2 <= n && formula.substring(i, i + 2).equals("->")) {
                tokens.add(new Token("COND", "->"));
                i = i + 2;
                continue;
            }

            // 4) conectivos de UM caractere (versao unicode ou atalho ascii)
            if (c == '~' || c == '¬') {
                tokens.add(new Token("NOT", String.valueOf(c)));
                i++;
                continue;
            }
            if (c == '∧' || c == '&') {
                tokens.add(new Token("AND", String.valueOf(c)));
                i++;
                continue;
            }
            if (c == '∨' || c == '|') {
                tokens.add(new Token("OR", String.valueOf(c)));
                i++;
                continue;
            }
            if (c == '⊻' || c == '^') {
                tokens.add(new Token("XOR", String.valueOf(c)));
                i++;
                continue;
            }
            if (c == '→') {
                tokens.add(new Token("COND", String.valueOf(c)));
                i++;
                continue;
            }
            if (c == '↔') {
                tokens.add(new Token("BICOND", String.valueOf(c)));
                i++;
                continue;
            }
            if (c == '(') {
                tokens.add(new Token("LPAREN", "("));
                i++;
                continue;
            }
            if (c == ')') {
                tokens.add(new Token("RPAREN", ")"));
                i++;
                continue;
            }
            if (Character.isLetter(c)) {
                tokens.add(new Token("PROP", String.valueOf(c)));
                i++;
                continue;
            }

            // 5) nao se encaixou em nada acima -> simbolo fora do
            //    alfabeto da logica proposicional
            simbolosInvalidos.add(c);
            i++;
        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public ArrayList<Character> getSimbolosInvalidos() {
        return simbolosInvalidos;
    }
}
