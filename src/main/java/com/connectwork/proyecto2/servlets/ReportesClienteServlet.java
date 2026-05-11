package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.ReportesClienteDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/reportes/cliente")
public class ReportesClienteServlet extends HttpServlet {
    private ReportesClienteDAO reportesDAO = new ReportesClienteDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String tipo = req.getParameter("tipo");
        String idUsuarioParam = req.getParameter("idUsuario");
        String fechaInicio = req.getParameter("fechaInicio");
        String fechaFin = req.getParameter("fechaFin");

        if (idUsuarioParam != null && tipo != null) {
            int idUsuario = Integer.parseInt(idUsuarioParam);
            List<Map<String, Object>> datos = null;

            switch (tipo) {
                case "recargas":
                    datos = reportesDAO.obtenerHistorialRecargas(idUsuario);
                    break;
                case "proyectos":
                    datos = reportesDAO.obtenerHistorialProyectos(idUsuario, fechaInicio, fechaFin);
                    break;
                case "gastos":
                    datos = reportesDAO.obtenerGastoPorCategoria(idUsuario, fechaInicio, fechaFin);
                    break;
            }
            resp.getWriter().print(gson.toJson(datos));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}