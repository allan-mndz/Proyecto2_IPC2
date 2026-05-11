package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.ContratoDAO;
import com.connectwork.proyecto2.models.Contrato;
import com.google.gson.Gson;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/contratos/freelancer")
public class ListarContratosFreelancerServlet extends HttpServlet {
    private ContratoDAO contratoDAO = new ContratoDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
            List<Contrato> contratos = contratoDAO.obtenerContratosPorFreelancer(idUsuario);
            resp.getWriter().print(gson.toJson(contratos));
        } catch (Exception e) {
            System.out.println("Error al obtener contratos: " + e.getMessage());
            e.printStackTrace();

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
