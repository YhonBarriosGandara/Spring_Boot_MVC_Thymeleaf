package com.udc.fincas.controller;

import com.udc.fincas.entity.Finca;
import com.udc.fincas.entity.Usuario;
import com.udc.fincas.service.FincaService;
import com.udc.fincas.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final FincaService fincaService;
    private final UsuarioService usuarioService;

    public ReporteController(FincaService fincaService, UsuarioService usuarioService) {
        this.fincaService = fincaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String reportesPrincipal(
            @RequestParam(value = "tipo", defaultValue = "fincas-extension") String tipo,
            // Parametros Reporte Finca 1: Ubicacion y Extension
            @RequestParam(value = "departamento", required = false) String departamento,
            @RequestParam(value = "minHectareas", required = false) BigDecimal minHectareas,
            @RequestParam(value = "maxHectareas", required = false) BigDecimal maxHectareas,
            // Parametros Reporte Finca 2: Lineas de Produccion
            @RequestParam(value = "produceLeche", defaultValue = "false") boolean produceLeche,
            @RequestParam(value = "produceCereales", defaultValue = "false") boolean produceCereales,
            @RequestParam(value = "produceFrutas", defaultValue = "false") boolean produceFrutas,
            @RequestParam(value = "produceVerduras", defaultValue = "false") boolean produceVerduras,
            // Parametros Reporte Usuario 1: Por Rol
            @RequestParam(value = "rol", required = false) String rol,
            // Parametros Reporte Usuario 2: Por Busqueda
            @RequestParam(value = "terminoUsuario", required = false) String terminoUsuario,
            Model model) {

        model.addAttribute("tipoActivo", tipo);

        // 1. Reporte Finca 1: Ubicacion y Extension
        List<Finca> fincasPorExtension = fincaService.filtrarPorUbicacionYExtension(departamento, minHectareas, maxHectareas);
        model.addAttribute("fincasExtension", fincasPorExtension);
        model.addAttribute("departamentoFiltro", departamento);
        model.addAttribute("minHectareasFiltro", minHectareas);
        model.addAttribute("maxHectareasFiltro", maxHectareas);

        // 2. Reporte Finca 2: Lineas de Produccion
        List<Finca> fincasPorProduccion = fincaService.filtrarPorProduccion(produceLeche, produceCereales, produceFrutas, produceVerduras);
        model.addAttribute("fincasProduccion", fincasPorProduccion);
        model.addAttribute("filtroLeche", produceLeche);
        model.addAttribute("filtroCereales", produceCereales);
        model.addAttribute("filtroFrutas", produceFrutas);
        model.addAttribute("filtroVerduras", produceVerduras);

        // 3. Reporte Usuario 1: Por Rol
        List<Usuario> usuariosPorRol = usuarioService.buscarPorRol(rol);
        model.addAttribute("usuariosRol", usuariosPorRol);
        model.addAttribute("rolFiltro", rol);

        // 4. Reporte Usuario 2: Por Busqueda de Nombre o Correo
        List<Usuario> usuariosPorTermino = usuarioService.buscarPorNombre(terminoUsuario);
        model.addAttribute("usuariosTermino", usuariosPorTermino);
        model.addAttribute("terminoUsuarioFiltro", terminoUsuario);

        return "reportes/index";
    }
}
