package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.AdminComisionesDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/admin/comisiones")
public class AdminComisionesServlet extends HttpServlet {
    private AdminComisionesDAO comisionDAO = new AdminComisionesDAO();
    private Gson gson = new Gson();

    private static class PeticionComision {
        double nuevoPorcentaje;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(gson.toJson(comisionDAO.obtenerHistorial()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            PeticionComision datos = gson.fromJson(req.getReader(), PeticionComision.class);
            boolean exito = comisionDAO.cambiarComision(datos.nuevoPorcentaje);

            if (exito) {
                resp.getWriter().print("{\"status\":\"success\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}