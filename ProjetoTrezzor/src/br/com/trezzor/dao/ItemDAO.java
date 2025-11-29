package br.com.trezzor.dao;

import br.com.trezzor.model.Item;
import br.com.trezzor.util.ConexaoBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public void salvar(Item item) throws SQLException {
        String sql = "INSERT INTO Itens (Nome, Categoria, ValorEstimado) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, item.getNome());
            stmt.setString(2, item.getCategoria());
            stmt.setDouble(3, item.getValorEstimado());
            stmt.execute();
        }
    }

    public List<Item> listar() throws SQLException {
        List<Item> lista = new ArrayList<>();
        String sql = "SELECT ItemID, Nome, Categoria, ValorEstimado FROM Itens";
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("ItemID"));
                item.setNome(rs.getString("Nome"));
                item.setCategoria(rs.getString("Categoria"));
                item.setValorEstimado(rs.getDouble("ValorEstimado"));
                lista.add(item);
            }
        }
        return lista;
    }
    // Método para deletar um item pelo ID
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM Itens WHERE ItemID = ?";
        
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.execute();
        }
    }
}