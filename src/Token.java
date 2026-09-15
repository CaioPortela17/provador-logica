/**
 * Token.java
 * ---------------------------------------------------------------------
 * Representa uma "palavra" (unidade lexica) reconhecida dentro da formula
 * de entrada. Por exemplo, ao ler "p -> q", o Analisador Lexico (Etapa I)
 * gera 3 tokens: PROP("p"), COND("->"), PROP("q").
 *
 * Encapsulamento (POO): os campos "tipo" e "lexema" sao privados e so
 * podem ser lidos de fora da classe atraves dos metodos getTipo() e
 * getLexema() (getters).
 * ---------------------------------------------------------------------
 */
public class Token {

    // tipo do token: identifica a CATEGORIA gramatical do simbolo lido.
    // valores possiveis: "NOT", "AND", "OR", "XOR", "COND",
    //                    "BICOND", "LPAREN", "RPAREN", "PROP"
    private String tipo;

    // lexema: o texto EXATO que apareceu na formula digitada pelo usuario.
    // Guardamos o lexema (e nao so o tipo) para poder montar mensagens de
    // erro mais claras, por exemplo dizendo exatamente qual caractere foi
    // digitado no lugar errado.
    private String lexema;

    public Token(String tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }
}
