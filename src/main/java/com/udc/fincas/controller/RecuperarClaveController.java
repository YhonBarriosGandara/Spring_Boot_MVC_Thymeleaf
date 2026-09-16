package com.udc.fincas.controller;

import com.udc.fincas.entity.Usuario;
import com.udc.fincas.service.EmailService;
import com.udc.fincas.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Optional;

@Controller
public class RecuperarClaveController {

    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    public RecuperarClaveController(UsuarioService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    @GetMapping("/recuperar-clave")
    public String formularioRecuperacion() {
        return "auth/recuperar";
    }

    @PostMapping("/recuperar-clave")
    public String procesarRecuperacion(@RequestParam("identificador") String identificador,
                                       Model model,
                                       RedirectAttributes redirect) {
        if (identificador == null || identificador.trim().isEmpty()) {
            model.addAttribute("mensajeError", "Debe ingresar su identificador de usuario o correo electrónico.");
            return "auth/recuperar";
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorIdOCorreo(identificador.trim());
        if (usuarioOpt.isEmpty()) {
            model.addAttribute("mensajeError", "No existe ninguna cuenta registrada con el identificador o correo '" + identificador + "'.");
            model.addAttribute("identificadorIngresado", identificador);
            return "auth/recuperar";
        }

        Usuario usuario = usuarioOpt.get();
        // Generar clave temporal de 8 digitos alfanumericos
        int numeroAleatorio = 1000 + random.nextInt(9000);
        String claveTemporal = "Agro" + numeroAleatorio + "!";

        usuario.setClave(claveTemporal);
        usuarioService.guardar(usuario);

        emailService.enviarRecuperacionClave(usuario.getEmail(), usuario.getNombre(), claveTemporal);

        // Ocultar parcialmente el correo para proteger privacidad en el mensaje visual
        String correoOculto = ofuscarCorreo(usuario.getEmail());
        redirect.addFlashAttribute("mensajeExito",
                "Se ha enviado una clave temporal de acceso al correo institucional: " + correoOculto +
                ". Verifique su bandeja de entrada (Clave de prueba generada: " + claveTemporal + ")");
        return "redirect:/login";
    }

    private String ofuscarCorreo(String correo) {
        if (correo == null || !correo.contains("@")) return correo;
        String[] partes = correo.split("@");
        String usuario = partes[0];
        if (usuario.length() <= 2) return correo;
        return usuario.charAt(0) + "***" + usuario.charAt(usuario.length() - 1) + "@" + partes[1];
    }
}
