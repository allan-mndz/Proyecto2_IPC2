package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PerfilDAO {

    public boolean guardarPerfilCliente(int idUsuario, String descripcion, String sector, String sitioWeb) {
        boolean existe = false;
        String sqlCheck = "SELECT id_cliente FROM Cliente WHERE id_usuario = ?";
        String sqlInsert = "INSERT INTO Cliente (id_usuario, saldo, descripcion_empresa, sector, sitio_web) VALUES (?, 0.00, ?, ?, ?)";
        String sqlUpdate = "UPDATE Cliente SET descripcion_empresa = ?, sector = ?, sitio_web = ? WHERE id_usuario = ?";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) existe = true;
            }

            if (!existe) {
                try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, descripcion);
                    ps.setString(3, sector);
                    ps.setString(4, sitioWeb);
                    return ps.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                    ps.setString(1, descripcion);
                    ps.setString(2, sector);
                    ps.setString(3, sitioWeb);
                    ps.setInt(4, idUsuario);
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR SQL EN CLIENTE");
            e.printStackTrace();
            return false;
        }
    }

    public boolean guardarPerfilFreelancer(int idUsuario, String biografia, String nivel, double tarifa) {
        boolean existe = false;
        String sqlCheck = "SELECT id_freelancer FROM Freelancer WHERE id_usuario = ?";
        String sqlInsert = "INSERT INTO Freelancer (id_usuario, saldo, biografia, nivel_experiencia, tarifa_hora, calificacion_promedio) VALUES (?, 0.00, ?, ?, ?, 0.00)";
        String sqlUpdate = "UPDATE Freelancer SET biografia = ?, nivel_experiencia = ?, tarifa_hora = ? WHERE id_usuario = ?";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) existe = true;
            }

            if (!existe) {
                try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, biografia);
                    ps.setString(3, nivel);
                    ps.setDouble(4, tarifa);
                    return ps.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                    ps.setString(1, biografia);
                    ps.setString(2, nivel);
                    ps.setDouble(3, tarifa);
                    ps.setInt(4, idUsuario);
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR SQL EN FREELANCER");
            e.printStackTrace();
            return false;
        }
    }
}