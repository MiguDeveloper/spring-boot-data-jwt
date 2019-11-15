package pe.tuna.app.auth.filter;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pe.tuna.app.auth.service.IJWTService;
import pe.tuna.app.auth.service.JWTServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private IJWTService ijwtService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, IJWTService ijwtService) {
        super(authenticationManager);
        this.ijwtService = ijwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(JWTServiceImpl.HEADER_STRING);
        if (!requireAuthentication(header)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken  authentication = null;
        if (ijwtService.validate(header)){
            String username = ijwtService.getUsername(header);

            Collection<? extends GrantedAuthority> authorities = ijwtService.getRoles(header);

            authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

        }

        // Asignamos al contexto de seguridad el authentication: autenticara al usuario dentro de la solicitud
        // ya que no estamos manajando sesiones
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    protected boolean requireAuthentication(String header) {
        if (header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}