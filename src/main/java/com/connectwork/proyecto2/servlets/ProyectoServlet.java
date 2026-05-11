package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.ProyectoDAO;
import com.connectwork.proyecto2.models.Proyecto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "ProyectoServlet", urlPatterns = {"/api/proyectos/publicar"})
public class ProyectoServlet extends HttpServlet {

    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try{
            // leer datos enviados por angular
            BufferedReader reader = req.getReader();
            Proyecto nuevoProyecto = gson.fromJson(reader, Proyecto.class);

            PrintWriter out = resp.getWriter();
            JsonObject jsonResponse = new JsonObject();

            // guardar en la base de datos usando el DAO
            boolean exito = proyectoDAO.publicarProyecto(nuevoProyecto);

            if(exito){
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Proyecto publicado exitosamente");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Error al publicar el proyecto");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            out.print(jsonResponse.toString());
            out.flush();
        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.print("{\"status\": \"error\", \"message\": \"Error al procesar la solicitud\"}");
            out.flush();
        }
    }
}
