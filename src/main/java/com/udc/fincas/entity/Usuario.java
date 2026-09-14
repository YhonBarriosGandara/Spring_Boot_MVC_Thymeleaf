package com.udc.fincas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotBlank(message = "El ID de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El ID de usuario debe tener entre 3 y 50 caracteres")
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 4, max = 100, message = "La contraseña debe tener al menos 4 caracteres")
    @Column(name = "clave", nullable = false, length = 100)
    private String clave;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "Debe asignar un rol de usuario")
    @Column(name = "rol", nullable = false, length = 30)
    private String rol;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe proporcionar una dirección de correo válida")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    public Usuario() {
    }

    public Usuario(String id, String clave, String nombre, String rol, String email) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.rol = rol;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rol='" + rol + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
