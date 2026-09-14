package com.udc.fincas.repository;

import com.udc.fincas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    /**
     * Autenticacion por identificador y clave.
     */
    Optional<Usuario> findByIdAndClave(String id, String clave);

    /**
     * Busqueda por correo electronico.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busqueda para recuperacion de clave por ID o correo institucional.
     */
    Optional<Usuario> findByIdOrEmail(String id, String email);

    /**
     * Reporte 3: Usuarios filtrados por rol asignado.
     */
    List<Usuario> findByRol(String rol);

    /**
     * Reporte 4: Busqueda parametrizada por coincidencia parcial de nombre.
     */
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}
