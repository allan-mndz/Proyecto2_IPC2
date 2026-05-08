package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.models.Usuario;
import com.connectwork.proyecto2.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario validarLogin(String username, String password) {
        Usuario usuarioValido = null;
        String sql = "SELECT id_usuario, tipo_usuario, username, nombre_completo, password FROM Usuario WHERE username = ? AND estado = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passwordHash = rs.getString("password");

                    if (BCrypt.checkpw(password, passwordHash)) {
                        usuarioValido = new Usuario();
                        usuarioValido.setIdUsuario(rs.getInt("id_usuario"));
                        usuarioValido.setTipoUsuario(rs.getString("tipo_usuario"));
                        usuarioValido.setUsername(rs.getString("username"));
                        usuarioValido.setNombreCompleto(rs.getString("nombre_completo"));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al validar login: " + e.getMessage());
        }

        return usuarioValido;
    }

    public boolean registrarUsuario(Usuario nuevoUsuario) {
        String sqlUsuario = "INSERT INTO Usuario (tipo_usuario, username, password, nombre_completo, correo, telefono, direccion, cui, fecha_nacimiento, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        String sqlCliente = "INSERT INTO Cliente (id_usuario, saldo) VALUES (?, 0.00)";
        String sqlFreelancer = "INSERT INTO Freelancer (id_usuario, saldo) VALUES (?, 0.00)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                psUsuario.setString(1, nuevoUsuario.getTipoUsuario());
                psUsuario.setString(2, nuevoUsuario.getUsername());

                String hashPassword = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
                psUsuario.setString(3, hashPassword);

                psUsuario.setString(4, nuevoUsuario.getNombreCompleto());
                psUsuario.setString(5, nuevoUsuario.getCorreo());
                psUsuario.setString(6, nuevoUsuario.getTelefono());
                psUsuario.setString(7, nuevoUsuario.getDireccion());
                psUsuario.setString(8, nuevoUsuario.getCui());
                psUsuario.setString(9, nuevoUsuario.getFechaNacimiento());

                int filasAfectadas = psUsuario.executeUpdate();

                if (filasAfectadas > 0) {
                    try (ResultSet rs = psUsuario.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idGenerado = rs.getInt(1);

                            if ("CLIENTE".equalsIgnoreCase(nuevoUsuario.getTipoUsuario())) {
                                try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente)) {
                                    psCliente.setInt(1, idGenerado);
                                    psCliente.executeUpdate();
                                }
                            } else if ("FREELANCER".equalsIgnoreCase(nuevoUsuario.getTipoUsuario())) {
                                try (PreparedStatement psFreelancer = conn.prepareStatement(sqlFreelancer)) {
                                    psFreelancer.setInt(1, idGenerado);
                                    psFreelancer.executeUpdate();
                                }
                            }
                        }
                    }
                } else {
                    conn.rollback();
                    return false;
                }
                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error en la transacción de registro: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error de conexión al registrar: " + e.getMessage());
            return false;
        }
    }

    public boolean tienePerfilCompleto(int idUsuario, String tipoUsuario) {
        String sql = "";

        if ("CLIENTE".equalsIgnoreCase(tipoUsuario)) {
            sql = "SELECT id_cliente FROM Cliente WHERE id_usuario = ? AND sector IS NOT NULL";
        } else if ("FREELANCER".equalsIgnoreCase(tipoUsuario)) {
            sql = "SELECT id_freelancer FROM Freelancer WHERE id_usuario = ? AND biografia IS NOT NULL";
        } else {
            return true;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
