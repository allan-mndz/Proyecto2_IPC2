package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.BilleteraDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/billetera")
public class BilleteraServlet extends HttpServlet {
    private BilleteraDAO billeteraDAO = new BilleteraDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("application/json");

        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
        String rol = req.getParameter("rol");

        double saldo = billeteraDAO.obtenerSaldo(idUsuario, rol);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("saldo", saldo);
        resp.getWriter().print(gson.toJson(jsonResponse));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        JsonObject datos = gson.fromJson(req.getReader(), JsonObject.class);
        int idUsuario = datos.get("idUsuario").getAsInt();
        double monto = datos.get("monto").getAsDouble();

        boolean exito = billeteraDAO.recargarSaldo(idUsuario, monto);

        if (exito) {
            resp.getWriter().print("{\"status\":\"success\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"status\":\"error\"}");
        }
    }
}
