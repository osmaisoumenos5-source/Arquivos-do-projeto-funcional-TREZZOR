package br.com.trezzor.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    
    // IMPORTANTE: Verifique se sua senha do SQL Server é essa mesma
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=MercadoTrezzor;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa"; // Geralmente é 'sa'
    private static final String PASS = "123456"; // A senha que você definiu na instalação

   public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }  }
