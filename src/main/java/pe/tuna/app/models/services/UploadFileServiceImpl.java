package pe.tuna.app.models.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String UPLOADS_FOLDER = "uploads";

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path pathFoto = getPath(filename);
        logger.info("[MIGUEL] pathFoto: " + pathFoto);

        Resource recurso = null;

        recurso = new UrlResource(pathFoto.toUri());
        if (!recurso.exists() || !recurso.isReadable()) {
            throw new RuntimeException("Error: no se puede cargar imagen " + pathFoto.toString());
        }

        return recurso;
    }

    @Override
    public String copy(MultipartFile file) throws IOException {
        // sobreescribimos el nombre del archivo para que sea unico
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // Ahora debemos tener la ruta absoluta es decir desde c://...
        Path rootAbsolutePath = getPath(uniqueFileName);
        logger.info("El rootAbsolutePath es: " + rootAbsolutePath); // Path absoluto: c:/...

        /* Ejemplo con Files.write
         byte[] bytes = foto.getBytes();
         Path rutaCompleta = Paths.get(rootPath + "/" + foto.getOriginalFilename());
         Files.write(rutaCompleta, bytes);
        */
        // con la ruta absoluta usaremos el metodo copy
        Files.copy(file.getInputStream(), rootAbsolutePath);

        return uniqueFileName;
    }

    @Override
    public boolean delete(String filename) {
        // Ahora eliminamos la imagen del cliente
        Path rootPath = getPath(filename);
        File archivo = rootPath.toFile();

        if (archivo.exists() && archivo.canRead()){
            if (archivo.delete()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
    }

    @Override
    public void init() throws IOException {
        Files.createDirectory(Paths.get(UPLOADS_FOLDER));
    }

    public Path getPath(String filename) {
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }
}
