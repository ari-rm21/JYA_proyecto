package com.JYA_proyecto.JYA_proyecto.dao;

import com.JYA_proyecto.JYA_proyecto.model.CarritoRegistro; 
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CarritoRegistroDao extends JpaRepository<CarritoRegistro, Long> {

    @Query("SELECT c FROM CarritoRegistro c JOIN FETCH c.producto p WHERE c.usuario.idUsuario = :uid")
    List<CarritoRegistro> findAllByUsuario(@Param("uid") Long usuarioId);

    @Query("SELECT c FROM CarritoRegistro c WHERE c.usuario.idUsuario = :uid AND c.producto.id = :pid")
    Optional<CarritoRegistro> findRow(@Param("uid") Long usuarioId, @Param("pid") Long productoId);

    @Modifying @Transactional
    @Query("DELETE FROM CarritoRegistro c WHERE c.usuario.idUsuario = :uid AND c.producto.id = :pid")
    void deleteRow(@Param("uid") Long usuarioId, @Param("pid") Long productoId);

    @Modifying @Transactional
    @Query("DELETE FROM CarritoRegistro c WHERE c.usuario.idUsuario = :uid")
    void deleteAllForUsuario(@Param("uid") Long usuarioId);

    @Query("SELECT COALESCE(SUM(c.cantidad),0) FROM CarritoRegistro c WHERE c.usuario.idUsuario = :uid")
    Integer totalCantidad(@Param("uid") Long usuarioId);
}
