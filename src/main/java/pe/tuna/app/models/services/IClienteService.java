package pe.tuna.app.models.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.tuna.app.models.entity.Cliente;
import pe.tuna.app.models.entity.Factura;
import pe.tuna.app.models.entity.Producto;

import java.util.List;

public interface IClienteService {
    public List<Cliente> findAll();
    public Page<Cliente> findAll(Pageable pageable);
    public void save(Cliente cliente);
    public Cliente findOne(Long id);
    public Cliente fetchByIdWithFacturas(Long id);
    public void delete(Long id);
    public List<Producto> findProductoByNombre(String term);
    public void saveFactura(Factura factura);
    public Producto findProductoById(Long id);
    public Factura findFacturaById(Long id);
    public void deleteFactura(Long id);
    public  Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id);
}
