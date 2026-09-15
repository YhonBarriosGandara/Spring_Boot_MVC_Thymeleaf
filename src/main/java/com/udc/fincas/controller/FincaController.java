package com.udc.fincas.controller;

import com.udc.fincas.entity.Finca;
import com.udc.fincas.service.FincaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/fincas")
public class FincaController {

    private final FincaService fincaService;

    public FincaController(FincaService fincaService) {
        this.fincaService = fincaService;
    }

    @GetMapping
    public String listar(@RequestParam(value = "busqueda", required = false) String busqueda, Model model) {
        List<Finca> fincas;
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            fincas = fincaService.buscarPorTermino(busqueda.trim());
            model.addAttribute("busqueda", busqueda.trim());
        } else {
            fincas = fincaService.listarTodas();
        }
        model.addAttribute("fincas", fincas);
        return "finca/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Finca finca = new Finca();
        finca.setPais("Colombia");
        model.addAttribute("finca", finca);
        model.addAttribute("esEdicion", false);
        return "finca/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
        Optional<Finca> fincaOpt = fincaService.buscarPorId(id);
        if (fincaOpt.isEmpty()) {
            redirect.addFlashAttribute("mensajeError", "La finca solicitada (ID: " + id + ") no existe.");
            return "redirect:/fincas";
        }
        model.addAttribute("finca", fincaOpt.get());
        model.addAttribute("esEdicion", true);
        return "finca/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("finca") Finca finca,
                          BindingResult result,
                          @RequestParam(value = "esEdicion", defaultValue = "false") boolean esEdicion,
                          Model model,
                          RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("esEdicion", esEdicion);
            return "finca/form";
        }

        fincaService.guardar(finca);
        String mensaje = esEdicion ? "Finca actualizada exitosamente." : "Finca registrada exitosamente.";
        redirect.addFlashAttribute("mensajeExito", mensaje);
        return "redirect:/fincas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirect) {
        if (!fincaService.existePorId(id)) {
            redirect.addFlashAttribute("mensajeError", "No se puede eliminar: la finca no existe.");
            return "redirect:/fincas";
        }
        fincaService.eliminar(id);
        redirect.addFlashAttribute("mensajeExito", "Finca eliminada exitosamente.");
        return "redirect:/fincas";
    }
}
