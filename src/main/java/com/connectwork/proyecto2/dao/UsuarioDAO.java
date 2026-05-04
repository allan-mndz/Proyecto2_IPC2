package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.models.Usuario;
import com.connectwork.proyecto2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario validarLogin(String username, String password) {
        Usuario usuarioValido = null;
        String sql = "SELECT id_usuario, tipo_usuario, username, nombre_completo FROM Usuario WHERE username = ? AND password = ? AND estado = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Reemplazamos los signos de interrogacion por los datos reales
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuarioValido = new Usuario(); // Creamos la caja
                    usuarioValido.setIdUsuario(rs.getInt("id_usuario"));
                    usuarioValido.setTipoUsuario(rs.getString("tipo_usuario"));
                    usuarioValido.setUsername(rs.getString("username"));
                    usuarioValido.setNombreCompleto(rs.getString("nombre_completo"));
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
                psUsuario.setString(3, nuevoUsuario.getPassword());
                psUsuario.setString(4, nuevoUsuario.getNombreCompleto());
                psUsuario.setString(5, nuevoUsuario.getCorreo());
                psUsuario.setString(6, nuevoUsuario.getTelefono());
                psUsuario.setString(7, nuevoUsuario.getDireccion());
                psUsuario.setString(8, nuevoUsuario.getCui());
                psUsuario.setString(9, nuevoUsuario.getFechaNacimiento());

                int filasAfectadas = psUsuario.executeUpdate();

                if (filasAfectadas > 0) {
                    // Capturamos el nuevo ID que MySQL le dio a este usuario
                    try (ResultSet rs = psUsuario.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idGenerado = rs.getInt(1);

                            // 4. Preguntamos que rol eligio y le creamos su perfil correspondiente
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
                    conn.rollback(); // Si no se pudo crear el usuario, cancelamos todo
                    return false;
                }
                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback(); // Deshacemostodo si hay un error
                System.out.println("Error en la transacción de registro: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error de conexión al registrar: " + e.getMessage());
            return false;
        }
    }
}
