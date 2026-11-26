package com.kult.tienda.service;

import com.kult.tienda.model.Sede;
import com.kult.tienda.repository.SedeRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class SedeService {
    private final SedeRepository sedes;

    public SedeService(SedeRepository sedes) { this.sedes = sedes; }

    @Transactional(readOnly = true)
    public List<Sede> listar() { return sedes.findAll(); }

    @Transactional(readOnly = true)
    public Sede obtener(Integer id) {
        return sedes.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sede no encontrada"));
    }

    public Sede crear(Sede sede) { validar(sede); sede.setIdSede(null); if (sede.getActiva() == null) sede.setActiva(true); return sedes.save(sede); }

    public Sede actualizar(Integer id, Sede entrada) {
        validar(entrada);
        Sede actual = obtener(id);
        actual.setNombre(entrada.getNombre().trim());
        actual.setCiudad(entrada.getCiudad().trim());
        actual.setDireccion(entrada.getDireccion().trim());
        actual.setActiva(entrada.getActiva() == null ? true : entrada.getActiva());
        return sedes.save(actual);
    }

    public void eliminar(Integer id) { sedes.delete(obtener(id)); }

    private void validar(Sede sede) {
        if (sede == null || sede.getNombre() == null || sede.getNombre().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la sede es obligatorio");
        if (sede.getCiudad() == null || sede.getCiudad().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ciudad es obligatoria");
        if (sede.getDireccion() == null || sede.getDireccion().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La dirección es obligatoria");
    }
}
