package uce.edu.web.api.auth.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import uce.edu.web.api.auth.application.representation.UsuarioRepresentation;
import uce.edu.web.api.auth.domain.Usuario;
import uce.edu.web.api.auth.infraestructure.UsuarioRepository;

@ApplicationScoped
public class UsuarioService {

    @Inject
    private UsuarioRepository usuarioRepository;

    public UsuarioRepresentation findByUsuario(String usuario) {
        return this.mapperToUR(this.usuarioRepository.find("usuario", usuario).firstResult());
    }

    private UsuarioRepresentation mapperToUR(Usuario usuario){
        UsuarioRepresentation uRepresentation = new UsuarioRepresentation();

        uRepresentation.setUsuario(usuario.getUsuario());
        uRepresentation.setPassword(usuario.getPassword());
        uRepresentation.setRole(usuario.getRole());
        return uRepresentation;

    }

    
}
