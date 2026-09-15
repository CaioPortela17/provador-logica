import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * InterfaceGrafica.java  (NOVA CLASSE - interface grafica em Swing)
 * ---------------------------------------------------------------------
 * Janela grafica do provador de formulas proposicionais. E o que a
 * equipe deve MOSTRAR na apresentacao depois dos slides: o professor
 * digita uma formula, clica em "Analisar" e a tela mostra, na hora, o
 * resultado das 3 etapas (lexica, sintatica e tabela verdade).
 *
 * Esta classe NAO reimplementa nenhuma regra de logica -- ela so chama
 * MotorProvador.analisar(formula) e desenha o ResultadoAnalise devolvido
 * usando componentes Swing (JTextField, JLabel, JTable...). Ou seja: a
 * "camada de calculo" e a "camada de tela" sao independentes, o que e
 * uma boa pratica de POO (separacao de responsabilidades) e facilita
 * responder "onde fica o calculo?" (MotorProvador) x "onde fica a
 * tela?" (aqui) na hora das perguntas.
 *
 * COMPONENTES DA TELA:
 *   - campoFormula   : onde a formula e digitada
 *   - botaoAnalisar  : dispara a analise
 *   - comboExemplos  : lista pronta de formulas para carregar rapido
 *   - labelEtapaI / labelEtapaII : status (OK / ERRO) de cada etapa
 *   - tabelaVerdade  : JTable com a tabela verdade calculada
 *   - labelClassificacao : TAUTOLOGIA / CONTINGENCIA / CONTRADICAO,
 *                          com a cor de fundo mudando conforme o caso
 * ---------------------------------------------------------------------
 */
public class InterfaceGrafica extends JFrame {

    private JTextField campoFormula;
    private JButton botaoAnalisar;
    private JComboBox<String> comboExemplos;

    private JLabel labelEtapaI;
    private JLabel labelEtapaII;
    private JLabel labelProposicoes;

    private DefaultTableModel modeloTabela;
    private JTable tabelaVerdade;

    private JLabel labelClassificacao;
    private JTextArea areaLegenda;

    // formulas prontas para o professor pedir "mostra um exemplo de X"
    // na hora da apresentacao, sem precisar digitar tudo de novo
    private static final String[] EXEMPLOS = {
        "-- escolha um exemplo --",
        "~p",
        "p & q",
        "p | q",
        "p -> q",
        "p <-> q",
        "p ^ q          (XOR)",
        "(p -> q) & (q -> p)",
        "p & ~p          (contradicao)",
        "p | ~p          (tautologia)",
        "(p -> q) & (p & ~q)",
        "A)) ∧∧ -> BC   (nao e FBF - exemplo de erro)"
    };

