package view;

import javax.swing.*;
import java.awt.*;
import controller.UsuarioController;
import model.Usuario;

public class SistemaEventosApp extends JFrame {

    private final UsuarioController usuarioController = new UsuarioController();
    private JPanel mainContentPanel;
    private JLabel statusLabel;
    private Usuario usuarioEmCadastro;
    private Usuario usuarioLogado;

    public SistemaEventosApp() {
        setTitle("Sistema de Eventos e Rede Social");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());


        getContentPane().setBackground(new Color(24, 30, 48));

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.setBackground(new Color(24, 30, 48));
        add(mainContentPanel, BorderLayout.CENTER);


        statusLabel = new JLabel("Status: Aguardando Login...");
        statusLabel.setForeground(new Color(150, 150, 150));
        statusLabel.setBackground(new Color(36, 44, 62));
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        mostrarTelaLogin();

        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void mostrarTelaLogin() {
        mainContentPanel.removeAll();

        Component navPanel = ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.WEST);
        if (navPanel != null) {
            remove(navPanel);
        }

        TelaLogin telaLogin = new TelaLogin(this);
        mainContentPanel.add(telaLogin, BorderLayout.CENTER);
        statusLabel.setText("Status: Faça o login ou cadastre-se.");
        revalidarConteudo();
    }



    public void mostrarTelaCadastroInicial() {
        mainContentPanel.removeAll();
        usuarioEmCadastro = new Usuario();
        TelaCadastroInicial telaCadastro = new TelaCadastroInicial(this);
        mainContentPanel.add(telaCadastro, BorderLayout.CENTER);
        statusLabel.setText("Status: Inicie seu cadastro.");
        revalidarConteudo();
    }

    public void mostrarTelaCadastroPerfilDados() {
        mainContentPanel.removeAll();
        TelaCadastroPerfilDados telaDados = new TelaCadastroPerfilDados(this, usuarioEmCadastro);
        mainContentPanel.add(telaDados, BorderLayout.CENTER);
        statusLabel.setText("Status: Preencha seus dados de perfil.");
        revalidarConteudo();
    }

    public void mostrarTelaCadastroPerfilInteresses() {
        mainContentPanel.removeAll();
        TelaCadastroPerfilInteresses telaInteresses = new TelaCadastroPerfilInteresses(this, usuarioEmCadastro);
        mainContentPanel.add(telaInteresses, BorderLayout.CENTER);
        statusLabel.setText("Status: Escolha seus interesses.");
        revalidarConteudo();
    }



    public void loginSucesso(model.Usuario usuario) {
        this.usuarioLogado = usuario;
        mainContentPanel.removeAll();

        // cria a barra de navegação lateral
        JPanel navPanel = criarPainelNavegacao();
        add(navPanel, BorderLayout.WEST);

        statusLabel.setText("Status: Bem-vindo(a), " + usuarioLogado.getNome() + "!");

        // exibe o painel de eventos
        mostrarPainel(new PainelEvento(usuarioLogado.getIdUsuario()), "Eventos Disponíveis");

        revalidarConteudo();
    }

    public void cadastroCompleto(model.Usuario novoUsuario) {
        if (usuarioController.cadastrarUsuario(novoUsuario)) {
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso! Faça login para continuar.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            mostrarTelaLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao finalizar cadastro. Verifique se o email já está em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
            mostrarTelaCadastroPerfilInteresses();
        }
    }


    private JPanel criarPainelNavegacao() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setPreferredSize(new Dimension(150, getHeight()));
        navPanel.setBackground(new Color(36, 44, 62)); // Fundo escuro do painel lateral
        navPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(60, 75, 100))); // Linha divisória sutil

        JLabel title = new JLabel("Módulos");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        navPanel.add(Box.createVerticalStrut(15));
        navPanel.add(title);
        navPanel.add(Box.createVerticalStrut(20));

        int idLogado = usuarioLogado != null ? usuarioLogado.getIdUsuario() : -1;


        adicionarBotaoNavegacao(navPanel, "Meu Perfil", () -> new PainelUsuario(idLogado));
        adicionarBotaoNavegacao(navPanel, "Eventos", () -> new PainelEvento(idLogado));
        adicionarBotaoNavegacao(navPanel, "Amizades", () -> new PainelAmizades(idLogado));
        adicionarBotaoNavegacao(navPanel, "Tags", () -> new PainelTags());

        JButton logoutButton = criarBotaoEstilizado("Sair", new Color(180, 50, 50)); // Botão Sair em vermelho
        logoutButton.addActionListener(e -> mostrarTelaLogin());

        navPanel.add(Box.createVerticalGlue());
        navPanel.add(logoutButton);
        navPanel.add(Box.createVerticalStrut(15));

        return navPanel;
    }

    private void adicionarBotaoNavegacao(JPanel navPanel, String titulo, java.util.function.Supplier<JPanel> panelSupplier) {
        JButton button = criarBotaoEstilizado(titulo, new Color(36, 44, 62)); // Cor de fundo neutra


        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(42, 110, 203)); // Azul no hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(36, 44, 62)); // Volta para o neutro
            }
        });

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(140, 35));
        button.addActionListener(e -> mostrarPainel(panelSupplier.get(), titulo));
        navPanel.add(button);
        navPanel.add(Box.createVerticalStrut(10));
    }


    private JButton criarBotaoEstilizado(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }


    private void mostrarPainel(JPanel novoPainel, String titulo) {
        mainContentPanel.removeAll();
        mainContentPanel.setBorder(null);
        mainContentPanel.add(novoPainel, BorderLayout.CENTER);
        revalidarConteudo();
    }

    private void revalidarConteudo() {
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    public Usuario getUsuarioEmCadastro() {
        return usuarioEmCadastro;
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(SistemaEventosApp::new);
    }
}