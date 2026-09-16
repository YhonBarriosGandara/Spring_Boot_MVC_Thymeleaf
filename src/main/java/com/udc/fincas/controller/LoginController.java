package com.udc.fincas.controller;

import com.udc.fincas.entity.Usuario;
import com.udc.fincas.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/dashboard";
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("id") String id,
                                @RequestParam("clave") String clave,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirect) {
        if (id == null || id.trim().isEmpty() || clave == null || clave.trim().isEmpty()) {
            model.addAttribute("mensajeError", "Debe ingresar el identificador y la contraseña.");
            return "auth/login";
        }

        Optional<Usuario> usuarioOpt = usuarioService.autenticar(id, clave);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioLogueado", usuario);
            redirect.addFlashAttribute("mensajeExito", "¡Bienvenido, " + usuario.getNombre() + "!");
            return "redirect:/dashboard";
        } else {
            model.addAttribute("mensajeError", "Credenciales incorrectas. Verifique su ID de usuario o contraseña.");
            model.addAttribute("idIngresado", id);
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirect) {
        session.removeAttribute("usuarioLogueado");
        session.invalidate();
        redirect.addFlashAttribute("mensajeInfo", "Ha cerrado sesión de forma segura.");
        return "redirect:/login";
    }
}
