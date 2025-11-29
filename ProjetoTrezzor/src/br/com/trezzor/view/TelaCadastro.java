package br.com.trezzor.view;

import br.com.trezzor.dao.UsuarioDAO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.SQLException;
// --- Imports explícitos para corrigir os erros ---
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Component; // Import para o alinhamento

public class TelaCadastro extends JFrame {

    // Cores e Fontes (mesmo estilo do Login)
    private final Color COR_FUNDO = new Color(20, 25, 40);
    private final Color COR_TEXTO = new Color(150, 160, 180);
    private final Color COR_DESTAQUE = new Color(50, 180, 255);
    private final Font FONTE_TEXTO = new Font("Consolas", Font.PLAIN, 14);

    // Campos que precisam ser acessados por toda a classe
    private JTextField txtNome, txtEmail, txtSenha, txtConfirmaSenha;
    private JTextField txtCep, txtCidade, txtRua, txtNumero;
    
    // Gerenciador de "Telas"
    private CardLayout cardLayout;
    private JPanel painelPrincipal;
    
    private UsuarioDAO usuarioDAO;

    public TelaCadastro() {
        usuarioDAO = new UsuarioDAO();
        
        setTitle("Trezzor - Cadastro");
        setSize(400, 600); // Mesmo tamanho do Login
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE fecha só essa janela
        setLocationRelativeTo(null);
        setResizable(false);
        
        // --- Gerenciador de "Cards" ---
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);
        
        // --- PASSO 1: DADOS CADASTRAIS ---
        JPanel passo1 = criarPainelPasso1();
        
        // --- PASSO 2: ENDEREÇO ---
        JPanel passo2 = criarPainelPasso2();

        // Adiciona os passos ao gerenciador
        painelPrincipal.add(passo1, "passo1");
        painelPrincipal.add(passo2, "passo2");
        
        add(painelPrincipal); // Adiciona o gerenciador à janela
        
        // Mostra o primeiro passo
        cardLayout.show(painelPrincipal, "passo1");
    }

    private JPanel criarPainelPasso1() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        painel.add(criarTitulo("TREZZOR"));
        painel.add(criarSubtitulo("Dados cadastrais:"));
        painel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Form
        painel.add(criarLabel("Nome Completo:"));
        txtNome = new JTextField();
        estilizarCampo(txtNome);
        painel.add(txtNome);
        
        painel.add(criarLabel("E-mail:"));
        txtEmail = new JTextField();
        estilizarCampo(txtEmail);
        painel.add(txtEmail);
        
        painel.add(criarLabel("Senha:"));
        txtSenha = new JPasswordField();
        estilizarCampo(txtSenha);
        painel.add(txtSenha);
        
        painel.add(criarLabel("Confirmar senha:"));
        txtConfirmaSenha = new JPasswordField();
        estilizarCampo(txtConfirmaSenha);
        painel.add(txtConfirmaSenha);
        
        painel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Botão
        JButton btnProximo = new JButton("Confirmar");
        estilizarBotaoAcessar(btnProximo);
        painel.add(btnProximo);
        painel.add(Box.createVerticalGlue()); // Empurra tudo pra cima

        // Ação do Botão
        btnProximo.addActionListener(e -> irParaPasso2());
        
        return painel;
    }

    private JPanel criarPainelPasso2() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        painel.add(criarTitulo("TREZZOR"));
        painel.add(criarSubtitulo("Endereço:"));
        painel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form
        painel.add(criarLabel("CEP:"));
        txtCep = new JTextField();
        estilizarCampo(txtCep);
        painel.add(txtCep);
        
        painel.add(criarLabel("Cidade/Estado:"));
        txtCidade = new JTextField();
        estilizarCampo(txtCidade);
        painel.add(txtCidade);
        
        painel.add(criarLabel("Rua:"));
        txtRua = new JTextField();
        estilizarCampo(txtRua);
        painel.add(txtRua);
        
        painel.add(criarLabel("Número:"));
        txtNumero = new JTextField();
        estilizarCampo(txtNumero);
        painel.add(txtNumero);
        
        painel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Botão
        JButton btnFinalizar = new JButton("Confirmar");
        estilizarBotaoAcessar(btnFinalizar);
        painel.add(btnFinalizar);
        painel.add(Box.createVerticalGlue()); // Empurra tudo pra cima

        // Ação do Botão
        btnFinalizar.addActionListener(e -> finalizarCadastro());
        
        return painel;
    }
    
    // --- LÓGICA DE NAVEGAÇÃO E CADASTRO ---
    
    private void irParaPasso2() {
        // Validação do Passo 1
        String senha = new String(((JPasswordField)txtSenha).getPassword());
        String confirma = new String(((JPasswordField)txtConfirmaSenha).getPassword());
        
        if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e E-mail são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!senha.equals(confirma)) {
            JOptionPane.showMessageDialog(this, "As senhas não são iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Se tudo estiver OK, muda de tela
        cardLayout.show(painelPrincipal, "passo2");
    }
    
    private void finalizarCadastro() {
        // Aqui pegamos os dados do Passo 1
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = new String(((JPasswordField)txtSenha).getPassword());

        // (Aqui você pegaria os dados do endereço se o banco suportasse)
        // String cep = txtCep.getText();
        // ... etc ...
        
        try {
            // Manda salvar no banco
            usuarioDAO.cadastrar(nome, email, senha);
            
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!\nFaça o login.");
            
            // Fecha a janela de cadastro
            this.dispose(); 
            
        } catch (SQLException ex) {
            // Trata erro de e-mail duplicado
            if (ex.getMessage().contains("UNIQUE KEY constraint")) {
                JOptionPane.showMessageDialog(this, "Este E-mail já está cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + ex.getMessage());
            }
        }
    }

    // =================================================================================
    // MÉTODOS DE ESTILO (Copiados da TelaLogin)
    // =================================================================================

    private JLabel criarTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Consolas", Font.BOLD, 48));
        lbl.setForeground(COR_DESTAQUE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }
    
    private JLabel criarSubtitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Consolas", Font.PLAIN, 18));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

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
}