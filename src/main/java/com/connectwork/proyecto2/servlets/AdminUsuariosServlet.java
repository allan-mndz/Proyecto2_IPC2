package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.AdminUsuariosDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/admin/usuarios")
public class AdminUsuariosServlet extends HttpServlet {
    private AdminUsuariosDAO adminDAO = new AdminUsuariosDAO();
    private Gson gson = new Gson();

    private static class PeticionEstado {
        int idUsuario;
        String nuevoEstado;
    }

    // GET: Para pedir la lista de usuarios
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(gson.toJson(adminDAO.obtenerUsuarios()));
    }

    // POST: Para activar/desactivar un usuario
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            PeticionEstado datos = gson.fromJson(req.getReader(), PeticionEstado.class);
            boolean exito = adminDAO.cambiarEstado(datos.idUsuario, datos.nuevoEstado);

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