package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminComisionesDAO {

    public List<Map<String, Object>> obtenerHistorial() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT id_comision, porcentaje, fecha_inicio, fecha_fin FROM Historial_Comision ORDER BY fecha_inicio DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("idComision", rs.getInt("id_comision"));
                fila.put("porcentaje", rs.getDouble("porcentaje"));
                fila.put("fechaInicio", rs.getString("fecha_inicio"));
                fila.put("fechaFin", rs.getString("fecha_fin"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener comisiones: " + e.getMessage());
        }
        return lista;
    }

    public boolean cambiarComision(double nuevoPorcentaje) {
        String sqlCerrarActual = "UPDATE Historial_Comision SET fecha_fin = NOW() WHERE fecha_fin IS NULL";
        String sqlNueva = "INSERT INTO Historial_Comision (porcentaje, fecha_inicio) VALUES (?, NOW())";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Transacción segura

            try {
                // 1. Cerrar la comisión vigente
                try (PreparedStatement ps1 = conn.prepareStatement(sqlCerrarActual)) {
                    ps1.executeUpdate();
                }

                // 2. Insertar la nueva comisión
                try (PreparedStatement ps2 = conn.prepareStatement(sqlNueva)) {
                    ps2.setDouble(1, nuevoPorcentaje);
                    ps2.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}