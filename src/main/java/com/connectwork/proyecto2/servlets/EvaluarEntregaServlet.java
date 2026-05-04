package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.EntregaDAO;
import com.google.gson.Gson;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet ("/api/entregas/evaluar")
public class EvaluarEntregaServlet extends HttpServlet {
    private EntregaDAO entregaDAO = new EntregaDAO();
    private Gson gson = new Gson();

    private class DatosEvaluacion {
        int idEntrega;
        int idProyecto;
        int idContrato;
        boolean esAceptada;
        String motivoRechazo;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            DatosEvaluacion datos = gson.fromJson(req.getReader(), DatosEvaluacion.class);
            boolean exito = entregaDAO.evaluarEntrega(datos.idEntrega, datos.idProyecto, datos.idContrato, datos.esAceptada, datos.motivoRechazo);

            if (exito) {
                resp.getWriter().print("{\"status\":\"success\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\"}");
            }
        } catch (Exception e) {
            System.out.println("error al entregar: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
