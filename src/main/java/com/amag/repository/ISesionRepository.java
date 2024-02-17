package com.amag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amag.model.Sesion;


public interface ISesionRepository extends JpaRepository<Sesion, Long> {
	@Query(value = "SELECT * FROM sesiones WHERE id_usuario = :usuarioId ORDER BY fecha_ingreso DESC LIMIT 1", nativeQuery = true)
    Sesion findUltimaSesionPorUsuario(Long usuarioId);

}
