package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.models.Propuesta;
import com.connectwork.proyecto2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PropuestaDAO {

    public boolean registrarPropuesta(Propuesta p) {
        String sql = "INSERT INTO Propuesta (id_proyecto, id_freelancer, monto_ofertado, plazo_dias, carta_presentacion) " +
                "VALUES (?, (SELECT id_freelancer FROM Freelancer WHERE id_usuario = ? LIMIT 1), ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getIdProyecto());
            ps.setInt(2, p.getIdFreelancer());
            ps.setDouble(3, p.getMontoOfertado());
            ps.setInt(4, p.getPlazoDias());
            ps.setString(5, p.getCartaPresentacion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(">>> ERROR SQL AL GUARDAR PROPUESTA: " + e.getMessage());
            return false;
        }
    }

    public List<Propuesta> obtenerPropuestasPorProyecto(int idProyecto) {
        List<Propuesta> lista = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre_completo, f.calificacion_promedio " +
                "FROM Propuesta p " +
                "JOIN Freelancer f ON p.id_freelancer = f.id_freelancer " +
                "JOIN Usuario u ON f.id_usuario = u.id_usuario " +
                "WHERE p.id_proyecto = ?";

        try(Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProyecto);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Propuesta p = new Propuesta();
                p.setIdPropuesta(rs.getInt("id_propuesta"));
                p.setIdProyecto(rs.getInt("id_proyecto"));
                p.setIdFreelancer(rs.getInt("id_freelancer"));
                p.setMontoOfertado(rs.getDouble("monto_ofertado"));
                p.setPlazoDias(rs.getInt("plazo_dias"));
                p.setCartaPresentacion(rs.getString("carta_presentacion"));
                p.setEstado(rs.getString("estado"));

                p.setNombreFreelancer(rs.getString("nombre_completo"));
                p.setCalificacionFreelancer(rs.getDouble("calificacion_promedio"));

                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println(">>> ERROR SQL AL OBTENER PROPUESTAS: " + e.getMessage());
        }
        return lista;
    }

    public boolean retirarPropuesta(int idPropuesta) {
        String sql = "DELETE FROM Propuesta WHERE id_propuesta = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPropuesta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al retirar propuesta: " + e.getMessage());
            return false;
        }
    }
}
