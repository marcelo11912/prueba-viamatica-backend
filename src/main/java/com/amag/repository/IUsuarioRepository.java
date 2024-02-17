package com.amag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amag.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
	@Query(value = "SELECT r.rol_name " + "FROM usuarios u " + "JOIN roles_usuarios ru ON u.id_usuario = ru.id_usuario "
			+ "JOIN roles r ON ru.id_rol = r.id_rol " + "WHERE u.id_usuario = :idUsuario", nativeQuery = true)
	List<String> findRolesByUserId(Long idUsuario);

	@Query(value = "SELECT * FROM usuarios WHERE usuarios.mail = :mail", nativeQuery = true)
	Usuario findByEmail(String mail);

	@Query(value = "SELECT * FROM usuarios WHERE usuarios.user_name = :user", nativeQuery = true)
	Usuario findByUser(String user);

}
