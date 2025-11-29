package br.com.trezzor.view;

import br.com.trezzor.dao.UsuarioDAO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

// Imports explícitos para evitar erros no NetBeans
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Component;

public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private UsuarioDAO usuarioDAO;

    // --- PALETA DE CORES ---
    private final Color COR_FUNDO = new Color(20, 25, 40); // Azul escuro
    private final Color COR_TEXTO = new Color(150, 160, 180); // Cinza
    private final Color COR_DESTAQUE = new Color(50, 180, 255); // Azul Neon
    private final Font FONTE_TEXTO = new Font("Consolas", Font.PLAIN, 14);
    private final Font FONTE_LINK = new Font("Consolas", Font.BOLD, 12);

    public TelaLogin() {
        usuarioDAO = new UsuarioDAO();

        setTitle("Trezzor - Login");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Painel Principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Mola para centralizar verticalmente
        painelPrincipal.add(Box.createVerticalGlue());

        // --- 1. LOGO (Texto) ---
        JLabel lblLogo = new JLabel("TREZZOR");
        lblLogo.setFont(new Font("Consolas", Font.BOLD, 48));
        lblLogo.setForeground(COR_DESTAQUE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(lblLogo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- 2. BOTÕES SOCIAIS (Decorativos) ---
        JPanel painelSocial = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        painelSocial.setOpaque(false);
        JButton btnGoogle = criarBotaoSocial("G");
        JButton btnFacebook = criarBotaoSocial("F");
        painelSocial.add(btnGoogle);
        painelSocial.add(btnFacebook);
        painelPrincipal.add(painelSocial);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- 3. CAMPO E-MAIL ---
        JLabel lblEmail = criarLabel("E-mail ou usuário");
        painelPrincipal.add(lblEmail);
        txtEmail = new JTextField("admin@trezzor.com");
        estilizarCampo(txtEmail);
        painelPrincipal.add(txtEmail);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // --- 4. CAMPO SENHA ---
        JLabel lblSenha = criarLabel("Senha");
        painelPrincipal.add(lblSenha);
        txtSenha = new JPasswordField();
        estilizarCampo(txtSenha);
        painelPrincipal.add(txtSenha);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // --- 5. BOTÃO ACESSAR ---
        JButton btnAcessar = new JButton("Acessar");
        estilizarBotaoAcessar(btnAcessar);
        painelPrincipal.add(btnAcessar);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // --- 6. LINKS ---
        JLabel linkEsqueceu = criarLink("Esqueceu sua senha? ", "Clique aqui");
        painelPrincipal.add(linkEsqueceu);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel linkCadastro = criarLink("Não possui login? ", "Fazer Cadastro");
        painelPrincipal.add(linkCadastro);

        // AÇÃO: Clicar em "Fazer Cadastro" abre a tela de cadastro
        linkCadastro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaCadastro().setVisible(true);
            }
        });

        painelPrincipal.add(Box.createVerticalGlue());
        add(painelPrincipal);

        // --- AÇÕES DE LOGIN ---
        btnAcessar.addActionListener(e -> tentarLogin());
        txtSenha.addActionListener(e -> tentarLogin());
    }

    // =================================================================================
    // LÓGICA DO LOGIN (ATUALIZADA)
    // =================================================================================
    
    private void tentarLogin() {
        try {
            String email = txtEmail.getText();
            String senha = new String(txtSenha.getPassword());

            if (usuarioDAO.validarLogin(email, senha)) {
                // --- AQUI ESTÁ A MUDANÇA: Abre a tela de SELEÇÃO ---
                new TelaSelecao().setVisible(true); 
                this.dispose(); // Fecha o login
            } else {
                JOptionPane.showMessageDialog(this, 
                    "E-mail ou senha incorretos.", 
                    "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage());
        }
    }

    // =================================================================================
    // MÉTODOS DE DESIGN (Estilo Gamer)
    // =================================================================================

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONTE_TEXTO);
        lbl.setForeground(COR_TEXTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT); 
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl.getPreferredSize().height));
        return lbl;
    }
    
    private void estilizarCampo(JTextField campo) {
        campo.setBackground(COR_FUNDO);
        campo.setForeground(Color.WHITE);
        campo.setFont(new Font("Consolas", Font.PLAIN, 16));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        Border bordaInferior = BorderFactory.createMatteBorder(0, 0, 1, 0, COR_TEXTO);
        Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        campo.setBorder(BorderFactory.createCompoundBorder(bordaInferior, padding));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, campo.getPreferredSize().height));
    }

    private JButton criarBotaoSocial(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Consolas", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(50, 60, 90));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(60, 60));
        btn.setBorder(BorderFactory.createLineBorder(COR_DESTAQUE));
        btn.setFocusPainted(false);
        return btn;
    }
    
    private void estilizarBotaoAcessar(JButton btn) {
        btn.setBackground(COR_DESTAQUE);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Consolas", Font.BOLD, 18));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    private JLabel criarLink(String textoNormal, String textoLink) {
        JLabel lbl = new JLabel("<html>" + textoNormal + "<font color='#32B4FF'>" + textoLink + "</font></html>");
        lbl.setFont(FONTE_LINK);
        lbl.setForeground(COR_TEXTO);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return lbl;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}