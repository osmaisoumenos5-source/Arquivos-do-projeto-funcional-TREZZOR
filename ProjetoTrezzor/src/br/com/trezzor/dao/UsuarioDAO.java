package br.com.trezzor.dao;

import br.com.trezzor.util.ConexaoBD; // Importa nossa classe de conexão
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Verifica se um usuário e senha existem no banco.
     * Retorna true se o login for válido, false caso contrário.
     */
    public boolean validarLogin(String email, String senha) throws SQLException {
        // ATENÇÃO: Ver observação de segurança abaixo!
        String sql = "SELECT COUNT(UsuarioID) FROM Usuarios WHERE Email = ? AND Senha = ?";
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, senha); // Compara a senha em texto plano
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Se o COUNT for maior que 0, o usuário existe
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false; // Não encontrou
    }

    /**
     * Insere um novo usuário no banco de dados.
     * O TipoUsuario é definido como 'Cliente' por padrão.
     */
    public void cadastrar(String nome, String email, String senha) throws SQLException {
        String sql = "INSERT INTO Usuarios (Nome, Email, Senha, TipoUsuario) VALUES (?, ?, ?, 'Cliente')";
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            
            stmt.execute();
        }
    }
}