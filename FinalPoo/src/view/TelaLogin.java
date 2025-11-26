package view;
import javax.swing.text.JTextComponent;
import javax.swing.*;
import java.awt.*;
import controller.UsuarioController;
import model.Usuario;

public class TelaLogin extends JPanel {
    private final SistemaEventosApp mainApp;
    private final UsuarioController usuarioController = new UsuarioController();

    private JTextField emailField;
    private JPasswordField senhaField;
    private JLabel messageLabel;

    public TelaLogin(SistemaEventosApp mainApp) {
        this.mainApp = mainApp;
        setBackground(new Color(24, 30, 48)); // Fundo escuro
        setLayout(new GridBagLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(new Color(24, 30, 48));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // icone da tela de login
        JLabel iconLabel = new JLabel("👥"); // Usando um emoji como placeholder
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 100));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(iconLabel);
        loginPanel.add(Box.createVerticalStrut(20));

        // campo de email
        emailField = criarCampoEstilizado("Email", false);
        emailField.setMaximumSize(new Dimension(250, 35));
        loginPanel.add(emailField);
        loginPanel.add(Box.createVerticalStrut(10));

        // campo de senha
        senhaField = criarCampoEstilizado("Senha", true);
        senhaField.setMaximumSize(new Dimension(250, 35));
        loginPanel.add(senhaField);
        loginPanel.add(Box.createVerticalStrut(20));

        // botao de login
        JButton loginButton = criarBotaoEstilizado("Login", new Color(42, 110, 203));
        loginButton.addActionListener(e -> tentarLogin());
        loginButton.setMaximumSize(new Dimension(250, 35));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(15));

        // botao de cadastro
        JButton signupButton = new JButton("<html><u>Se inscreva</u></html>");
        signupButton.setForeground(Color.WHITE);
        signupButton.setBackground(new Color(24, 30, 48));
        signupButton.setBorderPainted(false);
        signupButton.setFocusPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.addActionListener(e -> mainApp.mostrarTelaCadastroInicial());
        loginPanel.add(signupButton);
        loginPanel.add(Box.createVerticalStrut(10));


        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(255, 100, 100));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(messageLabel);

        add(loginPanel);
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

    private void tentarLogin() {
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());

        // Limpa o placeholder para validação
        if (email.equals("Email")) email = "";
        if (senha.equals("Senha")) senha = "";

        if (email.isEmpty() || senha.isEmpty()) {
            messageLabel.setText("Por favor, insira seu email e senha.");
            return;
        }

        Usuario usuario = usuarioController.login(email, senha);

        if (usuario != null) {
            messageLabel.setText("Login bem-sucedido!");
            mainApp.loginSucesso(usuario);
        } else {
            messageLabel.setText("Email ou senha incorretos.");
            senhaField.setText("Senha");
            senhaField.setForeground(Color.GRAY);
            ((JPasswordField)senhaField).setEchoChar((char) 0);
        }
    }
}