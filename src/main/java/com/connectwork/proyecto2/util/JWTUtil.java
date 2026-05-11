package com.connectwork.proyecto2.util;

import com.connectwork.proyecto2.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTUtil {
    private static final String SECRET = "EstaEsUnaClaveSuperSecretaParaConnectWork2026";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // El token durara 24 horas
    private static final long EXPIRATION_TIME = 86400000;

    // Generar Token al iniciar sesión
    public static String generateToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getUsername()) // Guardamos el username
                .claim("id", usuario.getIdUsuario()) // Guardamos el ID
                .claim("rol", usuario.getTipoUsuario()) // Guardamos el ROL (Admin, Cliente, Freelancer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
}