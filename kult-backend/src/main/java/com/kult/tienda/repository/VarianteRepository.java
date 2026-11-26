package com.kult.tienda.repository;

import com.kult.tienda.model.Variante;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VarianteRepository extends JpaRepository<Variante, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Variante v join fetch v.producto where v.idVariante in :ids")
    List<Variante> findAllByIdForUpdate(@Param("ids") List<Integer> ids);
}
