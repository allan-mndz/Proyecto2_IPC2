package com.connectwork.proyecto2.servlets;

import com.connectwork.proyecto2.dao.UsuarioDAO;
import com.connectwork.proyecto2.models.Usuario;
import com.connectwork.proyecto2.util.JWTUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = req.getReader();
            JsonObject jsonReq = JsonParser.parseReader(reader).getAsJsonObject();

            String username = jsonReq.get("username").getAsString();
            String password = jsonReq.get("password").getAsString();

            System.out.println("INTENTO DE LOGIN -> Usuario: [" + username + "] Password: [" + password + "]");

            Usuario usuario = usuarioDAO.validarLogin(username, password);


            PrintWriter out = resp.getWriter();
            JsonObject jsonResponse = new JsonObject();

            if (usuario != null) {
                boolean completado = usuarioDAO.tienePerfilCompleto(usuario.getIdUsuario(), usuario.getTipoUsuario());
                String token = JWTUtil.generateToken(usuario);
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("token", token);
                jsonResponse.addProperty("perfilCompletado", completado);
                jsonResponse.add("usuario", gson.toJsonTree(usuario));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Usuario o contraseña incorrectos");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            out.print(jsonResponse.toString());
            out.flush();

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.print("{\"status\":\"error\", \"message\":\"Datos incompletos o mal formateados\"}");
            out.flush();
        }
    }
}