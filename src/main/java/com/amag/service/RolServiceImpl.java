package com.amag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amag.model.Rol;
import com.amag.repository.IRolRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RolServiceImpl implements IRolService {
	
	@Autowired
	private IRolRepository rolrepository ;
	
	@Override
	public List<Rol> getAllRoles() {
		return (List<Rol>) rolrepository.findAll();

	}

	@Override
	public Rol getRolById(Long id) {
		return rolrepository.findById(id).orElse(null);

	}

	@Override
	public Rol createRol(Rol rol) {
		return rolrepository.save(rol);

	}

	@Override
	public Rol updateRol(Long id, Rol rolDetalles) {
		Rol rol = rolrepository.findById(id).orElse(null);
		if (rol != null) {
			rol.setRolName(rol.getRolName());
			return rolrepository.save(rol);
		} else {
			throw new EntityNotFoundException("El empleado con ID " + id + " no existe");
		}
	}

	@Override
	public void deleteRol(Long id) {
		rolrepository.deleteById(id);

		
	}
	
}
