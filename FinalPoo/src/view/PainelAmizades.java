package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controller.UsuarioController;
import controller.AmizadeController;
import model.Usuario;
import model.Amizade;

public class PainelAmizades extends JPanel {

    private final int idUsuarioLogado;
    private final UsuarioController usuarioController;
    private final AmizadeController amizadeController;
    private JTabbedPane tabbedPane;


    private JPanel amigosContainer;


    private JTextField buscarCampo;
    private JPanel resultadosContainer;

    public PainelAmizades(int idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
        this.usuarioController = new UsuarioController();
        this.amizadeController = new AmizadeController();

        setBackground(new Color(24, 30, 48));
        setLayout(new BorderLayout(10, 10));


        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(36, 44, 62));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));


        tabbedPane.addTab("Meus Amigos", criarPainelListaAmigos());


        tabbedPane.addTab("Solicitações Pendentes", criarPainelSolicitacoesPendentes());


        tabbedPane.addTab("Buscar Usuários", criarPainelBuscarUsuarios());


        tabbedPane.addChangeListener(e -> {
            String tituloAba = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            if (tituloAba.equals("Solicitações Pendentes")) {
                carregarSolicitacoesPendentes();
            } else if (tituloAba.equals("Meus Amigos")) {
                carregarListaAmigos();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }



    private JPanel criarPainelListaAmigos() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(36, 44, 62));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = criarLabel("Meus Amigos", Color.WHITE, new Font("SansSerif", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);


        amigosContainer = new JPanel();
        amigosContainer.setLayout(new BoxLayout(amigosContainer, BoxLayout.Y_AXIS));
        amigosContainer.setBackground(new Color(36, 44, 62));

        JScrollPane scrollPane = new JScrollPane(amigosContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);


        carregarListaAmigos();

        return panel;
    }


    private void carregarListaAmigos() {
        amigosContainer.removeAll();

        List<Amizade> amizades = amizadeController.listarAmigos(idUsuarioLogado);

        if (amizades.isEmpty()) {
            amigosContainer.add(criarLabel("Você ainda não tem amigos aceitos.",
                    Color.LIGHT_GRAY, new Font("SansSerif", Font.ITALIC, 16)));
        } else {
            for (Amizade amizade : amizades) {
                int idAmigo = (amizade.getIdSolicitante() == idUsuarioLogado) ?
                        amizade.getIdReceptor() :
                        amizade.getIdSolicitante();

                Usuario amigo = usuarioController.buscarUsuarioPorId(idAmigo);

                if (amigo != null) {
                    adicionarItemAmigo(amigo);
                    amigosContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }
        }

        amigosContainer.revalidate();
        amigosContainer.repaint();
    }



    private void adicionarItemAmigo(Usuario amigo) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
        itemPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        itemPanel.setBackground(new Color(45, 55, 75));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 75, 100), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));


        JLabel infoLabel = criarLabel("<html><b>" + amigo.getNome() + "</b> <font color='#AAAAAA'>(" + amigo.getEmail() + ")</font></html>",
                Color.WHITE, new Font("SansSerif", Font.PLAIN, 14));
        itemPanel.add(infoLabel, BorderLayout.CENTER);

        JButton acaoButton = criarBotaoEstilizado("Desfazer Amizade", new Color(203, 42, 42));
        acaoButton.setPreferredSize(new Dimension(150, 30));

        acaoButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(this, "A lógica de 'Desfazer Amizade' precisa ser implementada no AmizadeController.", "Aviso", JOptionPane.INFORMATION_MESSAGE);

        });

        itemPanel.add(acaoButton, BorderLayout.EAST);
        amigosContainer.add(itemPanel);
    }



    private JPanel criarPainelSolicitacoesPendentes() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(36, 44, 62));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = criarLabel("Solicitações de Amizade Recebidas", Color.WHITE, new Font("SansSerif", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);


        JPanel solicitacoesContainer = new JPanel();
        solicitacoesContainer.setLayout(new BoxLayout(solicitacoesContainer, BoxLayout.Y_AXIS));
        solicitacoesContainer.setBackground(new Color(36, 44, 62));
        this.resultadosContainer = solicitacoesContainer;

        JScrollPane scrollPane = new JScrollPane(solicitacoesContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);


        carregarSolicitacoesPendentes();

        return mainPanel;
    }

    private void carregarSolicitacoesPendentes() {
        resultadosContainer.removeAll();

        List<Amizade> pendentes = amizadeController.listarPendentes(idUsuarioLogado);

        if (pendentes.isEmpty()) {
            resultadosContainer.add(criarLabel("Você não tem solicitações de amizade pendentes no momento.",
                    Color.LIGHT_GRAY, new Font("SansSerif", Font.ITALIC, 16)));
        } else {
            for (Amizade solicitacao : pendentes) {
                Usuario solicitante = usuarioController.buscarUsuarioPorId(solicitacao.getIdSolicitante());

                if (solicitante != null) {
                    adicionarItemSolicitacao(solicitante, solicitacao.getIdSolicitante(), solicitacao.getIdReceptor());
                    resultadosContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }
        }

        resultadosContainer.revalidate();
        resultadosContainer.repaint();
    }

    private void adicionarItemSolicitacao(Usuario solicitante, int idSolicitante, int idReceptor) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
        itemPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        itemPanel.setBackground(new Color(45, 55, 75));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 75, 100), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel nomeLabel = criarLabel("<html><b>" + solicitante.getNome() + "</b> enviou uma solicitação.</html>",
                Color.WHITE, new Font("SansSerif", Font.PLAIN, 15));
        itemPanel.add(nomeLabel, BorderLayout.CENTER);

        JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acoesPanel.setOpaque(false);

        JButton aceitarButton = criarBotaoEstilizado("Aceitar", new Color(13, 169, 120));
        aceitarButton.setPreferredSize(new Dimension(100, 30));

        JButton recusarButton = criarBotaoEstilizado("Recusar", new Color(203, 42, 42));
        recusarButton.setPreferredSize(new Dimension(100, 30));


        aceitarButton.addActionListener(e -> {
            boolean sucesso = amizadeController.aceitarSolicitacao(idSolicitante, idReceptor);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Você e " + solicitante.getNome() + " agora são amigos!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarSolicitacoesPendentes();

                carregarListaAmigos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao aceitar solicitação.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });


        recusarButton.addActionListener(e -> {
            boolean sucesso = amizadeController.recusarSolicitacao(idSolicitante, idReceptor);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Solicitação de " + solicitante.getNome() + " recusada.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarSolicitacoesPendentes();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao recusar solicitação.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        acoesPanel.add(aceitarButton);
        acoesPanel.add(recusarButton);

        itemPanel.add(acoesPanel, BorderLayout.EAST);

        resultadosContainer.add(itemPanel);
    }


    private JPanel criarPainelBuscarUsuarios() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(36, 44, 62));

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);

        buscarCampo = criarCampoTextoEstilizado(30);
        buscarCampo.setToolTipText("Digite o nome ou telefone do usuário...");
        searchPanel.add(buscarCampo, BorderLayout.CENTER);

        JButton buscarButton = criarBotaoEstilizado("Buscar", new Color(42, 110, 203));
        buscarButton.setPreferredSize(new Dimension(100, 35));
        buscarButton.addActionListener(e -> realizarBuscaUsuarios());
        searchPanel.add(buscarButton, BorderLayout.EAST);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel buscaResultadosContainer = new JPanel();
        buscaResultadosContainer.setLayout(new BoxLayout(buscaResultadosContainer, BoxLayout.Y_AXIS));
        buscaResultadosContainer.setBackground(new Color(36, 44, 62));
        this.resultadosContainer = buscaResultadosContainer;

        JScrollPane scrollPane = new JScrollPane(buscaResultadosContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private void realizarBuscaUsuarios() {
        String termo = buscarCampo.getText().trim();
        resultadosContainer.removeAll();

        if (termo.isEmpty()) {
            resultadosContainer.add(criarLabel("Digite um nome, email ou telefone para buscar.", Color.GRAY, new Font("SansSerif", Font.ITALIC, 14)));
        } else {

            List<Usuario> resultados = usuarioController.buscarPorNomeOuTelefone(termo);

            if (resultados.isEmpty()) {
                resultadosContainer.add(criarLabel("Nenhum usuário encontrado com o termo '" + termo + "'.", Color.LIGHT_GRAY, new Font("SansSerif", Font.ITALIC, 14)));
            } else {
                for (Usuario usuario : resultados) {
                    if (usuario.getIdUsuario() != idUsuarioLogado) {
                        adicionarResultadoUsuario(usuario);
                        resultadosContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                    }
                }
            }
        }

        resultadosContainer.revalidate();
        resultadosContainer.repaint();
    }

    private void adicionarResultadoUsuario(Usuario usuario) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
        itemPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        itemPanel.setBackground(new Color(45, 55, 75));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 75, 100), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        JLabel infoLabel = criarLabel("<html><b>" + usuario.getNome() + "</b> <font color='#AAAAAA'>(" + usuario.getEmail() + ")</font></html>", Color.WHITE, new Font("SansSerif", Font.PLAIN, 14));
        itemPanel.add(infoLabel, BorderLayout.CENTER);

        JButton acaoButton = criarBotaoEstilizado("Enviar Pedido", new Color(13, 169, 120));
        acaoButton.setPreferredSize(new Dimension(130, 35));

        acaoButton.addActionListener(e -> {
            boolean sucesso = amizadeController.enviarSolicitacao(idUsuarioLogado, usuario.getIdUsuario());

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Solicitação de amizade enviada para " + usuario.getNome() + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                acaoButton.setText("Solicitação Enviada");
                acaoButton.setEnabled(false);
                acaoButton.setBackground(new Color(100, 100, 100));
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao enviar solicitação. Verifique se o pedido já foi enviado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemPanel.add(acaoButton, BorderLayout.EAST);
        resultadosContainer.add(itemPanel);
    }


    private JLabel criarLabel(String text, Color cor, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(cor);
        label.setFont(font);
        return label;
    }

    private JTextField criarCampoTextoEstilizado(int cols) {
        JTextField field = new JTextField(cols);
        field.setPreferredSize(new Dimension(200, 35));
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