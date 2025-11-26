package view;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import controller.EventoController;
import controller.ParticipanteEventoController;
import model.Evento;

public class PainelEvento extends JPanel {
    private final int idUsuarioLogado;
    private final EventoController eventoController;
    private final ParticipanteEventoController participanteEventoController;

    private JPanel listaEventosPanel;
    private JPanel eventosContainer;
    private JTabbedPane tabbedPane;

    // Campos do formulário de criação
    private JTextField tituloField;
    private JTextArea descricaoArea;
    private JTextField dataField;
    private JTextField horarioField;
    private JTextField localField;
    private JSpinner limiteParticipantesSpinner;
    private JComboBox<String> visibilidadeCombo;

    public PainelEvento(int idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
        this.eventoController = new EventoController();
        this.participanteEventoController = new ParticipanteEventoController();

        setBackground(new Color(24, 30, 48));
        setLayout(new BorderLayout(10, 10));

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(36, 44, 62));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        listaEventosPanel = criarPainelListaEventos();
        tabbedPane.addTab("Eventos Disponíveis", listaEventosPanel);

        JPanel criarEventoPanel = criarPainelCriarEvento();
        tabbedPane.addTab("Criar Evento", criarEventoPanel);

        add(tabbedPane, BorderLayout.CENTER);

