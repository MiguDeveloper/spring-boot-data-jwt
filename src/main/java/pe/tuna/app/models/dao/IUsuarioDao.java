package pe.tuna.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import pe.tuna.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
    public Usuario findByUsername(String username);
}
