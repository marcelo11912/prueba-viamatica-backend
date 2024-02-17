package com.amag.service;

import java.util.List;

import com.amag.model.Usuario;



public interface IUsuarioService {
	
	public List<Usuario> getAllUsers();

	public Usuario getUserById(Long id);

	public Usuario createUser(Usuario usuario);

	public Usuario updateUser(Long id, Usuario usuarioDetalles);
	
	public void deleteUser(Long id);
}
