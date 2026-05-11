package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.PropuestaDAO;
import com.connectwork.proyecto2.models.Propuesta;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/proyectos/postularse")
public class PostularseServlet extends HttpServlet {
    private PropuestaDAO propuestaDAO = new PropuestaDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Propuesta nuevaPropuesta = gson.fromJson(req.getReader(), Propuesta.class);
            boolean exito = propuestaDAO.registrarPropuesta(nuevaPropuesta);

            if(exito) {
                resp.getWriter().print("{\"status\":\"success\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\"}");
            }
        } catch (Exception e) {
            resp.getWriter().print("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        int idPropuesta = Integer.parseInt(req.getParameter("idPropuesta"));

        boolean exito = propuestaDAO.retirarPropuesta(idPropuesta);

        if (exito) {
            resp.getWriter().print("{\"status\":\"success\"}");
        } else {
            resp.getWriter().print("{\"status\":\"error\"}");
        }
    }
}