package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.CalificacionDAO;
import com.google.gson.Gson;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/calificaciones/crear")
public class CalificarServlet extends HttpServlet {
    private CalificacionDAO calificacionDAO = new CalificacionDAO();
    private Gson gson = new Gson();

    private class DatosCalificacion {
        int idContrato;
        int estrellas;
        String comentario;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            DatosCalificacion datos = gson.fromJson(req.getReader(), DatosCalificacion.class);
            boolean exito = calificacionDAO.registrarCalificacion(datos.idContrato, datos.estrellas, datos.comentario);

            if (exito) {
                resp.getWriter().print("{\"status\":\"success\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\"}");
            }
        } catch (Exception e) {
            System.out.println(">>> ERROR AL CALIFICAR: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
