package com.udc.fincas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "finca")
public class Finca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Atributo 1
    @NotBlank(message = "El nombre de la finca es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Atributo 2
    @NotNull(message = "El número de hectáreas es obligatorio")
    @DecimalMin(value = "0.01", message = "El número de hectáreas debe ser mayor a cero")
    @Column(name = "num_hectareas", nullable = false, precision = 10, scale = 2)
    private BigDecimal numHectareas;

    // Atributo 3
    @NotNull(message = "Los metros cuadrados son obligatorios")
    @DecimalMin(value = "1.00", message = "Los metros cuadrados deben ser mayores a cero")
    @Column(name = "metros_cuadrados", nullable = false, precision = 12, scale = 2)
    private BigDecimal metrosCuadrados;

    // Atributo 4
    @NotBlank(message = "El nombre del propietario es obligatorio")
    @Size(max = 100, message = "El nombre del propietario no puede exceder 100 caracteres")
    @Column(name = "propietario", nullable = false, length = 100)
    private String propietario;

    // Atributo 5
    @NotBlank(message = "El nombre del capataz es obligatorio")
    @Size(max = 100, message = "El nombre del capataz no puede exceder 100 caracteres")
    @Column(name = "capataz", nullable = false, length = 100)
    private String capataz;

    // Atributo 6
    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país no puede exceder 50 caracteres")
    @Column(name = "pais", nullable = false, length = 50)
    private String pais;

    // Atributo 7
    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 50, message = "El departamento no puede exceder 50 caracteres")
    @Column(name = "departamento", nullable = false, length = 50)
    private String departamento;

    // Atributo 8
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 50, message = "La ciudad no puede exceder 50 caracteres")
    @Column(name = "ciudad", nullable = false, length = 50)
    private String ciudad;

    // Atributo 9
    @Column(name = "produce_leche", nullable = false)
    private boolean produceLeche;

    // Atributo 10
    @Column(name = "produce_cereales", nullable = false)
    private boolean produceCereales;

    // Atributo 11
    @Column(name = "produce_frutas", nullable = false)
    private boolean produceFrutas;

    // Atributo 12
    @Column(name = "produce_verduras", nullable = false)
    private boolean produceVerduras;

    public Finca() {
    }

    public Finca(Long id, String nombre, BigDecimal numHectareas, BigDecimal metrosCuadrados,
                 String propietario, String capataz, String pais, String departamento, String ciudad,
                 boolean produceLeche, boolean produceCereales, boolean produceFrutas, boolean produceVerduras) {
        this.id = id;
        this.nombre = nombre;
        this.numHectareas = numHectareas;
        this.metrosCuadrados = metrosCuadrados;
        this.propietario = propietario;
        this.capataz = capataz;
        this.pais = pais;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.produceLeche = produceLeche;
        this.produceCereales = produceCereales;
        this.produceFrutas = produceFrutas;
        this.produceVerduras = produceVerduras;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getNumHectareas() {
        return numHectareas;
    }

    public void setNumHectareas(BigDecimal numHectareas) {
        this.numHectareas = numHectareas;
    }

    public BigDecimal getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(BigDecimal metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getCapataz() {
        return capataz;
    }

    public void setCapataz(String capataz) {
        this.capataz = capataz;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public boolean isProduceLeche() {
        return produceLeche;
    }

    public void setProduceLeche(boolean produceLeche) {
        this.produceLeche = produceLeche;
    }

    public boolean isProduceCereales() {
        return produceCereales;
    }

    public void setProduceCereales(boolean produceCereales) {
        this.produceCereales = produceCereales;
    }

    public boolean isProduceFrutas() {
        return produceFrutas;
    }

    public void setProduceFrutas(boolean produceFrutas) {
        this.produceFrutas = produceFrutas;
    }

    public boolean isProduceVerduras() {
        return produceVerduras;
    }

    public void setProduceVerduras(boolean produceVerduras) {
        this.produceVerduras = produceVerduras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Finca finca = (Finca) o;
        return Objects.equals(id, finca.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Finca{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", numHectareas=" + numHectareas +
                ", propietario='" + propietario + '\'' +
                ", departamento='" + departamento + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
