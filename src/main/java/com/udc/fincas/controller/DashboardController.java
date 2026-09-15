package com.udc.fincas.controller;

import com.udc.fincas.entity.Finca;
import com.udc.fincas.entity.Usuario;
import com.udc.fincas.service.FincaService;
import com.udc.fincas.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
public class DashboardController {

    private final FincaService fincaService;
    private final UsuarioService usuarioService;

    public DashboardController(FincaService fincaService, UsuarioService usuarioService) {
        this.fincaService = fincaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        List<Finca> fincas = fincaService.listarTodas();
        List<Usuario> usuarios = usuarioService.listarTodos();

        long totalFincas = fincas.size();
        long totalUsuarios = usuarios.size();

        BigDecimal totalHectareas = fincas.stream()
                .map(Finca::getNumHectareas)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long fincasLeche = fincas.stream().filter(Finca::isProduceLeche).count();
        long fincasCereales = fincas.stream().filter(Finca::isProduceCereales).count();
        long fincasFrutas = fincas.stream().filter(Finca::isProduceFrutas).count();
        long fincasVerduras = fincas.stream().filter(Finca::isProduceVerduras).count();

        // Obtener ultimas fincas registradas (hasta 5)
        List<Finca> ultimasFincas = fincas.stream()
                .sorted((a, b) -> Long.compare(b.getId() != null ? b.getId() : 0, a.getId() != null ? a.getId() : 0))
                .limit(5)
                .toList();

        model.addAttribute("totalFincas", totalFincas);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalHectareas", totalHectareas);
        model.addAttribute("fincasLeche", fincasLeche);
        model.addAttribute("fincasCereales", fincasCereales);
        model.addAttribute("fincasFrutas", fincasFrutas);
        model.addAttribute("fincasVerduras", fincasVerduras);
        model.addAttribute("ultimasFincas", ultimasFincas);

        return "dashboard";
    }
}
