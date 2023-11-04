package com.example.smithproducoes.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:mydatabase.db"; // Nome do arquivo do banco de dados
            conn = DriverManager.getConnection(url);
            System.out.println("Conexão com o banco de dados estabelecida.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return conn;
    }

    public static void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS jsonData (id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT)";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Tabela criada com sucesso.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
