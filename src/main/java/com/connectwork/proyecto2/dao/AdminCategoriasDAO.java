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

public class AdminCategoriasDAO {

    public List<Map<String, Object>> obtenerCategorias() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion, estado FROM Categoria ORDER BY id_categoria DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> cat = new HashMap<>();
                cat.put("idCategoria", rs.getInt("id_categoria"));
                cat.put("nombre", rs.getString("nombre"));
                cat.put("descripcion", rs.getString("descripcion"));

                int estado = 1;
                try { estado = rs.getInt("estado"); } catch (Exception e) {}
                cat.put("estado", estado);
                lista.add(cat);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener categorías: " + e.getMessage());
        }
        return lista;
    }

    public boolean crearCategoria(String nombre, String descripcion) {
        String sql = "INSERT INTO Categoria (nombre, descripcion, estado) VALUES (?, ?, 1)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean editarCategoria(int idCategoria, String nuevoNombre, String nuevaDescripcion) {
        String sql = "UPDATE Categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setString(2, nuevaDescripcion);
            ps.setInt(3, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean cambiarEstado(int idCategoria, int nuevoEstado) {
        String sql = "UPDATE Categoria SET estado = ? WHERE id_categoria = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}