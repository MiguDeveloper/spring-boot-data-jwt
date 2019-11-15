package pe.tuna.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String formLogin(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model, Principal principal, RedirectAttributes flash){
        if (principal != null){
            flash.addFlashAttribute("info", "el usuario ya esta logeado");
            return "redirect:/";
        }

        if (error != null){
            model.addAttribute("danger", "Nombre de usuario o contraseña incorrecta");
        }

        if (logout != null){
            model.addAttribute("success", "Cerro con exito la sesion");;
        }

        return "login";
    }
}
