package pe.tuna.app.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import pe.tuna.app.models.entity.Factura;
import pe.tuna.app.models.entity.ItemFactura;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Locale;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Factura factura = (Factura) model.get("factura");

        // 1ra forma de obtener los mensajes de traduccion
        Locale locale = localeResolver.resolveLocale(request);

        // 2da forma mas facil y rapida para los mensajes de traduccion
        MessageSourceAccessor mensajes = getMessageSourceAccessor();

        // Tabla datos del cliente
        PdfPTable tableCliente = new PdfPTable(1);
        tableCliente.setSpacingAfter(20);
        // Generamos una celda para poder darle estilos a la cabecera
        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale))); //"Datos del cliente"
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);

        tableCliente.addCell(cell);
        tableCliente.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tableCliente.addCell(factura.getCliente().getEmail());

        // Tabla datos de la factura
        PdfPTable tableFactura = new PdfPTable(1);
        tableFactura.setSpacingAfter(20);
        // Generamos una celda para poder darle estilos a la cabecera
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.factura", null, locale))); //"Datos de la factura"
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);

        tableFactura.addCell(cell);
        tableFactura.addCell(mensajes.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
        tableFactura.addCell(mensajes.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
        tableFactura.addCell(mensajes.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());

        // Tabla para el detalle de la factura
        PdfPTable tableDetalle = new PdfPTable(4);
        tableDetalle.setWidths(new float[]{3.5f, 1, 1, 1});
        tableDetalle.addCell(mensajes.getMessage("text.factura.form.item.nombre"));
        tableDetalle.addCell(mensajes.getMessage("text.factura.form.item.precio"));
        tableDetalle.addCell(mensajes.getMessage("text.factura.form.item.cantidad"));
        tableDetalle.addCell(mensajes.getMessage("text.factura.form.item.importe"));

        for (ItemFactura item: factura.getItems()) {
            tableDetalle.addCell(item.getProducto().getNombre());
            tableDetalle.addCell(item.getProducto().getPrecio().toString());

            cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tableDetalle.addCell(cell);

            cell = new PdfPCell(new Phrase(item.calcularImporte().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tableDetalle.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(mensajes.getMessage("text.factura.form.total")));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

        tableDetalle.addCell(cell);

        cell = new PdfPCell(new Phrase(factura.getTotal().toString()));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tableDetalle.addCell(cell);

        document.add(tableCliente);
        document.add(tableFactura);
        document.add(tableDetalle);

    }
}
