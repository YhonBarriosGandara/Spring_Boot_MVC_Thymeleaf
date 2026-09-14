package com.udc.fincas.repository;

import com.udc.fincas.entity.Finca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FincaRepository extends JpaRepository<Finca, Long> {

    /**
     * Reporte 1: Fincas filtradas por departamento y rango de hectareas.
     */
    List<Finca> findByDepartamentoIgnoreCaseAndNumHectareasBetween(String departamento, BigDecimal minHectareas, BigDecimal maxHectareas);

    List<Finca> findByDepartamentoIgnoreCase(String departamento);

    List<Finca> findByNumHectareasBetween(BigDecimal minHectareas, BigDecimal maxHectareas);

    /**
     * Reporte 2: Consultas especificas por linea de produccion agropecuaria.
     */
    List<Finca> findByProduceLecheTrue();

    List<Finca> findByProduceCerealesTrue();

    List<Finca> findByProduceFrutasTrue();

    List<Finca> findByProduceVerdurasTrue();

    /**
     * Consulta parametrizada multi-criterio para lineas de produccion.
     */
    @Query("SELECT f FROM Finca f WHERE " +
           "(:leche = false OR f.produceLeche = true) AND " +
           "(:cereales = false OR f.produceCereales = true) AND " +
           "(:frutas = false OR f.produceFrutas = true) AND " +
           "(:verduras = false OR f.produceVerduras = true)")
    List<Finca> findByLineasProduccion(@Param("leche") boolean leche,
                                       @Param("cereales") boolean cereales,
                                       @Param("frutas") boolean frutas,
                                       @Param("verduras") boolean verduras);
}
