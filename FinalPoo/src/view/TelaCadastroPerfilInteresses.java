package view;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import controller.TagController;
import model.Tag;
import model.Usuario;

public class TelaCadastroPerfilInteresses extends JPanel {
    private final SistemaEventosApp mainApp;
    private final Usuario usuario;
    private final TagController tagController = new TagController();

    private JPanel tagsPanel;
    private List<JCheckBox> tagCheckBoxes;
    private JLabel messageLabel;

    public TelaCadastroPerfilInteresses(SistemaEventosApp mainApp, Usuario usuario) {
        this.mainApp = mainApp;
        this.usuario = usuario;
        setBackground(new Color(24, 30, 48));
        setLayout(new GridBagLayout());

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(new Color(36, 44, 62));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // titulo
        JLabel title = new JLabel("Preenchendo o Perfil | Interesses", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        contentPanel.add(title, BorderLayout.NORTH);

        // tags
        tagsPanel = new JPanel();
        tagsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tagsPanel.setBackground(new Color(30, 38, 54));
        tagsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tagCheckBoxes = new ArrayList<>();

        carregarTags();

        JScrollPane scrollPane = new JScrollPane(tagsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

       //botoes
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setBackground(new Color(36, 44, 62));

        JButton backButton = criarBotaoEstilizado("VOLTAR", new Color(60, 75, 100));
        backButton.addActionListener(e -> mainApp.mostrarTelaCadastroPerfilDados());

        JButton finishButton = criarBotaoEstilizado("FINALIZAR CADASTRO", new Color(42, 110, 203));
        finishButton.addActionListener(e -> finalizarCadastro());

        actionPanel.add(backButton);
        actionPanel.add(finishButton);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(255, 100, 100));

        add(contentPanel);
    }

    private JButton criarBotaoEstilizado(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    private void carregarTags() {
        List<Tag> tags = tagController.listarTodas();

        if (tags.isEmpty()) {
            JLabel noTagsLabel = new JLabel("Nenhuma tag disponível. Crie tags no banco de dados primeiro.");
            noTagsLabel.setForeground(Color.WHITE);
            tagsPanel.add(noTagsLabel);
            return;
        }

        for (Tag tag : tags) {
            JCheckBox checkBox = new JCheckBox(tag.getNome());
            checkBox.setForeground(Color.WHITE);
            checkBox.setBackground(new Color(30, 38, 54));
            checkBox.setActionCommand(tag.getNome());
            tagCheckBoxes.add(checkBox);
            tagsPanel.add(checkBox);
        }
    }

    private void finalizarCadastro() {

        List<String> tagsSelecionadas = new ArrayList<>();
        for(JCheckBox cb : tagCheckBoxes) {
            if (cb.isSelected()) {
                tagsSelecionadas.add(cb.getActionCommand());
            }
        }


        usuario.setTags(tagsSelecionadas);

        usuario.setLinks(new ArrayList<>());

        mainApp.cadastroCompleto(usuario);
    }
}