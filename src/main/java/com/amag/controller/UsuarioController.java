package com.amag.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amag.exeption.MensajeError;
import com.amag.model.Rol;
import com.amag.model.Sesion;
import com.amag.model.Usuario;
import com.amag.repository.IRolRepository;
import com.amag.repository.ISesionRepository;
import com.amag.repository.IUsuarioRepository;
import com.amag.service.IUsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
	
    private Map<String, Integer> failedAttempts = new HashMap<>(); // Correo o nombre de usuario -> Intentos fallidos

	@Autowired
	private IUsuarioRepository usuarioRepository;
	@Autowired
	private ISesionRepository sesionRepository;
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IRolRepository rolRepository;

	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> getAll() {
		List<Usuario> usuarios = usuarioService.getAllUsers();
		return ResponseEntity.ok(usuarios);

	}

	@PostMapping("/registrar")
	public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario,BindingResult result) {
		try {
			if (usuarioRepository.findByUser(usuario.getUserName()) != null) {
				throw new IllegalArgumentException("El nombre de usuario ya está en uso");
			}
			Set<Rol> roles = new HashSet<>();
		    for (Rol rol : usuario.getRoles()) {
		        Optional<Rol> rolOptional = rolRepository.findById(rol.getIdRol());
		        rolOptional.ifPresent(roles::add);
		    }
		    usuario.setRoles(roles);
			usuarioService.createUser(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensajeError(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensajeError(e.getMessage()));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {

		String emailOrUsuario = loginData.get("emailOrUsuario");
		String contrasena = loginData.get("contrasena");
		Usuario usuario = new Usuario();

		if (usuarioRepository.findByUser(emailOrUsuario) != null) {
			usuario = usuarioRepository.findByUser(emailOrUsuario);
		} else {
			usuario = usuarioRepository.findByEmail(emailOrUsuario);
		}
		
		if (usuario == null) {
			return ResponseEntity.badRequest().body("Usuario no encontrado.");
		}
		
        if (usuario.getStatus() !=null && usuario.getStatus().equalsIgnoreCase("BLOQUEADO")) {
            return ResponseEntity.badRequest().body("El usuario actuamente esta bloqueado");
        }
        
		// Verificar la contraseña
        int attempts = usuarioRepository.findFailedAttemptsByUser(usuario.getIdUsuario());
        if (!usuario.getPassword().equals(contrasena)) {
        	usuario.setIntentosFallidos(attempts+1);
            usuarioRepository.save(usuario);
            if (attempts + 1 >= 3) {
            	usuario.setStatus("BLOQUEADO");
                usuarioRepository.save(usuario);
                return ResponseEntity.badRequest().body("Usuario bloqueados.");
            }
            return ResponseEntity.badRequest().body("Contraseña incorrecta.");
        }
        Sesion sesion = new Sesion();
        sesion.setUsuario(usuario);
        sesion.setFechaIngreso(LocalDateTime.now());
        sesionRepository.save(sesion);

        //1 activa, 0 no activa
        usuario.setSessionActive("1");
        usuarioRepository.save(usuario);  
        
        // Obtener el nombre y rol del usuario
        long idUsuario = usuario.getIdUsuario();
        String nombre = usuario.getName();
        String apellidos = usuario.getLastName();
        List<String> roles = usuarioRepository.findRolesByUserId(usuario.getIdUsuario());

        Map<String, Object> response = new HashMap<>();
        response.put("idUsuario", idUsuario);
        response.put("emailOrUsuario", emailOrUsuario);
        response.put("nombre", nombre);
        response.put("apellidos", apellidos);
        response.put("roles", roles);
        response.put("message", "Inicio de sesión exitoso.");
        
        return ResponseEntity.ok(response);

	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody Map<String, String> loginData){
		String idUsuario = loginData.get("idUsuario");
		Usuario usuario = new Usuario();
		usuario = usuarioService.getUserById(Long.parseLong(idUsuario));
		if (usuario == null) {
			return ResponseEntity.badRequest().body("Usuario no encontrado.");
		}
		
		Sesion sesion = sesionRepository.findUltimaSesionPorUsuario(usuario.getIdUsuario());
        if (sesion != null) {
            sesion.setFechaCierre(LocalDateTime.now());
            sesionRepository.save(sesion);
        }
        //1 activa, 0 no activa
        usuario.setSessionActive("0");
        usuarioRepository.save(usuario);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cerraste Sesion con exito!");
        
        return ResponseEntity.ok(response);
	}
	
	@PostMapping("/detalles-sesion")
	public ResponseEntity<?> detallesSesion(@RequestBody Map<String, String> loginData){
		String idUsuario = loginData.get("idUsuario");
		String emailOrUsuario = loginData.get("emailOrUsuario");

		Usuario usuario = new Usuario();
		usuario = usuarioService.getUserById(Long.parseLong(idUsuario));
		if (usuario == null) {
			return ResponseEntity.badRequest().body("Usuario no encontrado.");
		}

		Sesion sesion = sesionRepository.findAntePenultimaSesionPorUsuario(usuario.getIdUsuario());
        int attempts = usuarioRepository.findFailedAttemptsByUser(usuario.getIdUsuario());
        Map<String, Object> response = new HashMap<>();
        response.put("fechaInicio", sesion.getFechaIngreso());
        response.put("fechaFinalizacion", sesion.getFechaCierre());
        response.put("numeroIntentos", attempts);
        
        return ResponseEntity.ok(response);
	}
	
	@PutMapping("actualizar/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Usuario usuario) {
		Usuario usuarioUpdated = usuarioService.updateUser(id, usuario);
		return ResponseEntity.ok(usuarioUpdated);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(usuarioService.getUserById(id));
	}
	
}
