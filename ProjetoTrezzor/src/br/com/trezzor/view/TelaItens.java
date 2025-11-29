package br.com.trezzor.view;

import br.com.trezzor.dao.ItemDAO;
import br.com.trezzor.model.Item;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TelaItens extends JFrame {

    private JTextField txtNome, txtCategoria, txtValor;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ItemDAO itemDAO;
    
    // --- PALETA DE CORES (Flat UI) ---
    private final Color COR_MENU = new Color(44, 62, 80);       // Azul Escuro (Sidebar)
    private final Color COR_FUNDO = new Color(236, 240, 241);   // Cinza Fundo
    private final Color COR_DESTAQUE = new Color(52, 152, 219); // Azul Botão
    private final Color COR_VERDE = new Color(46, 204, 113);    // Verde Salvar
    private final Color COR_VERMELHO = new Color(231, 76, 60);  // Vermelho Excluir

    public TelaItens() {
        itemDAO = new ItemDAO();

        setTitle("Mercado Trezzor - Dashboard Administrativo");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =================================================================================
        // 1. SIDEBAR (MENU LATERAL ESQUERDO)
        // =================================================================================
        JPanel menuLateral = new JPanel();
        menuLateral.setBackground(COR_MENU);
        menuLateral.setPreferredSize(new Dimension(250, 600));
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        
        // Logo
        JLabel lblLogo = new JLabel("TREZZOR");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Gestão de Raridades");
        lblSub.setForeground(new Color(189, 195, 199));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adicionando itens ao menu
        menuLateral.add(Box.createRigidArea(new Dimension(0, 30)));
        menuLateral.add(lblLogo);
        menuLateral.add(lblSub);
        menuLateral.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Itens do Menu
        menuLateral.add(criarItemMenu("»  Dashboard"));
        menuLateral.add(criarItemMenu("»  Meus Itens")); // Item Ativo
        menuLateral.add(criarItemMenu("»  Configurações"));
        
        menuLateral.add(Box.createVerticalGlue()); // Empurra o botão Sair para o fundo

        // --- BOTÃO SAIR (CONFIGURADO COM AÇÃO) ---
        JLabel lblSair = criarItemMenu("X  Sair");
        // Adicionamos o clique especificamente aqui
        lblSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Volta para a tela de seleção
                new TelaSelecao().setVisible(true);
                dispose(); // Fecha a tela atual
            }
        });
        menuLateral.add(lblSair);
        
        menuLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        add(menuLateral, BorderLayout.WEST);

        // =================================================================================
        // 2. ÁREA PRINCIPAL (DIREITA)
        // =================================================================================
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(COR_FUNDO);
        
        // Título da Seção
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200,200,200)));
        
        JLabel lblTituloSecao = new JLabel("Cadastrar Novo Item");
        lblTituloSecao.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloSecao.setForeground(COR_MENU);
        header.add(lblTituloSecao);
        
        painelPrincipal.add(header, BorderLayout.NORTH);

        // Conteúdo Central
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        conteudo.setBackground(COR_FUNDO);

        // --- CARD DO FORMULÁRIO ---
        JPanel cardForm = new JPanel(new GridLayout(2, 3, 15, 10));
        cardForm.setBackground(Color.WHITE);
        cardForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,220,220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        cardForm.setMaximumSize(new Dimension(2000, 140));

        // Criando os Inputs
        JPanel pNome = criarInput("Nome do Item:", txtNome = new JTextField());
        JPanel pCat = criarInput("Categoria:", txtCategoria = new JTextField());
        JPanel pValor = criarInput("Valor Estimado (R$):", txtValor = new JTextField());

        cardForm.add(pNome);
        cardForm.add(pCat);
        cardForm.add(pValor);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setBackground(Color.WHITE);
        
        JButton btnSalvar = criarBotao("💾 Salvar", COR_VERDE);
        JButton btnExcluir = criarBotao("🗑️ Excluir", COR_VERMELHO);
        JButton btnAtualizar = criarBotao("🔄 Recarregar", COR_DESTAQUE);

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAtualizar);
        
        // Gambiarra visual para posicionar os botões no Grid
        cardForm.add(new JLabel(""));
        cardForm.add(new JLabel(""));
        cardForm.add(painelBotoes);

        conteudo.add(cardForm);
        conteudo.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- TABELA ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Categoria", "Valor (R$)"}, 0);
        tabela = new JTable(modeloTabela);
        estilizarTabela(tabela);
        
        // Evento: Clique na tabela preenche o formulário
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int linha = tabela.getSelectedRow();
                if (linha != -1) {
                    txtNome.setText(tabela.getValueAt(linha, 1).toString());
                    txtCategoria.setText(tabela.getValueAt(linha, 2).toString());
                    txtValor.setText(tabela.getValueAt(linha, 3).toString());
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        conteudo.add(scrollPane);
        
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        add(painelPrincipal, BorderLayout.CENTER);

        // --- AÇÕES DOS BOTÕES ---
        btnSalvar.addActionListener(e -> salvarItem());
        btnAtualizar.addActionListener(e -> carregarTabela());
        btnExcluir.addActionListener(e -> excluirItem());

        // Carrega a lista ao abrir
        carregarTabela();
    }

    // =================================================================================
    // MÉTODOS AUXILIARES (DESIGN)
    // =================================================================================

    private JPanel criarInput(String label, JTextField campo) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(Color.WHITE);
        
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(100, 100, 100));
        
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        p.add(l, BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    private JLabel criarItemMenu(String texto) {
        JLabel item = new JLabel(texto);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        item.setForeground(new Color(236, 240, 241));
        item.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 10));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { item.setForeground(COR_DESTAQUE); }
            public void mouseExited(MouseEvent e) { item.setForeground(new Color(236, 240, 241)); }
        });
        
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        return item;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 35));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btn.setBackground(cor.darker()); }
            public void mouseExited(MouseEvent evt) { btn.setBackground(cor); }
        });
        return btn;
    }

    private void estilizarTabela(JTable tabela) {
        tabela.setRowHeight(35);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(new Color(230,230,230));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setSelectionBackground(new Color(220, 240, 255));
        tabela.setSelectionForeground(Color.BLACK);
        
        javax.swing.table.JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(COR_MENU);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));
        header.setOpaque(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(0).setMaxWidth(80);
        
        tabela.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Double) {
                    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    value = nf.format(value);
                }
                super.setValue(value);
                setHorizontalAlignment(JLabel.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            }
        });
    }

    // =================================================================================
    // LÓGICA E BANCO DE DADOS
    // =================================================================================

    private void salvarItem() {
        try {
            String nome = txtNome.getText();
            String categoria = txtCategoria.getText();
            // Limpeza para converter "R$ 1.000,00" de volta para double se necessário
            String valorLimpo = txtValor.getText().replace("R$", "").replace(".", "").replace(",", ".").trim();
            double valor = Double.parseDouble(valorLimpo);

            Item item = new Item(0, nome, categoria, valor);
            itemDAO.salvar(item);
            
            JOptionPane.showMessageDialog(this, "Item salvo com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar! Verifique o valor numérico.\n" + ex.getMessage());
        }
    }

    private void excluirItem() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela para excluir!");
            return;
        }
        
        String nome = tabela.getValueAt(linha, 1).toString();
        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir:\n" + nome + "?", "Excluir", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tabela.getValueAt(linha, 0);
                itemDAO.excluir(id);
                carregarTabela();
                limparCampos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Item> lista = itemDAO.listar();
            for (Item i : lista) {
                modeloTabela.addRow(new Object[]{i.getId(), i.getNome(), i.getCategoria(), i.getValorEstimado()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista: " + ex.getMessage());
        }
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtCategoria.setText("");
        txtValor.setText("");
    }

    public static void main(String[] args) {
        try { 
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
                if ("Nimbus".equals(info.getName())) UIManager.setLookAndFeel(info.getClassName());
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> new TelaItens().setVisible(true));
    }
}