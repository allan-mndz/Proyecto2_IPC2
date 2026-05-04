package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.UsuarioDAO;
import com.connectwork.proyecto2.models.Usuario;
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

@WebServlet(name = "RegistroServlet", urlPatterns = {"/api/registro"})
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = request.getReader(); // Leer el JSON que enviará Angular
            Usuario nuevoUsuario = gson.fromJson(reader, Usuario.class); //Convertir ese JSON a un objeto Usuario de Java
            PrintWriter out = response.getWriter();
            JsonObject jsonResponse = new JsonObject();

            boolean exito = usuarioDAO.registrarUsuario(nuevoUsuario); // Mandar a guardar a la base de datos

            if(exito){
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Usuario registrado exitosamente");
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Error al registrar usuario");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            out.print(jsonResponse.toString()); // Devolver una respuesta JSON a Angular
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"status\":\"error\",\"message\":\"Error al procesar la solicitud: " + e.getMessage() + "\"}");
            out.flush();
        }
    }
}
