package com.udc.fincas;

import com.udc.fincas.entity.Finca;
import com.udc.fincas.entity.Usuario;
import com.udc.fincas.service.FincaService;
import com.udc.fincas.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FincasApplicationTests {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FincaService fincaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertNotNull(usuarioService);
        assertNotNull(fincaService);
        assertNotNull(mockMvc);
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

    @Test
    void testAuthYControlAcceso() throws Exception {
        // 1. Pagina de login carga correctamente
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));

        // 2. Ruta protegida sin sesion redirige a /login
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // 3. Login con credenciales invalidas
        mockMvc.perform(post("/login")
                .param("id", "usuarioInexistente")
                .param("clave", "claveErronea"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attributeExists("mensajeError"));

        // 4. Login exitoso
        mockMvc.perform(post("/login")
                .param("id", "admin")
                .param("clave", "admin123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attributeExists("mensajeExito"));

        // 5. Con sesion activa se accede a rutas protegidas
        Usuario admin = usuarioService.buscarPorId("admin").orElseThrow();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioLogueado", admin);

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));

        // 6. Logout invalida sesion
        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testUsuarioController() throws Exception {
        Usuario admin = usuarioService.buscarPorId("admin").orElseThrow();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioLogueado", admin);

        mockMvc.perform(get("/usuarios").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario/listar"))
                .andExpect(model().attributeExists("usuarios"));

        mockMvc.perform(get("/usuarios/nuevo").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario/form"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attribute("esEdicion", false));
    }

    @Test
    void testFincaController() throws Exception {
        Usuario admin = usuarioService.buscarPorId("admin").orElseThrow();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioLogueado", admin);

        mockMvc.perform(get("/fincas").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("finca/listar"))
                .andExpect(model().attributeExists("fincas"));

        mockMvc.perform(get("/fincas/nuevo").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("finca/form"))
                .andExpect(model().attributeExists("finca"))
                .andExpect(model().attribute("esEdicion", false));

        mockMvc.perform(post("/fincas/guardar").session(session)
                .param("nombre", "Hacienda El Porvenir Test")
                .param("pais", "Colombia")
                .param("departamento", "Bolivar")
                .param("ciudad", "Turbaco")
                .param("numHectareas", "45.00")
                .param("metrosCuadrados", "450000.00")
                .param("propietario", "Yhon Barrios")
                .param("capataz", "Pedro Gomez")
                .param("produceLeche", "true")
                .param("produceCereales", "false")
                .param("produceFrutas", "true")
                .param("produceVerduras", "false")
                .param("esEdicion", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/fincas"))
                .andExpect(flash().attributeExists("mensajeExito"));
    }
}
