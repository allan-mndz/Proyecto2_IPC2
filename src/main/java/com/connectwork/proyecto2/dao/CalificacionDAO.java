package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalificacionDAO {
    public boolean registrarCalificacion(int idContrato, int estrellas, String comentario) {
        String sqlInsert = "INSERT INTO Calificacion (id_contrato, estrellas, comentario) VALUES (?, ?, ?)";
        String sqlGetFreelancer = "SELECT p.id_freelancer FROM Contrato c " +
                "JOIN Propuesta p ON c.id_propuesta = p.id_propuesta " +
                "WHERE c.id_contrato = ?";

        String sqlUpdatePromedio = "UPDATE Freelancer SET calificacion_promedio = (" +
                "   SELECT AVG(ca.estrellas) " +
                "   FROM Calificacion ca " +
                "   JOIN Contrato co ON ca.id_contrato = co.id_contrato " +
                "   JOIN Propuesta pr ON co.id_propuesta = pr.id_propuesta " +
                "   WHERE pr.id_freelancer = ?" +
                ") WHERE id_freelancer = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Iniciamos transacción segura

            try {
                // Guardamos las estrellas y el comentario
                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                    psInsert.setInt(1, idContrato);
                    psInsert.setInt(2, estrellas);
                    psInsert.setString(3, comentario);
                    psInsert.executeUpdate();
                }

                // Buscamos el ID del freelancer
                int idFreelancer = 0;
                try (PreparedStatement psGet = conn.prepareStatement(sqlGetFreelancer)) {
                    psGet.setInt(1, idContrato);
                    try (ResultSet rs = psGet.executeQuery()) {
                        if (rs.next()) {
                            idFreelancer = rs.getInt("id_freelancer");
                        }
                    }
                }

                // Si lo encontramos, actualizamos su promedio general
                if (idFreelancer > 0) {
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdatePromedio)) {
                        psUpdate.setInt(1, idFreelancer); // Para el WHERE interno del SELECT
                        psUpdate.setInt(2, idFreelancer); // Para el WHERE externo del UPDATE
                        psUpdate.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Error en transacción de calificación: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión al calificar: " + e.getMessage());
            return false;
        }
    }
}
