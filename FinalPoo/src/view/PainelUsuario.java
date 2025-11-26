package view;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.imageio.ImageIO;
import controller.UsuarioController;
import model.Usuario;

public class PainelUsuario extends JPanel {
    private final UsuarioController usuarioController;
    private final int idUsuarioLogado;
    private Usuario usuarioLogado;

    private JLabel fotoLabel;
    private JLabel nomeLabel;
    private JLabel emailLabel;
    private JLabel telefoneLabel;
    private JLabel sexoLabel;
    private JLabel generoLabel;
    private JTextArea sobreMimArea;
    private JPanel tagsContainer;

    public PainelUsuario(int idUsuarioLogado) {
        this.usuarioController = new UsuarioController();
        this.idUsuarioLogado = idUsuarioLogado;

        carregarDadosIniciais();

        setBackground(new Color(24, 30, 48));
        setLayout(new BorderLayout(20, 20));

        if (usuarioLogado == null) {
            exibirErroCarregamento();
            return;
        }

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(36, 44, 62));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        adicionarFotoPerfil(contentPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JPanel infoBasicaPanel = criarPainelInfoBasica();
        contentPanel.add(infoBasicaPanel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(criarLabelCampo("Telefone:", 10), gbc);
        gbc.gridx = 2;
        telefoneLabel = criarLabelValor();
        contentPanel.add(telefoneLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        contentPanel.add(criarLabelCampo("Sexo:", 10), gbc);
        gbc.gridx = 4;
        sexoLabel = criarLabelValor();
        contentPanel.add(sexoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(criarLabelCampo("Gênero:", 10), gbc);
        gbc.gridx = 2;
        generoLabel = criarLabelValor();
        contentPanel.add(generoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        contentPanel.add(criarLabelCampo("SOBRE MIM", 12), gbc);
        gbc.gridy = 4;
        sobreMimArea = criarAreaSobreMim();
        JScrollPane scrollSobre = new JScrollPane(sobreMimArea);
        scrollSobre.setPreferredSize(new Dimension(400, 100));
        contentPanel.add(scrollSobre, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        contentPanel.add(criarLabelCampo("INTERESSES (TAGS)", 12), gbc);
        gbc.gridy = 6;
        tagsContainer = criarPainelTags();
        JScrollPane scrollTags = new JScrollPane(tagsContainer);
        scrollTags.setPreferredSize(new Dimension(400, 80));
        scrollTags.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollTags, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        JButton editarButton = criarBotaoEstilizado("EDITAR PERFIL", new Color(42, 110, 203));
        editarButton.addActionListener(e -> actionEditarPerfil()); // CORREÇÃO APLICADA AQUI
        contentPanel.add(editarButton, gbc);

        add(contentPanel, BorderLayout.CENTER);

        carregarDadosNaTela();
    }

    private void carregarDadosIniciais() {
        this.usuarioLogado = usuarioController.buscarUsuarioPorId(idUsuarioLogado);
    }

    private void carregarDadosNaTela() {
        if (usuarioLogado == null) {
            return;
        }

        nomeLabel.setText(usuarioLogado.getNome());
        emailLabel.setText(usuarioLogado.getEmail());
        telefoneLabel.setText(usuarioLogado.getTelefone() != null && !usuarioLogado.getTelefone().isEmpty() ? usuarioLogado.getTelefone() : "N/A");
        sexoLabel.setText(usuarioLogado.getSexo() != null ? usuarioLogado.getSexo() : "N/A");
        generoLabel.setText(usuarioLogado.getGenero() != null ? usuarioLogado.getGenero() : "N/A");
        sobreMimArea.setText(usuarioLogado.getSobre_mim() != null ? usuarioLogado.getSobre_mim() : "O usuário ainda não preencheu a seção Sobre Mim.");

        carregarFoto();
        carregarTagsDoUsuario();
        revalidate();
        repaint();
    }

    private JLabel criarLabelCampo(String text, int size) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(150, 150, 150));
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        return label;
    }

    private JLabel criarLabelValor() {
        JLabel label = new JLabel("");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return label;
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

    private JTextArea criarAreaSobreMim() {
        JTextArea area = new JTextArea(5, 40);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(30, 38, 54));
        area.setForeground(Color.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return area;
    }

    private void adicionarFotoPerfil(JPanel panel, GridBagConstraints gbc) {
        fotoLabel = new JLabel();
        fotoLabel.setPreferredSize(new Dimension(150, 150));
        fotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fotoLabel.setVerticalAlignment(SwingConstants.CENTER);
        fotoLabel.setBorder(BorderFactory.createLineBorder(new Color(42, 110, 203), 3));
        fotoLabel.setBackground(new Color(50, 60, 80));
        fotoLabel.setOpaque(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(fotoLabel, gbc);
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }

    private JPanel criarPainelInfoBasica() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        nomeLabel = new JLabel("Nome do Usuário");
        nomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        nomeLabel.setForeground(Color.WHITE);

        emailLabel = new JLabel("email@usuario.com");
        emailLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        emailLabel.setForeground(new Color(180, 180, 180));

        panel.add(nomeLabel, BorderLayout.NORTH);
        panel.add(emailLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void carregarFoto() {
        byte[] fotoBytes = usuarioLogado.getFoto_perfil();
        if (fotoBytes != null) {
            try {
                Image img = ImageIO.read(new ByteArrayInputStream(fotoBytes));
                Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                fotoLabel.setIcon(new ImageIcon(scaledImg));
                fotoLabel.setText("");
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem do perfil: " + e.getMessage());
                fotoLabel.setText("📷");
                fotoLabel.setFont(new Font("SansSerif", Font.PLAIN, 40));
            }
        } else {
            fotoLabel.setText("👤");
            fotoLabel.setFont(new Font("SansSerif", Font.PLAIN, 40));
        }
    }

    private void exibirErroCarregamento() {
        removeAll();
        JLabel erroLabel = new JLabel("Erro: Não foi possível carregar os dados do usuário ID " + idUsuarioLogado + ". Verifique o banco de dados e a conexão.");
        erroLabel.setForeground(Color.RED);
        erroLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        setLayout(new GridBagLayout());
        add(erroLabel, new GridBagConstraints());
    }

    private JPanel criarPainelTags() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBackground(new Color(30, 38, 54));
        return panel;
    }

    private void carregarTagsDoUsuario() {
        tagsContainer.removeAll();
        List<String> tags = usuarioLogado.getTags();

        if (tags == null || tags.isEmpty()) {
            JLabel noTags = new JLabel("Nenhuma tag de interesse selecionada.");
            noTags.setForeground(new Color(150, 150, 150));
            tagsContainer.add(noTags);
            return;
        }

        for (String tagName : tags) {
            JLabel tagLabel = new JLabel(tagName);
            tagLabel.setForeground(Color.WHITE);
            tagLabel.setBackground(new Color(42, 110, 203));
            tagLabel.setOpaque(true);
            tagLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            tagLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

            tagsContainer.add(tagLabel);
        }
        tagsContainer.revalidate();
        tagsContainer.repaint();
    }


    private void actionEditarPerfil() {
        try {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Editar Perfil", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            PainelEditarPerfil editarPerfilPanel = new PainelEditarPerfil(this.idUsuarioLogado);

            dialog.setContentPane(editarPerfilPanel);

            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);

            carregarDadosIniciais();
            carregarDadosNaTela();
        } catch (Exception e) {
            System.err.println("ERRO FATAL ao abrir a janela de edição de perfil:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro interno ao abrir a tela de edição. Verifique o console.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}