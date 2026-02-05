package uce.edu.web.api.auth.interfaces;

import java.time.Instant;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import uce.edu.web.api.auth.application.UsuarioService;
import uce.edu.web.api.auth.application.representation.UsuarioRepresentation;

@Path("autorizacion")
public class AuthResource {

    @Inject
    private UsuarioService usuarioService;

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public TokenResponse token(
            @QueryParam("user") String user,
            @QueryParam("password") String password) {

        // Aqui es donde se compara el password y usuario contra la base
        UsuarioRepresentation usuario = usuarioService.findByUsuario(user);
        // TAREA
        boolean ok = false;
        String role = null;
        
        if (usuario != null && usuario.getPassword().equals(password)) {
            ok = true;
            role = usuario.getRole();
        }

        if (ok) {
            String issuer = "matricula-auth";
            long ttl = 3600;

            Instant now = Instant.now();
            Instant exp = now.plusSeconds(ttl);

            String jwt = Jwt.issuer(issuer)
                    .subject(user)
                    .groups(Set.of(role)) // roles: user / admin
                    .issuedAt(now)
                    .expiresAt(exp)
                    .sign();

            return new TokenResponse(jwt, exp.getEpochSecond(), role);
        } else {
            return null;
        }
    }

    public static class TokenResponse {
        public String accessToken;
        public long expiresAt;
        public String role;

        public TokenResponse() {
        }

        public TokenResponse(String accessToken, long expiresAt, String role) {
            this.accessToken = accessToken;
            this.expiresAt = expiresAt;
            this.role = role;
        }
    }

}
