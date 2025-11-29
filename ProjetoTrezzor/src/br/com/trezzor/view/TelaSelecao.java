package br.com.trezzor.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaSelecao extends JFrame {

    private final Color COR_FUNDO = new Color(20, 25, 40);
    private final Color COR_DESTAQUE = new Color(50, 180, 255); // Azul
    private final Color COR_SECUNDARIA = new Color(46, 204, 113); // Verde

    public TelaSelecao() {
        setTitle("Trezzor - Escolha seu Perfil");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Painel de Fundo
        JPanel painelPrincipal = new JPanel(new GridLayout(1, 2, 20, 0)); // 1 linha, 2 colunas
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // --- BOTÃO VENDEDOR ---
        JPanel cardVendedor = criarCard("VENDEDOR", "Gerenciar meus Itens", "📦", COR_DESTAQUE);
        cardVendedor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaItens().setVisible(true); // Abre a tela de gestão antiga
                dispose();
            }
        });

        // --- BOTÃO COMPRADOR ---
        JPanel cardComprador = criarCard("COMPRADOR", "Ver Anúncios", "🛒", COR_SECUNDARIA);
        cardComprador.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaMarketplace().setVisible(true); // Abre a NOVA tela de vitrine
                dispose();
            }
        });

        painelPrincipal.add(cardVendedor);
        painelPrincipal.add(cardComprador);

        // Título no topo
        JLabel lblTitulo = new JLabel("Como você deseja acessar hoje?", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(COR_FUNDO);

        add(lblTitulo, BorderLayout.NORTH);
        add(painelPrincipal, BorderLayout.CENTER);
    }

    private JPanel criarCard(String titulo, String desc, String icone, Color corBorda) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(30, 35, 55)); // Um pouco mais claro que o fundo
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corBorda, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcone = new JLabel(icone);
        lblIcone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblIcone.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIcone.setForeground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(corBorda);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(Color.LIGHT_GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblIcone);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblDesc);
        card.add(Box.createVerticalGlue());

        // Efeito Hover (mudar cor ao passar mouse)
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(40, 45, 65)); }
            public void mouseExited(MouseEvent e) { card.setBackground(new Color(30, 35, 55)); }
        });

        return card;
    }
}