package view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class PainelTags extends JPanel {

    public PainelTags() {
        setBackground(new Color(24, 30, 48));
        setLayout(new BorderLayout(15, 15));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(36, 44, 62));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel title = new JLabel("Tags de Interesse Disponíveis", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        contentPanel.add(title, BorderLayout.NORTH);

        // Painel de Exibição das Tags
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        tagsPanel.setBackground(new Color(30, 38, 54));

        carregarTagsExemplo(tagsPanel);

        JScrollPane scrollPane = new JScrollPane(tagsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void carregarTagsExemplo(JPanel tagsPanel) {

        List<String> tagsExemplo = Arrays.asList(
                "Música", "Tecnologia", "Esportes", "Gastronomia", "Cinema",
                "Jogos", "Viagem", "Literatura", "Natureza", "Artes Visuais",
                "Programação", "Fitness", "Meditação", "História"
        );

        for (String tagName : tagsExemplo) {
            JLabel tagLabel = new JLabel(tagName);
            tagLabel.setForeground(Color.WHITE);
            tagLabel.setBackground(new Color(42, 110, 203));
            tagLabel.setOpaque(true);
            tagLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            tagLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

            tagsPanel.add(tagLabel);
        }
    }
}