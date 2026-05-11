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
import java.util.List;

@WebServlet("/api/proyectos/mis-publicaciones")
public class MisPublicacionesServlet extends HttpServlet {
    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private Gson gson = new Gson();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("idUsuario");

        if (idStr != null) {
            int idUsuario = Integer.parseInt(idStr);
            List<Proyecto> misProyectos = proyectoDAO.obtenerProyectosPorCliente(idUsuario);
            resp.getWriter().print(gson.toJson(misProyectos));
        }
    }
}
