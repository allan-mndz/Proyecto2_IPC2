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

public class AdminHabilidadesDAO {

    public List<Map<String, Object>> obtenerHabilidades() {
        List<Map<String, Object>> lista = new ArrayList<>();
        // Hacemos un JOIN para traer también el nombre de la categoría y mostrarlo en la tabla
        String sql = "SELECT h.id_habilidad, h.id_categoria, c.nombre AS nombre_categoria, " +
                "h.nombre, h.descripcion, h.estado " +
                "FROM Habilidad h " +
                "JOIN Categoria c ON h.id_categoria = c.id_categoria " +
                "ORDER BY h.id_habilidad DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> hab = new HashMap<>();
                hab.put("idHabilidad", rs.getInt("id_habilidad"));
                hab.put("idCategoria", rs.getInt("id_categoria"));
                hab.put("nombreCategoria", rs.getString("nombre_categoria"));
                hab.put("nombre", rs.getString("nombre"));
                hab.put("descripcion", rs.getString("descripcion"));

                int estado = 1;
                try { estado = rs.getInt("estado"); } catch (Exception e) {}
                hab.put("estado", estado);
                lista.add(hab);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener habilidades: " + e.getMessage());
        }
        return lista;
    }

    public boolean crearHabilidad(int idCategoria, String nombre, String descripcion) {
        String sql = "INSERT INTO Habilidad (id_categoria, nombre, descripcion, estado) VALUES (?, ?, ?, 1)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean editarHabilidad(int idHabilidad, int idCategoria, String nombre, String descripcion) {
        String sql = "UPDATE Habilidad SET id_categoria = ?, nombre = ?, descripcion = ? WHERE id_habilidad = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setInt(4, idHabilidad);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean cambiarEstado(int idHabilidad, int nuevoEstado) {
        String sql = "UPDATE Habilidad SET estado = ? WHERE id_habilidad = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idHabilidad);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}