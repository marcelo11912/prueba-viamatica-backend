package com.amag.service;

import java.util.List;

import com.amag.model.Sesion;


public interface ISesionService {
	
	public List<Sesion> getAllSesion();

	public Sesion getSesionById(Long id);

	public Sesion createSesion(Sesion sesion);

	public Sesion updateSesion(Long id, Sesion sesionDetalles);
	
	public void deleteSesion(Long id);
}
