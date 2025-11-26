package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.JTextComponent;
import model.Usuario;

public class TelaCadastroInicial extends JPanel {
    private final SistemaEventosApp mainApp;

    private JTextField emailField;
    private JPasswordField senhaField;
    private JPasswordField confirmarSenhaField;
    private JLabel messageLabel;

    public TelaCadastroInicial(SistemaEventosApp mainApp) {
        this.mainApp = mainApp;
        setBackground(new Color(24, 30, 48));
        setLayout(new GridBagLayout());

        JPanel cadastroPanel = new JPanel();
        cadastroPanel.setLayout(new BoxLayout(cadastroPanel, BoxLayout.Y_AXIS));
        cadastroPanel.setBackground(new Color(24, 30, 48));
        cadastroPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel iconLabel = new JLabel("📝");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 100));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cadastroPanel.add(iconLabel);
        cadastroPanel.add(Box.createVerticalStrut(20));

        // email
        emailField = criarCampoEstilizado("Email", false);
        emailField.setMaximumSize(new Dimension(250, 35));
        cadastroPanel.add(emailField);
        cadastroPanel.add(Box.createVerticalStrut(10));

        // senha
        senhaField = criarCampoEstilizado("Senha", true);
        senhaField.setMaximumSize(new Dimension(250, 35));
        cadastroPanel.add(senhaField);
        cadastroPanel.add(Box.createVerticalStrut(10));

        // confirmar senha
        confirmarSenhaField = criarCampoEstilizado("Confirmar Senha", true);
        confirmarSenhaField.setMaximumSize(new Dimension(250, 35));
        cadastroPanel.add(confirmarSenhaField);
        cadastroPanel.add(Box.createVerticalStrut(20));

        // botao cadastrar
        JButton nextButton = criarBotaoEstilizado("Cadastrar", new Color(42, 110, 203));
        nextButton.addActionListener(e -> proximaEtapaCadastro());
        nextButton.setMaximumSize(new Dimension(250, 35));
        cadastroPanel.add(nextButton);
        cadastroPanel.add(Box.createVerticalStrut(15));

        // voltar para login
        JButton backToLoginButton = new JButton("<html><u>Já tenho uma conta</u></html>");
        backToLoginButton.setForeground(Color.WHITE);
        backToLoginButton.setBackground(new Color(24, 30, 48));
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setFocusPainted(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToLoginButton.addActionListener(e -> mainApp.mostrarTelaLogin());
        cadastroPanel.add(backToLoginButton);
        cadastroPanel.add(Box.createVerticalStrut(10));

        // message
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(255, 100, 100));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cadastroPanel.add(messageLabel);

        add(cadastroPanel);
    }

    private <T extends JTextComponent> T criarCampoEstilizado(String placeholder, boolean isPassword) {
        JTextComponent field;
        if (isPassword) {
            field = new JPasswordField(placeholder);
            ((JPasswordField) field).setEchoChar((char) 0); // Mostra o placeholder
        } else {
            field = new JTextField(placeholder);
        }

        field.setForeground(Color.GRAY);
        field.setBackground(new Color(36, 44, 62));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 75, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setCaretColor(Color.WHITE);

        //  alinhamento

        if (field instanceof JTextField) {
            ((JTextField) field).setHorizontalAlignment(JTextField.CENTER);
        }


        field.setFont(new Font("SansSerif", Font.PLAIN, 14));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                String currentText;
                if (isPassword) {
                    currentText = new String(((JPasswordField) field).getPassword());
                } else {
                    currentText = ((JTextField) field).getText();
                }

                if (currentText.equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                    if (isPassword) ((JPasswordField) field).setEchoChar('*');
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String currentText;
                if (isPassword) {
                    currentText = new String(((JPasswordField) field).getPassword());
                } else {
                    currentText = ((JTextField) field).getText();
                }

                if (currentText.isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    if (isPassword) ((JPasswordField) field).setEchoChar((char) 0);
                }
            }
        });

        return (T) field;
    }

    private JButton criarBotaoEstilizado(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }

    private void proximaEtapaCadastro() {
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());
        String confirmarSenha = new String(confirmarSenhaField.getPassword());


        if (email.equals("Email")) email = "";
        if (senha.equals("Senha")) senha = "";
        if (confirmarSenha.equals("Confirmar Senha")) confirmarSenha = "";

        if (email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            messageLabel.setText("Preencha todos os campos.");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            messageLabel.setText("As senhas não coincidem.");
            return;
        }

        Usuario usuarioEmCadastro = mainApp.getUsuarioEmCadastro();
        usuarioEmCadastro.setEmail(email);
        usuarioEmCadastro.setSenha(senha);

        mainApp.mostrarTelaCadastroPerfilDados();
    }
}
