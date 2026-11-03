package br.com.novaroupa.service;

import br.com.novaroupa.model.TipoPerfil;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/** Garante a existência do perfil local após autenticação. */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario garantirPerfilLocal(String username, Collection<? extends GrantedAuthority> authorities) {
        return usuarioRepository.findByUsername(username)
                .orElseGet(() -> usuarioRepository.save(new Usuario(username, determinarTipoPerfil(authorities))));
    }

    private TipoPerfil determinarTipoPerfil(Collection<? extends GrantedAuthority> authorities) {
        if (possuiPerfil(authorities, "ROLE_ADMIN")) {
            return TipoPerfil.ADMIN;
        }
        if (possuiPerfil(authorities, "ROLE_VENDEDOR")) {
            return TipoPerfil.VENDEDOR;
        }
        return TipoPerfil.COMPRADOR;
    }

    private boolean possuiPerfil(Collection<? extends GrantedAuthority> authorities, String perfil) {
        return authorities != null && authorities.stream()
                .anyMatch(authority -> perfil.equals(authority.getAuthority()));
    }
}
