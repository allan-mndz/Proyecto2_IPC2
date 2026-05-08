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

public class AdminReportesDAO {

    // 1. Historial de Comisiones
    public List<Map<String, Object>> obtenerHistorialComisiones() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT porcentaje, fecha_inicio, fecha_fin FROM Historial_Comision ORDER BY fecha_inicio DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("porcentaje", rs.getDouble("porcentaje"));
                fila.put("fecha_inicio", rs.getString("fecha_inicio"));
                fila.put("fecha_fin", rs.getString("fecha_fin"));
                lista.add(fila);
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return lista;
    }

    // 2. Top 5 Freelancers
    public List<Map<String, Object>> obtenerTopFreelancers(String f1, String f2) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT u.nombre_completo AS freelancer, COUNT(c.id_contrato) AS contratos, " +
                "SUM(c.monto_bloqueado) AS total_generado " +
                "FROM Contrato c " +
                "JOIN Propuesta p ON c.id_propuesta = p.id_propuesta " +
                "JOIN Freelancer f ON p.id_freelancer = f.id_freelancer " +
                "JOIN Usuario u ON f.id_usuario = u.id_usuario " +
                "JOIN Proyecto pro ON p.id_proyecto = pro.id_proyecto " +
                "WHERE c.estado = 'FINALIZADO' AND pro.fecha_limite BETWEEN ? AND ? " +
                "GROUP BY f.id_freelancer " +
                "ORDER BY total_generado DESC LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f1); ps.setString(2, f2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("nombre", rs.getString("freelancer"));
                fila.put("contratos", rs.getInt("contratos"));
                fila.put("total", rs.getDouble("total_generado"));
                lista.add(fila);
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return lista;
    }

    // 3. Top 5 Categorías
    public List<Map<String, Object>> obtenerTopCategorias(String f1, String f2) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT cat.nombre, COUNT(c.id_contrato) AS contratos " +
                "FROM Contrato c " +
                "JOIN Propuesta pr ON c.id_propuesta = pr.id_propuesta " +
                "JOIN Proyecto p ON pr.id_proyecto = p.id_proyecto " +
                "JOIN Categoria cat ON p.id_categoria = cat.id_categoria " +
                "WHERE c.estado = 'FINALIZADO' AND p.fecha_limite BETWEEN ? AND ? " +
                "GROUP BY cat.id_categoria " +
                "ORDER BY contratos DESC LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f1); ps.setString(2, f2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("nombre", rs.getString("nombre"));
                fila.put("contratos", rs.getInt("contratos"));
                lista.add(fila);
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return lista;
    }

    // 4. Total Ingresos Plataforma
    public Map<String, Object> obtenerIngresosTotales(String f1, String f2) {
        Map<String, Object> res = new HashMap<>();
        String sql = "SELECT COUNT(c.id_contrato) AS contratos, SUM(c.monto_bloqueado * 0.1) AS comisiones " +
                "FROM Contrato c " +
                "JOIN Propuesta pr ON c.id_propuesta = pr.id_propuesta " +
                "JOIN Proyecto p ON pr.id_proyecto = p.id_proyecto " +
                "WHERE c.estado = 'FINALIZADO' AND p.fecha_limite BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f1); ps.setString(2, f2);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                res.put("contratos", rs.getInt("contratos"));
                res.put("total", rs.getDouble("comisiones"));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return res;
    }
}