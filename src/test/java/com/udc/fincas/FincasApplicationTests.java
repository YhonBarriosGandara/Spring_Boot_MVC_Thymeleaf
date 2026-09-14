package com.udc.fincas;

import com.udc.fincas.entity.Finca;
import com.udc.fincas.entity.Usuario;
import com.udc.fincas.service.FincaService;
import com.udc.fincas.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FincasApplicationTests {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FincaService fincaService;

    @Test
    void contextLoads() {
        assertNotNull(usuarioService);
        assertNotNull(fincaService);
    }

    @Test
    void testServicioUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        assertFalse(usuarios.isEmpty(), "Debe haber usuarios registrados");
        assertTrue(usuarios.size() >= 4);

        Optional<Usuario> auth = usuarioService.autenticar("admin", "admin123");
        assertTrue(auth.isPresent(), "Autenticacion exitosa para admin");
        assertEquals("ADMINISTRADOR", auth.get().getRol());

        List<Usuario> admins = usuarioService.buscarPorRol("ADMINISTRADOR");
        assertFalse(admins.isEmpty());

        List<Usuario> coincidencias = usuarioService.buscarPorNombre("Rodriguez");
        assertFalse(coincidencias.isEmpty());
    }

    @Test
    void testServicioFincasYReportes() {
        List<Finca> fincas = fincaService.listarTodas();
        assertFalse(fincas.isEmpty(), "Debe haber fincas registradas");
        assertTrue(fincas.size() >= 7);

        // Reporte 1: Ubicacion y extension
        List<Finca> reporte1 = fincaService.filtrarPorUbicacionYExtension(
                "Antioquia", BigDecimal.valueOf(50), BigDecimal.valueOf(200));
        assertFalse(reporte1.isEmpty());
        assertEquals("Antioquia", reporte1.get(0).getDepartamento());

        // Reporte 2: Produccion de leche
        List<Finca> reporte2 = fincaService.filtrarPorProduccion(true, false, false, false);
        assertFalse(reporte2.isEmpty());
        assertTrue(reporte2.stream().allMatch(Finca::isProduceLeche));
    }
}
