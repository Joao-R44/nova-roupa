package br.com.novaroupa.controller;

import br.com.novaroupa.dto.LoginRequestDTO;
import br.com.novaroupa.dto.LoginResponseDTO;
import br.com.novaroupa.security.JwtTokenProvider;
import br.com.novaroupa.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

/** Endpoints de autenticação. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            // O perfil local é criado ou recuperado somente após a autenticação ser aceita.
            usuarioService.garantirPerfilLocal(authentication.getName(), authentication.getAuthorities());
            List<String> roles = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .toList();
            return ResponseEntity.ok(new LoginResponseDTO(tokenProvider.generateToken(authentication), "Bearer",
                    authentication.getName(), roles, tokenProvider.getExpirationMs()));
        } catch (AuthenticationException exception) {
            // Não revelar se o usuário ou a senha estavam incorretos.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciais inválidas"));
        }
    }
}
