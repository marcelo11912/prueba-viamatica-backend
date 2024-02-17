package com.amag.service;

import java.util.List;

import com.amag.model.Rol;


public interface IRolService {

	public List<Rol> getAllRoles();

	public Rol getRolById(Long id);

	public Rol createRol(Rol rol);

	public Rol updateRol(Long id, Rol rolDetalles);
	
	public void deleteRol(Long id);
}
