package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.PerfilDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/perfil/completar")
public class PerfilServlet extends HttpServlet {
    private PerfilDAO perfilDAO = new PerfilDAO();
    private Gson gson = new Gson();

    private static class PeticionPerfil {
        String tipoUsuario; // CLIENTE o FREELANCER
        int idUsuario;
        // Datos de Cliente
        String descripcion;
        String sector;
        String sitioWeb;
        // Datos de Freelancer
        String biografia;
        String nivelExperiencia;
        double tarifaHora;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            PeticionPerfil datos = gson.fromJson(req.getReader(), PeticionPerfil.class);
            boolean exito = false;

            System.out.println("--- DATOS RECIBIDOS EN EL SERVLET ---");
            System.out.println("ID RECIBIDO: " + datos.idUsuario);
            System.out.println("ROL: " + datos.tipoUsuario);
            System.out.println("TARIFA: " + datos.tarifaHora);

            if (datos.tipoUsuario.equals("CLIENTE")) {
                exito = perfilDAO.guardarPerfilCliente(datos.idUsuario, datos.descripcion, datos.sector, datos.sitioWeb);
            } else if (datos.tipoUsuario.equals("FREELANCER")) {
                exito = perfilDAO.guardarPerfilFreelancer(datos.idUsuario, datos.biografia, datos.nivelExperiencia, datos.tarifaHora);
            }

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