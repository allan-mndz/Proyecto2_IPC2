package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.util.DBConnection;
import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BilleteraDAO {

    public double obtenerSaldo(int idUsuario, String rol){
        double saldo = 0.0;
        String tabla = rol.equalsIgnoreCase("CLIENTE") ? "Cliente" : "Freelancer";
        String sql = "SELECT saldo FROM " + tabla + " WHERE id_usuario = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    saldo = rs.getDouble("saldo");
                }
            }
        }catch (SQLException e){
            System.out.println("Error al obtener saldo: " + e.getMessage());
        }
        return saldo;
    }

    public boolean recargarSaldo(int idUsuario, double monto){
        String updateSaldo = "UPDATE Cliente SET saldo = saldo + ? WHERE id_usuario = ?";
        String insertRecarga = "INSERT INTO Recarga (id_cliente, monto, fecha_hora) " + "SELECT id_cliente, ?, NOW() FROM Cliente WHERE id_usuario = ?";

        try (Connection conn = DBConnection.getConnection()){
            conn.setAutoCommit(false);

            try {
                // Sumamos el dinero al cliente
                try (PreparedStatement psUpdate = conn.prepareStatement(updateSaldo)) {
                    psUpdate.setDouble(1, monto);
                    psUpdate.setInt(2, idUsuario);
                    psUpdate.executeUpdate();
                }

                // Dejamos el registro para el historial usando el insert inteligente
                try (PreparedStatement psInsert = conn.prepareStatement(insertRecarga)) {
                    psInsert.setDouble(1, monto);
                    psInsert.setInt(2, idUsuario);
                    psInsert.executeUpdate();
                }

                conn.commit(); // Confirmamos cambios
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Error en transacción de recarga: " + ex.getMessage());
                return false;
            }
        }catch (SQLException e){
            System.out.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }
}
