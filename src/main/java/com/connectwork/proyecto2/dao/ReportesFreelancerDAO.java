package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportesFreelancerDAO {

    // OBTENER SALDO ACTUAL
    public Map<String, Double> obtenerSaldo(int idUsuario) {
        Map<String, Double> map = new HashMap<>();
        String sql = "SELECT saldo FROM Freelancer WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                map.put("saldo", rs.getDouble("saldo"));
            } else {
                map.put("saldo", 0.0);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    // HISTORIAL DE CONTRATOS COMPLETADOS
    public List<Map<String, Object>> obtenerHistorialContratos(int idUsuario, String fechaInicio, String fechaFin) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT u.nombre_completo AS cliente, p.titulo AS proyecto, " +
                "(pr.monto_ofertado - (pr.monto_ofertado * ((SELECT porcentaje FROM Historial_Comision ORDER BY id_comision DESC LIMIT 1) / 100))) AS monto_recibido, " +
                "cal.estrellas AS calificacion " +
                "FROM Contrato c " +
                "JOIN Propuesta pr ON c.id_propuesta = pr.id_propuesta " +
                "JOIN Freelancer f ON pr.id_freelancer = f.id_freelancer " +
                "JOIN Proyecto p ON pr.id_proyecto = p.id_proyecto " +
                "JOIN Cliente cli ON p.id_cliente = cli.id_cliente " +
                "JOIN Usuario u ON cli.id_usuario = u.id_usuario " +
                "LEFT JOIN Calificacion cal ON c.id_contrato = cal.id_contrato " +
                "WHERE f.id_usuario = ? AND c.estado = 'FINALIZADO' " +
                "AND p.fecha_limite BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("cliente", rs.getString("cliente"));
                fila.put("proyecto", rs.getString("proyecto"));
                fila.put("monto_recibido", rs.getDouble("monto_recibido"));
                Object estrellas = rs.getObject("calificacion");
                fila.put("calificacion", estrellas != null ? estrellas : "Sin calificar");
                lista.add(fila);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // TOP 5 CATEGORIAS TRABAJADAS
    public List<Map<String, Object>> obtenerTopCategorias(int idUsuario) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT cat.nombre AS categoria, COUNT(c.id_contrato) AS cantidad_contratos, " +
                "SUM(pr.monto_ofertado - (pr.monto_ofertado * ((SELECT porcentaje FROM Historial_Comision ORDER BY id_comision DESC LIMIT 1)/100))) AS total_ingresos " +
                "FROM Contrato c " +
                "JOIN Propuesta pr ON c.id_propuesta = pr.id_propuesta " +
                "JOIN Freelancer f ON pr.id_freelancer = f.id_freelancer " +
                "JOIN Proyecto p ON pr.id_proyecto = p.id_proyecto " +
                "JOIN Categoria cat ON p.id_categoria = cat.id_categoria " +
                "WHERE f.id_usuario = ? AND c.estado = 'FINALIZADO' " +
                "GROUP BY cat.id_categoria, cat.nombre " +
                "ORDER BY total_ingresos DESC LIMIT 5";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("categoria", rs.getString("categoria"));
                fila.put("cantidad_contratos", rs.getInt("cantidad_contratos"));
                fila.put("total_ingresos", rs.getDouble("total_ingresos"));
                lista.add(fila);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // PROPUESTAS ENVIADAS
    public List<Map<String, Object>> obtenerPropuestas(int idUsuario, String fechaInicio, String fechaFin) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT p.titulo AS proyecto, pr.monto_ofertado, pr.estado " +
                "FROM Propuesta pr " +
                "JOIN Freelancer f ON pr.id_freelancer = f.id_freelancer " +
                "JOIN Proyecto p ON pr.id_proyecto = p.id_proyecto " +
                "WHERE f.id_usuario = ? AND p.fecha_limite BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("proyecto", rs.getString("proyecto"));
                fila.put("monto_ofertado", rs.getDouble("monto_ofertado"));
                fila.put("estado", rs.getString("estado"));
                lista.add(fila);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}