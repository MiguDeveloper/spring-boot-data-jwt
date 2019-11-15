package pe.tuna.app.models.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IUploadFileService {
    // Mostrar la imagen en los html
    public Resource load(String filename) throws MalformedURLException;

    // Copiar la imagen ya renombrada
    public String copy(MultipartFile file) throws IOException;

    // Elimina la imagen guardada
    public boolean delete(String filename);

    // Borramos todo el directorio con las imagenes
    public void deleteAll();
    public void init() throws IOException;
}
