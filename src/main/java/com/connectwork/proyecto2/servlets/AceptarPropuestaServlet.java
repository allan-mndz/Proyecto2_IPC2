package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.ContratoDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/contratos/aceptar")
public class AceptarPropuestaServlet extends HttpServlet {

    private ContratoDAO contratoDAO = new ContratoDAO();
    private Gson gson = new Gson();

    private class DatosContrato {
        int idPropuesta;
        int idProyecto;
        double montoOfertado;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try{
            DatosContrato datos = gson.fromJson(req.getReader(), DatosContrato.class);
            boolean exito = contratoDAO.generarContrato(datos.idPropuesta, datos.idProyecto, datos.montoOfertado);

            if(exito) {
                resp.getWriter().print("{\"status\":\"success\"}");
            } else {
                resp.getWriter().print("{\"status\":\"error\"}");
            }
        }catch (Exception e){
            System.out.println(">>> ERROR FATAL EN EL SERVLET DE CONTRATO: " + e.getMessage());
            e.printStackTrace();

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
