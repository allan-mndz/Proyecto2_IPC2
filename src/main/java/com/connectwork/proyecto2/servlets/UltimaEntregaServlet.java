package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.EntregaDAO;
import com.connectwork.proyecto2.models.Entrega;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/entregas/ultima")
public class UltimaEntregaServlet extends HttpServlet {
    private EntregaDAO entregaDAO = new EntregaDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String idContratoParam = req.getParameter("idContrato");
        if (idContratoParam != null) {
            int idContrato = Integer.parseInt(idContratoParam);
            Entrega ultima = entregaDAO.obtenerUltimaEntrega(idContrato);

            if (ultima != null){
                resp.getWriter().print(gson.toJson(ultima));
            }else {
                resp.getWriter().print("{}");
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
