package com.udc.fincas.service;

import com.udc.fincas.entity.Finca;
import com.udc.fincas.repository.FincaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FincaService {

    private final FincaRepository fincaRepository;

    public FincaService(FincaRepository fincaRepository) {
        this.fincaRepository = fincaRepository;
    }

    public List<Finca> listarTodas() {
        return fincaRepository.findAll();
    }

    public Optional<Finca> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return fincaRepository.findById(id);
    }

    public List<Finca> buscarPorTermino(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return listarTodas();
        }
        String q = termino.trim();
        return fincaRepository.findByNombreContainingIgnoreCaseOrPropietarioContainingIgnoreCase(q, q);
    }

    @Transactional
    public Finca guardar(Finca finca) {
        // Logica de dominio: si metrosCuadrados es nulo o 0 y tiene hectareas, autocalcular (1 ha = 10,000 m2)
        if ((finca.getMetrosCuadrados() == null || finca.getMetrosCuadrados().compareTo(BigDecimal.ZERO) == 0)
                && finca.getNumHectareas() != null) {
            finca.setMetrosCuadrados(finca.getNumHectareas().multiply(BigDecimal.valueOf(10000)));
        }
        return fincaRepository.save(finca);
    }

    @Transactional
    public void eliminar(Long id) {
        if (id != null && fincaRepository.existsById(id)) {
            fincaRepository.deleteById(id);
        }
    }

    public boolean existePorId(Long id) {
        return id != null && fincaRepository.existsById(id);
    }

    public List<Finca> filtrarPorUbicacionYExtension(String departamento, BigDecimal minHectareas, BigDecimal maxHectareas) {
        boolean tieneDepto = departamento != null && !departamento.trim().isEmpty();
        boolean tieneMin = minHectareas != null;
        boolean tieneMax = maxHectareas != null;

        BigDecimal min = tieneMin ? minHectareas : BigDecimal.ZERO;
        BigDecimal max = tieneMax ? maxHectareas : BigDecimal.valueOf(9999999.99);

        if (tieneDepto) {
            return fincaRepository.findByDepartamentoIgnoreCaseAndNumHectareasBetween(departamento.trim(), min, max);
        } else if (tieneMin || tieneMax) {
            return fincaRepository.findByNumHectareasBetween(min, max);
        } else {
            return listarTodas();
        }
    }

    public List<Finca> filtrarPorProduccion(boolean leche, boolean cereales, boolean frutas, boolean verduras) {
        if (!leche && !cereales && !frutas && !verduras) {
            return listarTodas();
        }
        return fincaRepository.findByLineasProduccion(leche, cereales, frutas, verduras);
    }
}
