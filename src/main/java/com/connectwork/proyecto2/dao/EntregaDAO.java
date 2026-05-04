package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.models.Entrega;
import com.connectwork.proyecto2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntregaDAO {

    public boolean registrarEntrega(int idContrato, int idProyecto, String descripcion, String archivosUrl) {
        String sqlEntrega = "INSERT INTO Entrega (id_contrato, descripcion, archivos_url, fecha_subida) VALUES (?, ?, ?, NOW())";
        String sqlProyecto = "UPDATE Proyecto SET estado = 'ENTREGA_PENDIENTE' WHERE id_proyecto = ?";

        try(Connection conn = DBConnection.getConnection()){
            conn.setAutoCommit(false);

            try(PreparedStatement psEntrega= conn.prepareStatement(sqlEntrega);
            PreparedStatement psProyecto = conn.prepareStatement(sqlProyecto)) {

                // Insertamos la entrega
                psEntrega.setInt(1, idContrato);
                psEntrega.setString(2, descripcion);
                psEntrega.setString(3, archivosUrl);
                psEntrega.executeUpdate();

                // Actualizamos el estado del proyecto
                psProyecto.setInt(1, idProyecto);
                psProyecto.executeUpdate();

                // guardamos los cambios
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Error en la transacción de entrega: " + ex.getMessage());
                return false;
            }
        }catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }

    public Entrega obtenerEntregaPorProyecto(int idProyecto){
        Entrega entrega = null;

        String sql = "SELECT e.* FROM Entrega e " + "JOIN Contrato c ON e.id_contrato = c.id_contrato " + "JOIN Propuesta p ON c.id_propuesta = p.id_propuesta " + "WHERE p.id_proyecto = ? ORDER BY e.fecha_subida DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProyecto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    entrega = new Entrega();
                    entrega.setIdEntrega(rs.getInt("id_entrega"));
                    entrega.setIdContrato(rs.getInt("id_contrato"));
                    entrega.setDescripcion(rs.getString("descripcion"));
                    entrega.setArchivosUrl(rs.getString("archivos_url"));
                    entrega.setFechaSubida(rs.getString("fecha_subida"));
                    entrega.setEstado(rs.getString("estado"));
                    entrega.setMotivoRechazo(rs.getString("motivo_rechazo"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener entrega: " + ex.getMessage());
        }
        return entrega;
    }

    public boolean evaluarEntrega(int idEntrega, int idProyecto, int idContrato, boolean esAceptada, String motivoRechazo) {
        // Consultas si ACEPTA
        String sqlEntregaAceptar = "UPDATE Entrega SET estado = 'APROBADA' WHERE id_entrega = ?";
        String sqlProyectoFinalizar = "UPDATE Proyecto SET estado = 'COMPLETADO' WHERE id_proyecto = ?";
        String sqlContratoFinalizar = "UPDATE Contrato SET estado = 'FINALIZADO' WHERE id_contrato = ?";

        // Consultas si RECHAZA
        String sqlEntregaRechazar = "UPDATE Entrega SET estado = 'RECHAZADA', motivo_rechazo = ? WHERE id_entrega = ?";
        String sqlProyectoRegresar = "UPDATE Proyecto SET estado = 'EN_PROGRESO' WHERE id_proyecto = ?";

        // Traer el dinero bloqueado
        String sqlDatosDinero = "SELECT c.monto_bloqueado, p.id_freelancer, " +
                "(SELECT porcentaje FROM Historial_Comision ORDER BY fecha_inicio DESC LIMIT 1) AS porcentaje_comision " +
                "FROM Contrato c " +
                "JOIN Propuesta p ON c.id_propuesta = p.id_propuesta " +
                "WHERE c.id_contrato = ?";

        String sqlPagarFreelancer = "UPDATE Freelancer SET saldo = saldo + ? WHERE id_freelancer = ?";
        String sqlPagarPlataforma = "UPDATE Plataforma SET saldo_global = saldo_global + ? WHERE id_plataforma = 1";


        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Iniciamos transacción segura

            try {
                if (esAceptada) {
                    try (PreparedStatement ps1 = conn.prepareStatement(sqlEntregaAceptar);
                         PreparedStatement ps2 = conn.prepareStatement(sqlProyectoFinalizar);
                         PreparedStatement ps3 = conn.prepareStatement(sqlContratoFinalizar)) {
                        ps1.setInt(1, idEntrega); ps1.executeUpdate();
                        ps2.setInt(1, idProyecto); ps2.executeUpdate();
                        ps3.setInt(1, idContrato); ps3.executeUpdate();
                    }

                    double montoBloqueado = 0;
                    int idFreelancer = 0;
                    double porcentajeComision = 10.0;

                    // Obtenemos los datos del contrato
                    try (PreparedStatement psDatos = conn.prepareStatement(sqlDatosDinero)) {
                        psDatos.setInt(1, idContrato);
                        try (ResultSet rs = psDatos.executeQuery()) {
                            if (rs.next()) {
                                montoBloqueado = rs.getDouble("monto_bloqueado");
                                idFreelancer = rs.getInt("id_freelancer");

                                // Intentamos leer el porcentaje de la base de datos
                                double comisionBD = rs.getDouble("porcentaje_comision");
                                if (!rs.wasNull()) {
                                    porcentajeComision = comisionBD;
                                }
                            }
                        }
                    }

                    // Calculamos la parte de cada quien
                    double gananciaPlataforma = montoBloqueado * (porcentajeComision / 100.0);
                    double pagoFreelancer = montoBloqueado - gananciaPlataforma;

                    // Pagarle al Freelancer
                    try (PreparedStatement psPagoF = conn.prepareStatement(sqlPagarFreelancer)) {
                        psPagoF.setDouble(1, pagoFreelancer);
                        psPagoF.setInt(2, idFreelancer);
                        psPagoF.executeUpdate();
                    }

                    // Pagarle a la Plataforma
                    try (PreparedStatement psPagoP = conn.prepareStatement(sqlPagarPlataforma)) {
                        psPagoP.setDouble(1, gananciaPlataforma);
                        psPagoP.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps1 = conn.prepareStatement(sqlEntregaRechazar);
                         PreparedStatement ps2 = conn.prepareStatement(sqlProyectoRegresar)) {
                        ps1.setString(1, motivoRechazo);
                        ps1.setInt(2, idEntrega);
                        ps1.executeUpdate();
                        ps2.setInt(1, idProyecto);
                        ps2.executeUpdate();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Error ejecutando decisión: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
