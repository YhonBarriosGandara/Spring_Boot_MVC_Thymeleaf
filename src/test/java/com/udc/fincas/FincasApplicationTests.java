package com.udc.fincas;

import com.udc.fincas.entity.Finca;
import com.udc.fincas.entity.Usuario;
import com.udc.fincas.repository.FincaRepository;
import com.udc.fincas.repository.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FincaRepository fincaRepository;

    @Test
    void contextLoads() {
        assertNotNull(usuarioRepository);
        assertNotNull(fincaRepository);
    }

    @Test
    void testUsuariosEnBaseDeDatos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertFalse(usuarios.isEmpty(), "Debe haber usuarios precargados");
        assertTrue(usuarios.size() >= 4, "Debe haber al menos 4 usuarios");

        Optional<Usuario> admin = usuarioRepository.findByIdAndClave("admin", "admin123");
        assertTrue(admin.isPresent(), "El usuario admin debe autenticar correctamente");
        assertEquals("ADMINISTRADOR", admin.get().getRol());
    }

    @Test
    void testFincasEnBaseDeDatos() {
        List<Finca> fincas = fincaRepository.findAll();
        assertFalse(fincas.isEmpty(), "Debe haber fincas precargadas");
        assertTrue(fincas.size() >= 7, "Debe haber al menos 7 fincas con los 12 atributos");

        List<Finca> leche = fincaRepository.findByProduceLecheTrue();
        assertFalse(leche.isEmpty(), "Debe haber fincas que producen leche");

        List<Finca> antioquia = fincaRepository.findByDepartamentoIgnoreCaseAndNumHectareasBetween(
                "Antioquia", BigDecimal.valueOf(50), BigDecimal.valueOf(200));
        assertFalse(antioquia.isEmpty(), "Debe encontrar finca en Antioquia dentro del rango");
    }
}
