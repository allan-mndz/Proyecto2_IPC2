package com.connectwork.proyecto2.dao;

import com.connectwork.proyecto2.models.Proyecto;
import com.connectwork.proyecto2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {

    public boolean publicarProyecto(Proyecto nuevoProyecto){
        String sql = "INSERT INTO Proyecto (id_cliente, id_categoria, titulo, descripcion, presupuesto_maximo, fecha_limite, estado) " +
                "VALUES ((SELECT id_cliente FROM Cliente WHERE id_usuario = ? LIMIT 1), ?, ?, ?, ?, ?, 'ABIERTO')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevoProyecto.getIdCliente());
            stmt.setInt(2, nuevoProyecto.getIdCategoria());
            stmt.setString(3, nuevoProyecto.getTitulo());
            stmt.setString(4, nuevoProyecto.getDescripcion());
            stmt.setDouble(5, nuevoProyecto.getPresupuestoMaximo());
            stmt.setString(6, nuevoProyecto.getFechaLimite());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    public List<Proyecto> obtenerProyectosDisponibles() {
       List<Proyecto> listaProyectos = new ArrayList<>();
       String sql = "SELECT * FROM Proyecto WHERE estado = 'ABIERTO'";

       try(Connection conn = DBConnection.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql);
           ResultSet rs = stmt.executeQuery()) {

           while (rs.next()) {
               Proyecto proyecto = new Proyecto();
               proyecto.setIdProyecto(rs.getInt("id_proyecto"));
               proyecto.setIdCliente(rs.getInt("id_cliente"));
               proyecto.setIdCategoria(rs.getInt("id_categoria"));
               proyecto.setTitulo(rs.getString("titulo"));
               proyecto.setDescripcion(rs.getString("descripcion"));
               proyecto.setPresupuestoMaximo(rs.getDouble("presupuesto_maximo"));
               proyecto.setFechaLimite(rs.getString("fecha_limite"));
               proyecto.setEstado(rs.getString("estado"));

               listaProyectos.add(proyecto);
           }
       } catch (SQLException e) {
           e.printStackTrace();
           System.out.println("Error al obtener proyectos disponibles: " + e.getMessage());
       }
       return listaProyectos;
    }

    public List<Proyecto> obtenerProyectosPorCliente(int idUsuario) {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT p.* FROM Proyecto p JOIN Cliente c ON p.id_cliente = c.id_cliente WHERE c.id_usuario = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Proyecto p = new Proyecto();
                p.setIdProyecto(rs.getInt("id_proyecto"));
                p.setTitulo(rs.getString("titulo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPresupuestoMaximo(rs.getDouble("presupuesto_maximo"));
                p.setFechaLimite(rs.getString("fecha_limite"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error en DAO: " + e.getMessage());
        }
        return lista;
    }
}
