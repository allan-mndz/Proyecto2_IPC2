package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.AdminCategoriasDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/admin/categorias")
public class AdminCategoriasServlet extends HttpServlet {
    private AdminCategoriasDAO catDAO = new AdminCategoriasDAO();
    private Gson gson = new Gson();

    private static class PeticionCategoria {
        String accion;
        int idCategoria;
        String nombre;
        String descripcion; // <-- CAMBIO AQUÍ
        int estado;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(gson.toJson(catDAO.obtenerCategorias()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            PeticionCategoria datos = gson.fromJson(req.getReader(), PeticionCategoria.class);
            boolean exito = false;

            if (datos.accion != null) {
                switch (datos.accion) {
                    case "crear":
                        exito = catDAO.crearCategoria(datos.nombre, datos.descripcion); // <-- CAMBIO AQUÍ
                        break;
                    case "editar":
                        exito = catDAO.editarCategoria(datos.idCategoria, datos.nombre, datos.descripcion); // <-- CAMBIO AQUÍ
                        break;
                    case "estado":
                        exito = catDAO.cambiarEstado(datos.idCategoria, datos.estado);
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