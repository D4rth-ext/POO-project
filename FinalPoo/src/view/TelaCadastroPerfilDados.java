package view;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import model.Usuario;

public class TelaCadastroPerfilDados extends JPanel {
    private final SistemaEventosApp mainApp;
    private final Usuario usuario;

    private JTextField nomeField, telefoneField;
    private JTextArea sobreMimArea;
    private JComboBox<String> sexoCombo, generoCombo;
    private JLabel fotoPerfilLabel;
    private byte[] fotoPerfilBytes;

    public TelaCadastroPerfilDados(SistemaEventosApp mainApp, Usuario usuario) {
        this.mainApp = mainApp;
        this.usuario = usuario;
        setBackground(new Color(24, 30, 48));
        setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(36, 44, 62));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = new JLabel("Preenchendo o Perfil | Dados", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 5;
        formPanel.add(title, gbc);
        gbc.gridwidth = 1;

        // Bloco da Foto
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridheight = 4;
        JPanel fotoContainer = new JPanel(new BorderLayout());
        fotoContainer.setPreferredSize(new Dimension(150, 150));
        fotoContainer.setBackground(new Color(42, 53, 75));

        fotoPerfilLabel = new JLabel("Foto", SwingConstants.CENTER);
        fotoPerfilLabel.setForeground(Color.WHITE);
        fotoPerfilLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        fotoContainer.add(fotoPerfilLabel, BorderLayout.CENTER);

        JButton escolherFotoButton = criarBotaoEstilizado("Escolher Foto", new Color(42, 110, 203));
        escolherFotoButton.addActionListener(e -> escolherFoto());
        fotoContainer.add(escolherFotoButton, BorderLayout.SOUTH);

        formPanel.add(fotoContainer, gbc);
        gbc.gridheight = 1;

        // dados linha 1
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(criarLabelEstilizada("NOME"), gbc);
        gbc.gridx = 2; nomeField = criarCampoTextoEstilizado(20); formPanel.add(nomeField, gbc);

        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(criarLabelEstilizada("SEXO"), gbc);
        gbc.gridx = 4; sexoCombo = criarComboEstilizado(new String[]{"Não Informar", "Masculino", "Feminino"}); formPanel.add(sexoCombo, gbc);

        // dados linha 2
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(criarLabelEstilizada("GÊNERO"), gbc);
        gbc.gridx = 2; generoCombo = criarComboEstilizado(new String[]{"Não Informar", "Heterossexual", "Homossexual", "Bissexual", "Outro"}); formPanel.add(generoCombo, gbc);

        gbc.gridx = 3; gbc.gridy = 2; formPanel.add(criarLabelEstilizada("TELEFONE"), gbc);
        gbc.gridx = 4; telefoneField = criarCampoTextoEstilizado(15); formPanel.add(telefoneField, gbc);

        // sobre min
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 4; formPanel.add(criarLabelEstilizada("SOBRE"), gbc);
        gbc.gridy = 4;
        sobreMimArea = new JTextArea(3, 40);
        sobreMimArea.setBackground(new Color(30, 38, 54));
        sobreMimArea.setForeground(Color.WHITE);
        sobreMimArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sobreMimArea.setCaretColor(Color.WHITE);
        JScrollPane scrollSobre = new JScrollPane(sobreMimArea);
        formPanel.add(scrollSobre, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 5;
        JButton continuarButton = criarBotaoEstilizado("CONTINUAR", new Color(42, 110, 203));
        continuarButton.addActionListener(e -> proximaEtapaPerfil());
        formPanel.add(continuarButton, gbc);

        add(formPanel);
    }



    private JLabel criarLabelEstilizada(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        return label;
    }

    private JTextField criarCampoTextoEstilizado(int cols) {
        JTextField field = new JTextField(cols);
        field.setBackground(new Color(30, 38, 54));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private JComboBox<String> criarComboEstilizado(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(new Color(30, 38, 54));
        combo.setForeground(Color.WHITE);
        combo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return combo;
    }

    private JButton criarBotaoEstilizado(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }


    private void escolherFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolha sua Foto de Perfil");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
                Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                fotoPerfilLabel.setIcon(new ImageIcon(scaledImage));
                fotoPerfilLabel.setText("");

                fotoPerfilBytes = java.nio.file.Files.readAllBytes(file.toPath());

            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar imagem: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void proximaEtapaPerfil() {
        if (nomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }


        usuario.setNome(nomeField.getText().trim());
        usuario.setTelefone(telefoneField.getText().trim());
        usuario.setSexo((String) sexoCombo.getSelectedItem());
        usuario.setGenero((String) generoCombo.getSelectedItem());
        usuario.setSobre_mim(sobreMimArea.getText().trim());
        usuario.setFoto_perfil(fotoPerfilBytes);

        mainApp.mostrarTelaCadastroPerfilInteresses();
    }
}