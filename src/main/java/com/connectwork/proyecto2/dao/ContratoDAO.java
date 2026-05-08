package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.models.Contrato;
import com.connectwork.proyecto2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO {

    public boolean generarContrato(int idPropuesta, int idProyecto, double monto){
        String sqlInsertarContrato = "INSERT INTO Contrato (id_propuesta, monto_bloqueado, estado) VALUES (?, ?, 'ACTIVO')";
        String sqlActualizarProyecto = "UPDATE Proyecto SET estado = 'EN_PROGRESO' WHERE id_proyecto = ?";
        String sqlRestarSaldo = "UPDATE Cliente SET saldo = saldo - ? WHERE id_cliente = (SELECT id_cliente FROM Proyecto WHERE id_proyecto = ?)";

        try(Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try(PreparedStatement psContrato = conn.prepareStatement(sqlInsertarContrato);
                PreparedStatement psProyecto = conn.prepareStatement(sqlActualizarProyecto);
                PreparedStatement psSaldo = conn.prepareStatement(sqlRestarSaldo)) {

                // Creamos el contrato
                psContrato.setInt(1, idPropuesta);
                psContrato.setDouble(2, monto);
                psContrato.executeUpdate();

                // Cambiamos el estado del proyecto
                psProyecto.setInt(1, idProyecto);
                psProyecto.executeUpdate();

                // RESTAMOS EL SALDO
                psSaldo.setDouble(1, monto);
                psSaldo.setInt(2, idProyecto);
                psSaldo.executeUpdate();

                // guardamos los cambios
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Error en la transacción del contrato: " + ex.getMessage());
                return false;
            }

        }catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }

    public List<Contrato> obtenerContratosPorFreelancer(int idUsuario){
        List<Contrato> lista = new ArrayList<>();

        String sql = "SELECT c.id_contrato, c.id_propuesta, c.monto_bloqueado, c.estado AS estado_contrato, " +
                "pr.id_proyecto, pr.titulo, pr.descripcion, pr.fecha_limite " +
                "FROM Contrato c " +
                "JOIN Propuesta p ON c.id_propuesta = p.id_propuesta " +
                "JOIN Proyecto pr ON p.id_proyecto = pr.id_proyecto " +
                "JOIN Freelancer f ON p.id_freelancer = f.id_freelancer " +
                "WHERE f.id_usuario = ? AND c.estado = 'ACTIVO'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Contrato contrato = new Contrato();
                contrato.setIdContrato(rs.getInt("id_contrato"));
                contrato.setIdPropuesta(rs.getInt("id_propuesta"));
                contrato.setMontoBloqueado(rs.getDouble("monto_bloqueado"));
                contrato.setEstado(rs.getString("estado_contrato"));

                // Llenamos los datos del proyecto
                contrato.setIdProyecto(rs.getInt("id_proyecto"));
                contrato.setTituloProyecto(rs.getString("titulo"));
                contrato.setDescripcionProyecto(rs.getString("descripcion"));
                contrato.setFechaLimite(rs.getString("fecha_limite"));

                lista.add(contrato);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar contratos del freelancer: " + e.getMessage());
        }
        return lista;
    }
}
