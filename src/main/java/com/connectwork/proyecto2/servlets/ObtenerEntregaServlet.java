package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.EntregaDAO;
import com.connectwork.proyecto2.models.Entrega;
import com.google.gson.Gson;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet ("/api/entregas/obtener")
public class ObtenerEntregaServlet extends HttpServlet {
    private EntregaDAO entregaDAO = new EntregaDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            int idProyecto = Integer.parseInt(req.getParameter("idProyecto"));
            Entrega entrega = entregaDAO.obtenerEntregaPorProyecto(idProyecto);

            if (entrega != null) {
                resp.getWriter().print(gson.toJson(entrega));
            } else {
                resp.getWriter().print("{}"); // Devuelve vacío si no hay entrega
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
