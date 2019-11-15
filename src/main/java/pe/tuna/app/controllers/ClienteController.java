package pe.tuna.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.tuna.app.models.entity.Cliente;
import pe.tuna.app.models.services.IClienteService;
import pe.tuna.app.models.services.IUploadFileService;
import pe.tuna.app.util.paginator.PageRender;
import pe.tuna.app.view.xml.ClienteList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Controller
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    // Para usar el multi idioma inyectamos el message source
    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String UPLOADS_FOLDER = "uploads";

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);

    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        // Para que cuando haga la carga de cliente con facturas evitar muchas consultas y realizar solo una
        // ya no usamos la carga peresoza sino una sola consulta y ya no 'findOneid()'
        Cliente cliente = clienteService.fetchByIdWithFacturas(id);//clienteService.findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("danger", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "cliente:" + cliente.getApellido());

        return "ver";
    }

    @GetMapping("listar-rest")
    @ResponseBody
    public ClienteList listarRest(){
        return new ClienteList(clienteService.findAll());
    }

    @GetMapping({"/listar", "/"})
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
                         Authentication authentication,
                         HttpServletRequest request,
                         Locale locale) {

        // Solo para pruebas
        if (authentication != null) {
            logger.info("[DEBUG MIGUEL] Usuario autenticado: ".concat(authentication.getName()));
        }

        // Tres Formas de obtener el rol en el controlador

        // Solo para pruebas: otra forma de obtener el authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("[DEBUG MIGUEL] Forma estatica SecurityContextHolder, Usuario autenticado: ".concat(auth.getName()));
        }

        // 1ra Forma: manual programando una funcion
        if (hasRole("ROLE_ADMIN")) {
            logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Hola ".concat(auth.getName()).concat(" no tienes acceso"));
        }

        // 2da forma: usando el requestWrapper servletReauest request
        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
        if (securityContext.isUserInRole("ADMIN")){
            logger.info("Usando Wrapper: Hola ".concat(auth.getName()).concat(" tienes acceso"));
        }else{
            logger.info("Usando Wrapper: Hola ".concat(auth.getName()).concat(" no tienes acceso"));
        }

        // 3ra forma: nativa usando solo el request
        if(request.isUserInRole("ROLE_ADMIN")){
            logger.info("Usando request nativo: Hola ".concat(auth.getName()).concat(" tienes acceso"));
        }else{
            logger.info("Usando request nativo: Hola ".concat(auth.getName()).concat(" no tienes acceso"));
        }


        Pageable pageRequest = PageRequest.of(page, 4); // recordar que esto es con springboot 2
        // ya que en spring boot 1 se usa: new PageRequest

        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);

        return "listar";
    }

    @GetMapping("/form")
    public String formCrear(Model model) {
        Cliente cliente = new Cliente();
        model.addAttribute("titulo", "Formulario de cliente");
        model.addAttribute("cliente", cliente);

        return "form";
    }

    @PostMapping("/form")
    public String guardar(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash, Model model,
                          @RequestParam("file") MultipartFile foto) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }
        if (!foto.isEmpty()) {

            if (cliente.getId() != null
                    && cliente.getId() > 0
                    && cliente.getFoto() != null
                    && cliente.getFoto().length() > 0) {
                // Ahora eliminamos la imagen del cliente
                uploadFileService.delete(cliente.getFoto());
            }
            // Ejemplo path dentro del proyecto pero no es conveniente ya que al desplegar se elimina
            //Path directorioRecursos = Paths.get("src//main//resources//static//uploads");

            // Path en el server local
            // Path directorioRecursos = Paths.get("/Users/miguelchinchay/Documents/img_spring-boot");
            // String rootPath = directorioRecursos.toFile().getAbsolutePath();

            //

            String uniqueFileName = null;
            try {
                uniqueFileName = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }

            flash.addFlashAttribute("info", "Has subido correctamente ' "
                    + uniqueFileName
                    + "'");
            cliente.setFoto(uniqueFileName);


        }

        String mensajeflash = cliente.getId() != null ? "Cliente editado correctamente" : "Cliente creado con exito";
        clienteService.save(cliente);
        flash.addFlashAttribute("success", mensajeflash);

        return "redirect:listar";
    }

    @GetMapping("/form/{id}")
    public String edit(@PathVariable Long id, RedirectAttributes flash, Model model) {
        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("danger", "El cliente no existe");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("danger", "El ID no puede ser cero");
            return "redirect:/listar";
        }

        model.addAttribute("titulo", "Editar cliente");
        model.addAttribute("cliente", cliente);

        return "form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito");

            if (uploadFileService.delete(cliente.getFoto())) {
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito!");
            }

        }

        return "redirect:/listar";
    }

    private boolean hasRole(String role) {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null) {
            return false;
        }

        Authentication auth = context.getAuthentication();

        if (auth == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return authorities.contains(new SimpleGrantedAuthority(role));

        /*
        for (GrantedAuthority authority :
                authorities) {
            if (role.equals(authority.getAuthority())) {
                logger.info("Hola usuario: ".concat(auth.getName()).concat(" tu rol es ".concat(authority.getAuthority())));
                return true;
            }
        }

        return false;
         */
    }

}
