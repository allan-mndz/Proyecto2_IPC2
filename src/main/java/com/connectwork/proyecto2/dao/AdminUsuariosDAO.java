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

public class AdminUsuariosDAO {

    public List<Map<String, Object>> obtenerUsuarios() {
        List<Map<String, Object>> lista = new ArrayList<>();
        // Traemos a todos los que no sean administradores
        String sql = "SELECT id_usuario, username, nombre_completo, tipo_usuario, estado FROM Usuario WHERE tipo_usuario IN ('CLIENTE', 'FREELANCER')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> u = new HashMap<>();
                u.put("idUsuario", rs.getInt("id_usuario"));
                u.put("username", rs.getString("username"));
                u.put("nombreCompleto", rs.getString("nombre_completo"));
                u.put("tipoUsuario", rs.getString("tipo_usuario"));
                u.put("estado", rs.getString("estado"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }
        return lista;
    }

    public boolean cambiarEstado(int idUsuario, String nuevoEstado) {
        String sql = "UPDATE Usuario SET estado = ? WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }
}