package pe.tuna.app.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pe.tuna.app.auth.SimpleGrantedAuthoritiesMixin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader("Authorization");
        if (!requireAuthentication(header)) {
            chain.doFilter(request, response);
            return;
        }
        boolean validoToken;

        // Los claims son los todos los datos que se usan para la autenticacion
        Claims token = null;

        try {
            token = Jwts.parser()
                    .setSigningKey(JWTAuthenticationFilter.SECRET_KEY)
                    .parseClaimsJws(header.replace("Bearer ", ""))
                    .getBody();

            validoToken = true;
        } catch (JwtException | IllegalArgumentException e) {
            validoToken = false;
        }

        UsernamePasswordAuthenticationToken  authentication = null;
        if (validoToken){
            String username = token.getSubject();
            Object roles = token.get("authorities");

            Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
                    .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthoritiesMixin.class)
                    .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

            authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

        }

        // Asignamos al contexto de seguridad el authentication: autenticara al usuario dentro de la solicitud
        // ya que no estamos manajando sesiones
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    protected boolean requireAuthentication(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }
}