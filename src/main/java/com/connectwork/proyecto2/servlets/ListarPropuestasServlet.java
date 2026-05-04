package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.PropuestaDAO;
import com.connectwork.proyecto2.models.Propuesta;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/proyectos/propuestas")
public class ListarPropuestasServlet extends HttpServlet {
    private PropuestaDAO propuestaDAO = new PropuestaDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try{
            int idProyecto = Integer.parseInt(req.getParameter("idProyecto"));
            List<Propuesta> propuestas = propuestaDAO.obtenerPropuestasPorProyecto(idProyecto);
            resp.getWriter().print(gson.toJson(propuestas));
        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        }

    }
}
