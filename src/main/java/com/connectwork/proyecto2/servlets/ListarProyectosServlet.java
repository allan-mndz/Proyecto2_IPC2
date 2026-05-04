package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.ProyectoDAO;
import com.connectwork.proyecto2.models.Proyecto;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet (name = "ListarProyectosServlet", urlPatterns = {"/api/proyectos/disponibles"})
public class ListarProyectosServlet extends HttpServlet {

    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException , IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            // pedimos la lista al DAO
            List<Proyecto> proyectos = proyectoDAO.obtenerProyectosDisponibles();
            out.print(gson.toJson(proyectos)); // Convertimos esa lista de Java a un texto JSON y la enviamos
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }

}
