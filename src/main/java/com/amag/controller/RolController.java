package com.amag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amag.model.Rol;
import com.amag.service.IRolService;


@RestController
@RequestMapping("api/rol")
public class RolController {

	@Autowired
	private IRolService rolService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(rolService.getAllRoles());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(rolService.getRolById(id));
	}

	@PostMapping("/crear")
	public ResponseEntity<?> crearRol(@RequestBody Rol rol) {
		return ResponseEntity.status(HttpStatus.CREATED).body(rolService.createRol(rol));
	}

	@PutMapping("actualizar/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Rol rol) {
		Rol rolUpdated = rolService.updateRol(id, rol);
		return ResponseEntity.ok(rolUpdated);
	}

}