        carregarEventos();
    }


    private JPanel criarPainelListaEventos() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(36, 44, 62));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Eventos Disponíveis", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);

        eventosContainer = new JPanel();
        eventosContainer.setLayout(new BoxLayout(eventosContainer, BoxLayout.Y_AXIS));
        eventosContainer.setBackground(new Color(36, 44, 62));

        JScrollPane scrollPane = new JScrollPane(eventosContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarPainelCriarEvento() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(36, 44, 62));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);

        // titulo
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Título:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        tituloField = criarCampoTexto();
        formPanel.add(tituloField, gbc);

        // Descriçao
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(criarLabel("Descrição:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.ipady = 40;
        descricaoArea = criarAreaTexto();
        JScrollPane scrollDescricao = new JScrollPane(descricaoArea);
        scrollDescricao.setBorder(BorderFactory.createLineBorder(new Color(60, 75, 100)));
        formPanel.add(scrollDescricao, gbc);

        // data e horario
        JPanel dataHoraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dataHoraPanel.setOpaque(false);

        dataField = criarCampoTexto();
        dataField.setPreferredSize(new Dimension(100, 30));

        horarioField = criarCampoTexto();
        horarioField.setPreferredSize(new Dimension(60, 30));
        horarioField.setText("18:00");

        dataHoraPanel.add(dataField);
        dataHoraPanel.add(criarLabel("Horário (HH:mm):", Color.WHITE, labelFont));
        dataHoraPanel.add(horarioField);

        gbc.gridx = 0; gbc.gridy = 2; gbc.ipady = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Data do Evento:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(dataHoraPanel, gbc);

        // local
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Local:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        localField = criarCampoTexto();
        formPanel.add(localField, gbc);

        // limite de pessoas
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Limite Part.:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        limiteParticipantesSpinner = new JSpinner(new SpinnerNumberModel(50, 1, 1000, 1));
        limiteParticipantesSpinner.setPreferredSize(new Dimension(100, 30));
        formPanel.add(limiteParticipantesSpinner, gbc);

        // visibilidade
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(criarLabel("Visibilidade:", Color.WHITE, labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0;
        visibilidadeCombo = new JComboBox<>(new String[]{"Público", "Privado"});
        visibilidadeCombo.setPreferredSize(new Dimension(150, 30));
        formPanel.add(visibilidadeCombo, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton limparButton = criarBotaoEstilizado("Limpar Campos", new Color(100, 100, 100));
        limparButton.addActionListener(e -> limparCamposFormulario());

        JButton criarButton = criarBotaoEstilizado("Criar Evento", new Color(42, 110, 203));
        criarButton.addActionListener(e -> actionCriarEvento());

        buttonPanel.add(limparButton);
        buttonPanel.add(criarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }


    private void limparCamposFormulario() {
        tituloField.setText("");
        descricaoArea.setText("");
        dataField.setText("");
        horarioField.setText("18:00");
        localField.setText("");
        limiteParticipantesSpinner.setValue(50);
        visibilidadeCombo.setSelectedIndex(0);
    }

    private void actionCriarEvento() {

        String titulo = tituloField.getText().trim();
        String descricao = descricaoArea.getText().trim();
        String local = localField.getText().trim();
        int limite = (Integer) limiteParticipantesSpinner.getValue();
        String visibilidade = Objects.requireNonNull(visibilidadeCombo.getSelectedItem()).toString();

        String dataStr = dataField.getText().trim();
        String horarioStr = horarioField.getText().trim();
        String dataHoraCompleta = dataStr + " " + horarioStr;

        Timestamp dataHoraEventoSql;

        if (titulo.isEmpty() || local.isEmpty() || dataStr.isEmpty() || horarioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título, Data, Horário e Local são obrigatórios.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(dataHoraCompleta, formatter);

            ZoneId zoneIdBrasil = ZoneId.of("America/Sao_Paulo");
            ZonedDateTime zonedDateTime = localDateTime.atZone(zoneIdBrasil);
            dataHoraEventoSql = Timestamp.from(zonedDateTime.toInstant());

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de Data/Horário inválido. Use dd/MM/yyyy no campo Data e HH:mm no campo Horário.", "Erro de Data/Horário", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Evento novoEvento = new Evento();
        novoEvento.setIdCriador(this.idUsuarioLogado);
        novoEvento.setTitulo(titulo);
        novoEvento.setDescricao(descricao);
        novoEvento.setDataEvento(dataHoraEventoSql);
        novoEvento.setLocal(local);
        novoEvento.setLimiteParticipantes(limite);
        novoEvento.setVisibilidade(visibilidade);

        boolean sucesso = eventoController.cadastrarEvento(novoEvento);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Evento '" + titulo + "' criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            limparCamposFormulario();
            carregarEventos();

            tabbedPane.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao criar evento. Verifique o log do console para detalhes.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void carregarEventos() {
        eventosContainer.removeAll();

        List<Evento> eventos = eventoController.listarEventos();

        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        ZoneId zoneIdBrasil = ZoneId.of("America/Sao_Paulo");

        if (eventos.isEmpty()) {
            eventosContainer.add(criarLabel("Nenhum evento encontrado.", Color.LIGHT_GRAY, new Font("SansSerif", Font.ITALIC, 16)));
        } else {
            for (Evento evento : eventos) {
                String dataStr;
                if (evento.getDataEvento() instanceof Timestamp) {
                    Timestamp timestamp = (Timestamp) evento.getDataEvento();

                    LocalDateTime localDateTimeBrasil = timestamp.toInstant()
                            .atZone(zoneIdBrasil)
                            .toLocalDateTime();

                    dataStr = localDateTimeBrasil.format(displayFormatter);
                } else {
                    dataStr = "Data/Hora Indefinida";
                }

                String detalhes = dataStr + " | Limite: " + evento.getLimiteParticipantes();

                JPanel item = criarItemEvento(
                        evento.getIdEvento(), // CORRIGIDO
                        evento.getTitulo(),
                        detalhes,
                        evento.getVisibilidade(),
                        evento.getDescricao(),
                        evento.getIdCriador()
                );
                eventosContainer.add(item);
                eventosContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }


        eventosContainer.revalidate();
        eventosContainer.repaint();
    }

    private JPanel criarItemEvento(int idEvento, String titulo, String detalhes, String tag, String descricao, int idCriador) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
        itemPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 70));
        itemPanel.setBackground(new Color(45, 55, 75));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 75, 100), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel tituloLabel = criarLabel("<html><b>" + titulo + "</b></html>", Color.WHITE, new Font("SansSerif", Font.BOLD, 16));
        JLabel detalhesLabel = criarLabel("<html><font color='#AAAAAA'>" + detalhes + "</font></html>", Color.WHITE, new Font("SansSerif", Font.PLAIN, 12));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.add(tituloLabel, BorderLayout.NORTH);
        infoPanel.add(detalhesLabel, BorderLayout.SOUTH);

        JPanel acaoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acaoPanel.setOpaque(false);

        JLabel tagLabel = criarLabel(tag, new Color(42, 110, 203), new Font("SansSerif", Font.BOLD, 12));
        acaoPanel.add(tagLabel);

        if (this.idUsuarioLogado == idCriador) {
            JButton excluirButton = criarBotaoEstilizado("Excluir", new Color(203, 42, 42)); // Cor vermelha
            excluirButton.setPreferredSize(new Dimension(80, 30));
            excluirButton.addActionListener(e -> actionExcluirEvento(idEvento, titulo));
            acaoPanel.add(excluirButton);
        } else {

            JButton participarButton = criarBotaoEstilizado("Entrar", new Color(40, 167, 69));
            participarButton.setPreferredSize(new Dimension(80, 30));
            participarButton.addActionListener(e -> actionParticiparEvento(idEvento, titulo));
            acaoPanel.add(participarButton);
        }


        itemPanel.add(infoPanel, BorderLayout.WEST);
        itemPanel.add(acaoPanel, BorderLayout.EAST);

        itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Abre detalhes se o clique não for no botão
                Component source = (Component) evt.getSource();
                if (!(source instanceof JButton || source.getParent() == acaoPanel || source == acaoPanel)) {
                    JOptionPane.showMessageDialog(itemPanel,
                            "Título: " + titulo + "\n" +
                                    "Detalhes: " + detalhes + "\n" +
                                    "Descrição: " + descricao,
                            "Detalhes do Evento",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        return itemPanel;
    }

    private void actionParticiparEvento(int idEvento, String tituloEvento) {

        boolean sucesso = participanteEventoController.participarEvento(this.idUsuarioLogado, idEvento);

        if (sucesso) {
            JOptionPane.showMessageDialog(this,
                    "Você entrou no evento '" + tituloEvento + "' com sucesso!",
                    "Inscrição Concluída", JOptionPane.INFORMATION_MESSAGE);
            carregarEventos();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Falha ao entrar no evento. Verifique se você já está inscrito.",
                    "Erro ao Participar", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void actionExcluirEvento(int idEvento, String tituloEvento) {
        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir o evento '" + tituloEvento + "'?\nEsta ação é irreversível e removerá todos os participantes!",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = eventoController.excluirEvento(idEvento);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Evento '" + tituloEvento + "' excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarEventos();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao excluir o evento. Verifique o log do console para detalhes.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {

                System.err.println("ERRO INESPERADO NA VIEW AO CHAMAR EXCLUIREVENTO (ID: " + idEvento + "):");
                e.printStackTrace();

                JOptionPane.showMessageDialog(this, "Erro crítico na aplicação. Verifique o console.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private JLabel criarLabel(String text, Color cor, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(cor);
        label.setFont(font);
        return label;
    }

    private JTextField criarCampoTexto() {
        JTextField field = new JTextField(20);
        field.setPreferredSize(new Dimension(200, 30));
        field.setBackground(new Color(60, 75, 100));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(80, 100, 130)));
        return field;
    }

    private JTextArea criarAreaTexto() {
        JTextArea area = new JTextArea(5, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(60, 75, 100));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Padding
        return area;
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