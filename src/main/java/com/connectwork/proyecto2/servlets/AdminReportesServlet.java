package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.AdminReportesDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/admin/reportes")
public class AdminReportesServlet extends HttpServlet {
    private AdminReportesDAO dao = new AdminReportesDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String tipo = req.getParameter("tipo");
        String f1 = req.getParameter("fechaInicio");
        String f2 = req.getParameter("fechaFin");

        Object resultado = null;
        switch (tipo) {
            case "comisiones": resultado = dao.obtenerHistorialComisiones(); break;
            case "freelancers": resultado = dao.obtenerTopFreelancers(f1, f2); break;
            case "categorias": resultado = dao.obtenerTopCategorias(f1, f2); break;
            case "ingresos": resultado = dao.obtenerIngresosTotales(f1, f2); break;
        }
        resp.getWriter().print(gson.toJson(resultado));
    }
}