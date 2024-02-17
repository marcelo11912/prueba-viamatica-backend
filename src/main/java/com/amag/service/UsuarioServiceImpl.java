package com.amag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amag.model.Usuario;
import com.amag.repository.IUsuarioRepository;
import com.amag.utilis.CorreoUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
	@Autowired
	private IUsuarioRepository usuarioRepository;

	private CorreoUtil correoUtil = new CorreoUtil();

	@Override
	public List<Usuario> getAllUsers() {
		return (List<Usuario>) usuarioRepository.findAll();
	}

	@Override
	public Usuario getUserById(Long id) {
		return usuarioRepository.findById(id).orElse(null);

	}

	@Override
	public Usuario createUser(Usuario usuario) {
		String mail = correoUtil.generarCorreo(usuario.getName(), usuario.getLastName());
		// Verificar y modificar si el correo ya existe
		int contador = 0;
		String correoOriginal = mail;
		while (usuarioRepository.findByEmail(mail) != null) {
			contador++;
			mail = correoOriginal.split("@")[0] + contador + "@" + correoOriginal.split("@")[1];
		}
		usuario.setMail(mail);

		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario updateUser(Long id, Usuario usuarioDetalles) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		if (usuario != null) {
			usuario.setName(usuarioDetalles.getName());
			usuario.setLastName(usuarioDetalles.getLastName());
			usuario.setBirthDate(usuarioDetalles.getBirthDate());
			return usuarioRepository.save(usuario);

		} else {
			throw new EntityNotFoundException("El empleado con ID " + id + " no existe");

		}
	}

	@Override
	public void deleteUser(Long id) {
		usuarioRepository.deleteById(id);

	}

}
