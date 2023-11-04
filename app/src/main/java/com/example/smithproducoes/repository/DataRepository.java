package com.example.smithproducoes.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataRepository {
    public static void insertData(String jsonData) {
        String insertSQL = "INSERT INTO jsonData (json) VALUES (?)";

        try (Connection conn = SQLiteManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, jsonData);
            pstmt.executeUpdate();
            System.out.println("Dados inseridos no banco de dados.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static String retrieveData() {
        String selectSQL = "SELECT json FROM jsonData ORDER BY id DESC LIMIT 1";

        try (Connection conn = SQLiteManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet resultSet = pstmt.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getString("json");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public static void updateData(String jsonData) {
        String updateSQL = "UPDATE jsonData SET json = ? WHERE id = (SELECT MAX(id) FROM jsonData)";

        try (Connection conn = SQLiteManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, jsonData);
            pstmt.executeUpdate();
            System.out.println("Dados atualizados no banco de dados.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
