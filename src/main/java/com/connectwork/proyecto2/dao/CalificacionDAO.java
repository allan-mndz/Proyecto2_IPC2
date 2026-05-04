package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalificacionDAO {
    public boolean registrarCalificacion(int idContrato, int estrellas, String comentario) {
        String sql = "INSERT INTO Calificacion (id_contrato, estrellas, comentario) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idContrato);
            ps.setInt(2, estrellas);
            ps.setString(3, comentario);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar calificación: " + e.getMessage());
            return false;
        }
    }
}
