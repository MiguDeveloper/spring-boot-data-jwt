package pe.tuna.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.tuna.app.models.entity.Factura;

public interface IFacturaDao extends JpaRepository<Factura, Long> {
    @Query("SELECT f FROM Factura f JOIN FETCH f.cliente c JOIN FETCH f.items l JOIN FETCH l.producto WHERE f.id = ?1")
    public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);
}
