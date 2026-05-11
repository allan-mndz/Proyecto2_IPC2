package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.ReportesFreelancerDAO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/reportes-freelancer")
public class ReportesFreelancerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String tipo = req.getParameter("tipo");
            int idUsuario = Integer.parseInt(req.getParameter("id"));
            String fechaInicio = req.getParameter("fechaInicio");
            String fechaFin = req.getParameter("fechaFin");

            ReportesFreelancerDAO dao = new ReportesFreelancerDAO();
            Object resultado = null;

            if ("saldo".equals(tipo)) {
                resultado = dao.obtenerSaldo(idUsuario);
            } else if ("contratos".equals(tipo)) {
                resultado = dao.obtenerHistorialContratos(idUsuario, fechaInicio, fechaFin);
            } else if ("top_categorias".equals(tipo)) {
                resultado = dao.obtenerTopCategorias(idUsuario);
            } else if ("propuestas".equals(tipo)) {
                resultado = dao.obtenerPropuestas(idUsuario, fechaInicio, fechaFin);
            }

            String json = new Gson().toJson(resultado);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.print(json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Error interno al procesar el reporte\"}");
        }
    }
}