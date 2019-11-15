package pe.tuna.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.tuna.app.models.entity.Cliente;


public interface IClienteDao extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM  Cliente c LEFT JOIN FETCH c.facturas f WHERE c.id = ?1")
    public Cliente fetchByIdWithFacturas(Long id);
}
