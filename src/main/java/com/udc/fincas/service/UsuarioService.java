package com.udc.fincas.service;

import com.udc.fincas.entity.Usuario;
import com.udc.fincas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return usuarioRepository.findById(id.trim());
    }

    @Transactional
    public Usuario guardar(Usuario usuario) {
        if (usuario.getId() != null) {
            usuario.setId(usuario.getId().trim());
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(String id) {
        if (id != null && usuarioRepository.existsById(id.trim())) {
            usuarioRepository.deleteById(id.trim());
        }
    }

    public boolean existePorId(String id) {
        return id != null && usuarioRepository.existsById(id.trim());
    }

    public Optional<Usuario> autenticar(String id, String clave) {
        if (id == null || clave == null) {
            return Optional.empty();
        }
        return usuarioRepository.findByIdAndClave(id.trim(), clave.trim());
    }

    public Optional<Usuario> buscarPorIdOCorreo(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return Optional.empty();
        }
        String valor = termino.trim();
        return usuarioRepository.findByIdOrEmail(valor, valor);
    }

    public List<Usuario> buscarPorRol(String rol) {
        if (rol == null || rol.trim().isEmpty()) {
            return listarTodos();
        }
        return usuarioRepository.findByRol(rol.trim().toUpperCase());
    }

    public List<Usuario> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return listarTodos();
        }
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }
}
