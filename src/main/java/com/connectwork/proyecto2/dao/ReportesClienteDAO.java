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

public class ReportesClienteDAO {

    // 1. Historial de Recargas
    public List<Map<String, Object>> obtenerHistorialRecargas(int idUsuario) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT r.monto, r.fecha_hora " +
                "FROM Recarga r " +
                "JOIN Cliente c ON r.id_cliente = c.id_cliente " +
                "WHERE c.id_usuario = ? " +
                "ORDER BY r.fecha_hora DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("monto", rs.getDouble("monto"));
                    fila.put("fecha_hora", rs.getString("fecha_hora"));
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en reporte de recargas: " + e.getMessage());
        }
        return lista;
    }

    // 2. Historial de Proyectos
    public List<Map<String, Object>> obtenerHistorialProyectos(int idUsuario, String fechaInicio, String fechaFin) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT p.titulo, p.estado, p.presupuesto_maximo, c.monto_bloqueado, u.nombre_completo AS freelancer " +
                "FROM Proyecto p " +
                "JOIN Cliente cl ON p.id_cliente = cl.id_cliente " +
                "LEFT JOIN Propuesta pr ON p.id_proyecto = pr.id_proyecto AND pr.estado = 'ACEPTADA' " +
                "LEFT JOIN Contrato c ON pr.id_propuesta = c.id_propuesta " +
                "LEFT JOIN Freelancer f ON pr.id_freelancer = f.id_freelancer " +
                "LEFT JOIN Usuario u ON f.id_usuario = u.id_usuario " +
                "WHERE cl.id_usuario = ? AND p.fecha_limite BETWEEN ? AND ? " +
                "ORDER BY p.fecha_limite DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("titulo", rs.getString("titulo"));
                    fila.put("estado", rs.getString("estado"));
                    fila.put("monto", rs.getDouble("monto_bloqueado") > 0 ? rs.getDouble("monto_bloqueado") : rs.getDouble("presupuesto_maximo"));

                    String nomFreelancer = rs.getString("freelancer");
                    fila.put("freelancer", nomFreelancer != null ? nomFreelancer : "Sin asignar");
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en reporte de proyectos: " + e.getMessage());
        }
        return lista;
    }

    // 3. Gasto por categoría
    public List<Map<String, Object>> obtenerGastoPorCategoria(int idUsuario, String fechaInicio, String fechaFin) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT cat.nombre AS categoria, SUM(c.monto_bloqueado) AS total_gastado " +
                "FROM Categoria cat " +
                "JOIN Proyecto p ON cat.id_categoria = p.id_categoria " +
                "JOIN Cliente cl ON p.id_cliente = cl.id_cliente " +
                "JOIN Propuesta pr ON p.id_proyecto = pr.id_proyecto " +
                "JOIN Contrato c ON pr.id_propuesta = c.id_propuesta " +
                "WHERE cl.id_usuario = ? AND p.fecha_limite BETWEEN ? AND ? " +
                "GROUP BY cat.id_categoria, cat.nombre";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    // Llaves estrictamente en minúsculas para Angular
                    fila.put("categoria", rs.getString("categoria"));
                    fila.put("total", rs.getDouble("total_gastado"));
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en reporte de gastos: " + e.getMessage());
        }
        return lista;
    }
}
