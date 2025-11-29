package br.com.trezzor.view;

import br.com.trezzor.dao.ItemDAO;
import br.com.trezzor.model.Item;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TelaMarketplace extends JFrame {

    private ItemDAO itemDAO;
    private JPanel painelItens;

    public TelaMarketplace() {
        itemDAO = new ItemDAO();
        
        setTitle("Mercado Trezzor - Vitrine de Ofertas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- CABEÇALHO ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(46, 204, 113)); // Verde Marketplace
        header.setPreferredSize(new Dimension(1000, 80));
        header.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel lblLogo = new JLabel("TREZZOR SHOP");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);

        JButton btnSair = new JButton("Voltar");
        btnSair.setBackground(new Color(39, 174, 96));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(e -> {
            new TelaSelecao().setVisible(true);
            dispose();
        });

        header.add(lblLogo, BorderLayout.WEST);
        header.add(btnSair, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- ÁREA DE ROLAGEM (VITRINE) ---
        painelItens = new JPanel();
        painelItens.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 20)); // Layout especial (veja nota abaixo)
        painelItens.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(painelItens);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // Rolagem mais suave
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        carregarAnuncios();
    }

    private void carregarAnuncios() {
        try {
            List<Item> itens = itemDAO.listar();
            
            if (itens.isEmpty()) {
                JLabel lblVazio = new JLabel("Nenhum item à venda no momento.");
                lblVazio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                painelItens.add(lblVazio);
            }

            for (Item item : itens) {
                painelItens.add(criarCardAnuncio(item));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vitrine: " + e.getMessage());
        }
    }

    private JPanel criarCardAnuncio(Item item) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(220, 300));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Ícone do Item (Placeholder)
        JLabel lblIcon = new JLabel("📦");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nome
        JLabel lblNome = new JLabel(item.getNome());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Categoria
        JLabel lblCat = new JLabel(item.getCategoria());
        lblCat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCat.setForeground(Color.GRAY);
        lblCat.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Preço Formatado
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        JLabel lblPreco = new JLabel(nf.format(item.getValorEstimado()));
        lblPreco.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPreco.setForeground(new Color(46, 204, 113)); // Verde preço
        lblPreco.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botão Comprar
        JButton btnComprar = new JButton("Comprar");
        btnComprar.setBackground(new Color(52, 152, 219));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnComprar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Você demonstrou interesse em: " + item.getNome() + "\nEntre em contato com o vendedor!");
        });

        // Montando o Card
        card.add(Box.createVerticalGlue());
        card.add(lblIcon);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblNome);
        card.add(lblCat);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblPreco);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(btnComprar);
        card.add(Box.createVerticalGlue());

        return card;
    }
    
    // Classe interna para fazer os itens quebrarem linha automaticamente
    // Se der erro, troque por "new FlowLayout()" lá em cima, mas o visual fica pior
    class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
        @Override public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        @Override public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }
        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
                int width = 0, height = 0, rowWidth = 0, rowHeight = 0;
                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            width = Math.max(width, rowWidth);
                            height += rowHeight + vgap;
                            rowWidth = 0; rowHeight = 0;
                        }
                        rowWidth += d.width + hgap;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                width = Math.max(width, rowWidth);
                height += rowHeight + vgap;
                return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
            }
        }
    }
}