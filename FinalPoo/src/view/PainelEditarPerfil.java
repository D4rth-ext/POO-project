package view;

import controller.UsuarioController;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class PainelEditarPerfil extends JPanel {

    private final int idUsuarioLogado;
    private final UsuarioController usuarioController;

    private JTextField nomeField;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JPasswordField confirmarSenhaField;

    private Usuario usuarioAtual;

    public PainelEditarPerfil(int idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
        this.usuarioController = new UsuarioController();

        setLayout(new BorderLayout());
        setBackground(new Color(36, 44, 62));

        carregarDadosUsuario();

        if (usuarioAtual != null) {
            add(criarFormularioEdicao(), BorderLayout.CENTER);
        } else {
            add(criarLabel("Erro: Não foi possível carregar o perfil do usuário.",
                    Color.RED, new Font("SansSerif", Font.BOLD, 16)), BorderLayout.NORTH);
        }
    }

    private void carregarDadosUsuario() {
        this.usuarioAtual = usuarioController.buscarUsuarioPorId(this.idUsuarioLogado);
    }

    private JPanel criarFormularioEdicao() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);

        // Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Nome:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        nomeField = criarCampoTexto(usuarioAtual.getNome());
        formPanel.add(nomeField, gbc);

        //  Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Email:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        emailField = criarCampoTexto(usuarioAtual.getEmail());
        formPanel.add(emailField, gbc);

        //  Nova Senha
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Nova Senha:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        senhaField = new JPasswordField(20);
        senhaField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(senhaField, gbc);

        // Confirmar Senha
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Confirmar Senha:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        confirmarSenhaField = new JPasswordField(20);
        confirmarSenhaField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(confirmarSenhaField, gbc);

        // Painel de Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton salvarButton = criarBotaoEstilizado("Salvar Alterações", new Color(42, 110, 203));
        salvarButton.addActionListener(e -> actionSalvarAlteracoes());

        buttonPanel.add(salvarButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    private void actionSalvarAlteracoes() {
        String nome = nomeField.getText().trim();
        String email = emailField.getText().trim();
        String novaSenha = new String(senhaField.getPassword());
        String confirmaSenha = new String(confirmarSenhaField.getPassword());

        if (nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Email são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioAtual.setNome(nome);
        usuarioAtual.setEmail(email);

        // Lógica para lidar com a senha
        if (!novaSenha.isEmpty() || !confirmaSenha.isEmpty()) {
            if (!novaSenha.equals(confirmaSenha)) {
                JOptionPane.showMessageDialog(this, "A nova senha e a confirmação não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            usuarioAtual.setSenha(novaSenha); // O DAO criptografa
        } else {

        }

        boolean sucesso = usuarioController.atualizarUsuario(usuarioAtual);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            senhaField.setText("");
            confirmarSenhaField.setText("");

            // Fecha o JDialog
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao atualizar o perfil. Verifique os dados ou o console.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel criarLabel(String text, Color cor, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(cor);
        label.setFont(font);
        return label;
    }

    private JTextField criarCampoTexto(String textoInicial) {
        JTextField field = new JTextField(textoInicial);
        field.setPreferredSize(new Dimension(200, 30));
        field.setBackground(new Color(60, 75, 100));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(80, 100, 130)));
        return field;
    }

    private JButton criarBotaoEstilizado(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }
}