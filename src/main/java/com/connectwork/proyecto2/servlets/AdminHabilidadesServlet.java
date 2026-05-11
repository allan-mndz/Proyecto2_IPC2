package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.AdminHabilidadesDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/admin/habilidades")
public class AdminHabilidadesServlet extends HttpServlet {
    private AdminHabilidadesDAO habDAO = new AdminHabilidadesDAO();
    private Gson gson = new Gson();

    private static class PeticionHabilidad {
        String accion;
        int idHabilidad;
        int idCategoria;
        String nombre;
        String descripcion;
        int estado;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(gson.toJson(habDAO.obtenerHabilidades()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            PeticionHabilidad datos = gson.fromJson(req.getReader(), PeticionHabilidad.class);
            boolean exito = false;

            if (datos.accion != null) {
                switch (datos.accion) {
                    case "crear":
                        exito = habDAO.crearHabilidad(datos.idCategoria, datos.nombre, datos.descripcion);
                        break;
                    case "editar":
                        exito = habDAO.editarHabilidad(datos.idHabilidad, datos.idCategoria, datos.nombre, datos.descripcion);
                        break;
                    case "estado":
                        exito = habDAO.cambiarEstado(datos.idHabilidad, datos.estado);
                        break;
                }
            }

            if (exito) {
                resp.getWriter().print("{\"status\":\"success\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}