    public InterfaceGrafica() {
        super("Provador de Formulas Proposicionais - Projeto Watson (IBM)");
        montarTela();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 640);
        setLocationRelativeTo(null); // abre centralizada na tela
    }

    /**
     * Monta e organiza todos os componentes visuais da janela.
     * Dividido em 3 blocos com BorderLayout: topo (entrada), centro
     * (tabela verdade + status das etapas) e rodape (classificacao).
     */
    private void montarTela() {
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        add(construirPainelEntrada(), BorderLayout.NORTH);
        add(construirPainelCentral(), BorderLayout.CENTER);
        add(construirPainelClassificacao(), BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Painel de cima: campo de texto + botao Analisar + combo de exemplos
    // ---------------------------------------------------------------
    private JPanel construirPainelEntrada() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Formula proposicional"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        painel.add(new JLabel("Digite a formula:"), c);

        campoFormula = new JTextField();
        campoFormula.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        painel.add(campoFormula, c);

        botaoAnalisar = new JButton("Analisar");
        c.gridx = 2; c.gridy = 0; c.weightx = 0;
        painel.add(botaoAnalisar, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        painel.add(new JLabel("Exemplos prontos:"), c);

        comboExemplos = new JComboBox<String>(EXEMPLOS);
        c.gridx = 1; c.gridy = 1; c.weightx = 1; c.gridwidth = 2;
        painel.add(comboExemplos, c);
        c.gridwidth = 1;

        // acoes: Enter no campo de texto OU clique no botao == analisar
        botaoAnalisar.addActionListener(this::aoClicarAnalisar);
        campoFormula.addActionListener(this::aoClicarAnalisar);

        // ao escolher um exemplo no combo, joga o texto pro campo e ja analisa
        comboExemplos.addActionListener((ActionEvent e) -> {
            int indice = comboExemplos.getSelectedIndex();
            if (indice > 0) { // indice 0 = "-- escolha um exemplo --"
                String exemplo = EXEMPLOS[indice];
                // remove comentarios do tipo "  (XOR)" que sao so para o combo
                int posParenteses = exemplo.indexOf("  (");
                if (posParenteses > 0) {
                    exemplo = exemplo.substring(0, posParenteses);
                }
                campoFormula.setText(exemplo.trim());
                analisarFormulaAtual();
            }
        });

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel do meio: status das etapas I/II a esquerda, tabela verdade
    // e legenda de simbolos a direita
    // ---------------------------------------------------------------
    private JPanel construirPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        JPanel painelStatus = new JPanel(new GridLayout(3, 1, 4, 4));
        painelStatus.setBorder(BorderFactory.createTitledBorder("Status das etapas"));
        labelEtapaI = new JLabel("Etapa I (lexico): aguardando formula...");
        labelEtapaII = new JLabel("Etapa II (sintatico): aguardando formula...");
        labelProposicoes = new JLabel("Proposicoes atomicas: -");
        painelStatus.add(labelEtapaI);
        painelStatus.add(labelEtapaII);
        painelStatus.add(labelProposicoes);
        painel.add(painelStatus, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel();
        tabelaVerdade = new JTable(modeloTabela);
        tabelaVerdade.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        tabelaVerdade.getTableHeader().setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        tabelaVerdade.setEnabled(false); // e so exibicao, nao deve ser editavel
        JScrollPane scrollTabela = new JScrollPane(tabelaVerdade);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Etapa III - Tabela verdade"));
        painel.add(scrollTabela, BorderLayout.CENTER);

        areaLegenda = new JTextArea(
            "Simbolos aceitos:\n" +
            "  Negacao (NOT)      : ~  ou  ¬\n" +
            "  Conjuncao (AND)    : &  ou  ∧\n" +
            "  Disjuncao (OR)     : |  ou  ∨\n" +
            "  Disjuncao Excl.(XOR):^  ou  ⊻\n" +
            "  Condicional        : -> ou  →\n" +
            "  Bicondicional      : <-> ou ↔\n" +
            "  Proposicoes: qualquer letra (A-Z, a-z)\n" +
            "  Ate 5 proposicoes atomicas (mais que isso: aviso, mas calcula)"
        );
        areaLegenda.setEditable(false);
        areaLegenda.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaLegenda.setBackground(painel.getBackground());
        areaLegenda.setBorder(BorderFactory.createTitledBorder("Legenda"));
        painel.add(areaLegenda, BorderLayout.SOUTH);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel de baixo: classificacao final, bem grande e colorida
    // ---------------------------------------------------------------
    private JPanel construirPainelClassificacao() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Classificacao"));
        labelClassificacao = new JLabel("Digite uma formula e clique em Analisar", SwingConstants.CENTER);
        labelClassificacao.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        labelClassificacao.setOpaque(true);
        labelClassificacao.setBackground(Color.LIGHT_GRAY);
        labelClassificacao.setPreferredSize(new Dimension(100, 60));
        painel.add(labelClassificacao, BorderLayout.CENTER);
        return painel;
    }

    // chamado pelo botao "Analisar", pelo Enter no campo de texto, ou
    // indiretamente pelo combo de exemplos
    private void aoClicarAnalisar(ActionEvent e) {
        analisarFormulaAtual();
    }

    private void analisarFormulaAtual() {
        String formula = campoFormula.getText();
        if (formula == null || formula.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite uma formula antes de analisar.",
                "Campo vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // toda a analise (etapas I, II e III) acontece aqui, dentro do
        // motor que fica separado da tela:
        ResultadoAnalise resultado = MotorProvador.analisar(formula);

        atualizarEtapaI(resultado);
        atualizarEtapaII(resultado);
        atualizarTabela(resultado);
        atualizarClassificacao(resultado);
    }

    private void atualizarEtapaI(ResultadoAnalise resultado) {
        if (resultado.isLexicoOk()) {
            labelEtapaI.setText("<html><b>Etapa I (lexico):</b> <font color='green'>OK - todos os simbolos sao validos</font></html>");
        } else {
            StringBuilder simbolos = new StringBuilder();
            List<Character> invalidos = resultado.getSimbolosInvalidos();
            for (int i = 0; i < invalidos.size(); i++) {
                if (i > 0) simbolos.append(", ");
                simbolos.append("'").append(invalidos.get(i)).append("'");
            }
            labelEtapaI.setText("<html><b>Etapa I (lexico):</b> <font color='red'>ERRO - simbolo(s) invalido(s): " +
                simbolos + "</font></html>");
        }
    }

    private void atualizarEtapaII(ResultadoAnalise resultado) {
        if (!resultado.isLexicoOk()) {
            labelEtapaII.setText("Etapa II (sintatico): nao avaliada (erro na Etapa I)");
            return;
        }
        if (resultado.isSintaticoOk()) {
            labelEtapaII.setText("<html><b>Etapa II (sintatico):</b> <font color='green'>OK - formula bem formada (FBF)</font></html>");
        } else {
            labelEtapaII.setText("<html><b>Etapa II (sintatico):</b> <font color='red'>ERRO - " +
                resultado.getMensagemErroSintatico() + "</font></html>");
        }
    }

    /**
     * Preenche a JTable com a tabela verdade. Se a formula deu erro nas
     * etapas anteriores, a tabela e simplesmente limpa.
     */
    private void atualizarTabela(ResultadoAnalise resultado) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);

        if (!resultado.isLexicoOk() || !resultado.isSintaticoOk()) {
            labelProposicoes.setText("Proposicoes atomicas: -");
            return;
        }

        List<String> proposicoes = resultado.getProposicoes();
        labelProposicoes.setText("Proposicoes atomicas: " + String.join(", ", proposicoes) +
            "  (" + resultado.getTotalLinhas() + " linhas)");

        // monta as colunas: uma por proposicao + "Resultado"
        for (String p : proposicoes) {
            modeloTabela.addColumn(p);
        }
        modeloTabela.addColumn("Resultado");

        List<boolean[]> linhas = resultado.getLinhasEntrada();
        List<Boolean> resultados = resultado.getResultados();
        for (int i = 0; i < linhas.size(); i++) {
            boolean[] valores = linhas.get(i);
            Object[] linhaTabela = new Object[proposicoes.size() + 1];
            for (int j = 0; j < valores.length; j++) {
                linhaTabela[j] = valores[j] ? "V" : "F";
            }
            linhaTabela[valores.length] = resultados.get(i) ? "V" : "F";
            modeloTabela.addRow(linhaTabela);
        }
    }

    private void atualizarClassificacao(ResultadoAnalise resultado) {
        if (!resultado.isLexicoOk()) {
            labelClassificacao.setText("Nao foi possivel montar a tabela (erro lexico)");
            labelClassificacao.setBackground(new Color(255, 200, 200));
            return;
        }
        if (!resultado.isSintaticoOk()) {
            labelClassificacao.setText("Formula nao e uma FBF - tabela nao construida");
            labelClassificacao.setBackground(new Color(255, 200, 200));
            return;
        }

        String classificacao = resultado.getClassificacao();
        labelClassificacao.setText(classificacao);

        // cor muda conforme o resultado, ajuda a "ler" a tela de longe
        // durante a apresentacao
        if ("TAUTOLOGIA".equals(classificacao)) {
            labelClassificacao.setBackground(new Color(200, 255, 200)); // verde claro
        } else if ("CONTRADICAO".equals(classificacao)) {
            labelClassificacao.setBackground(new Color(255, 200, 200)); // vermelho claro
        } else {
            labelClassificacao.setBackground(new Color(255, 245, 180)); // amarelo claro
        }
    }

    /**
     * Ponto de entrada do programa: rode esta classe para abrir a janela.
     */
    public static void main(String[] args) {
        // SwingUtilities.invokeLater garante que a interface e criada na
        // "thread de eventos" do Swing, como a documentacao recomenda
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // se o look-and-feel do sistema falhar, segue com o padrao do Java
            }
            InterfaceGrafica janela = new InterfaceGrafica();
            janela.setVisible(true);
        });
    }
}
