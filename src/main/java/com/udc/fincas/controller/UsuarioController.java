package com.udc.fincas.controller;

import com.udc.fincas.entity.Usuario;
import com.udc.fincas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(@RequestParam(value = "busqueda", required = false) String busqueda, Model model) {
        List<Usuario> usuarios;
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            usuarios = usuarioService.buscarPorNombre(busqueda.trim());
            model.addAttribute("busqueda", busqueda.trim());
        } else {
            usuarios = usuarioService.listarTodos();
        }
        model.addAttribute("usuarios", usuarios);
        return "usuario/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Usuario usuario = new Usuario();
        usuario.setRol("OPERADOR");
        model.addAttribute("usuario", usuario);
        model.addAttribute("esEdicion", false);
        return "usuario/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") String id, Model model, RedirectAttributes redirect) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            redirect.addFlashAttribute("mensajeError", "El usuario con identificador '" + id + "' no existe.");
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("esEdicion", true);
        return "usuario/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult result,
                          @RequestParam(value = "esEdicion", defaultValue = "false") boolean esEdicion,
                          Model model,
                          RedirectAttributes redirect) {
        if (!esEdicion && usuarioService.existePorId(usuario.getId())) {
            result.rejectValue("id", "error.usuario", "Ya existe un usuario con este identificador.");
        }

        if (result.hasErrors()) {
            model.addAttribute("esEdicion", esEdicion);
            return "usuario/form";
        }

        usuarioService.guardar(usuario);
        String mensaje = esEdicion ? "Usuario actualizado correctamente." : "Usuario registrado exitosamente.";
        redirect.addFlashAttribute("mensajeExito", mensaje);
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") String id, RedirectAttributes redirect) {
        if (!usuarioService.existePorId(id)) {
            redirect.addFlashAttribute("mensajeError", "No se puede eliminar: el usuario no existe.");
            return "redirect:/usuarios";
        }
        usuarioService.eliminar(id);
        redirect.addFlashAttribute("mensajeExito", "Usuario eliminado exitosamente.");
        return "redirect:/usuarios";
    }
}
