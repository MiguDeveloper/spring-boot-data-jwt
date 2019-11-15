package pe.tuna.app.models.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.tuna.app.models.dao.IUsuarioDao;
import pe.tuna.app.models.entity.Role;
import pe.tuna.app.models.entity.Usuario;

import java.util.ArrayList;
import java.util.List;


@Service("jpaUserDetailsService")
@Transactional(readOnly = true)
public class JpaUserDetailsService implements UserDetailsService {
    
    @Autowired
    private IUsuarioDao usuarioDao;

    private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);

        if (usuario == null){
            logger.error("[Miguel loadUsername]: Error login no existe el usuario '" + username + "'");
            throw new UsernameNotFoundException("Username " + username + " no existe en el sistema");
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (Role role: usuario.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
            logger.info("[Miguel] Rol: ".concat(role.getAuthority()));
        }

        if (authorities.isEmpty()){
            logger.error("[Miguel loadUsername]: Error login, usuario: " + username + " no tiene roles asignados");
            throw new UsernameNotFoundException("Usuario " + username + " sin roles asignados");
        }

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnable(), true, true, true, authorities);
    }
}
