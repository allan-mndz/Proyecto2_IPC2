package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.EntregaDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet ("/api/entregas/subir")
public class SubirEntregaServlet extends HttpServlet {
    private EntregaDAO entregaDAO = new EntregaDAO();
    private Gson gson = new Gson();

    private class DatosEntrega {
        int idContrato;
        int idProyecto;
        String descripcion;
        String archivosUrl;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            DatosEntrega datos = gson.fromJson(req.getReader(), DatosEntrega.class);
            boolean exito = entregaDAO.registrarEntrega(datos.idContrato, datos.idProyecto, datos.descripcion, datos.archivosUrl);

            if (exito){
                resp.getWriter().print("{\"status\":\"success\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\"}");
            }
        }catch (Exception e){
            System.out.println("Error al registrar entrega: " + e.getMessage());
            e.printStackTrace();

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
