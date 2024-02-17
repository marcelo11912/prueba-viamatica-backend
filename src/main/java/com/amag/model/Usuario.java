package com.amag.model;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.amag.validation.NoFourConsecutiveDigits;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idUsuario;

	
	@Column(length = 60)
	@Pattern(regexp = "^[a-zA-Z0-9]*$")
	@NotBlank(message = "El nombre de usuario no puede estar vacio")
	@NotNull(message = "El nombre de usuario es requerido")
	private String name;

	@Column(length = 60)
	@NotBlank(message = "El apellido no puede estar vacio")
	@NotNull(message = "El apellido es requerido")
	private String lastName;

	@Column(length = 10)
	@NotBlank(message = "La identificacion no puede estar vacio")
	@NotNull(message = "La identificacion es requerido")
	@Pattern(regexp = "\\d{10}", message = "La identificación debe tener 10 dígitos")
    @NoFourConsecutiveDigits(message = "El número no puede tener cuatro dígitos iguales consecutivos.")
	private String identification;

	@NotNull(message = "La identificacion es requerido")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;

	@Column(name = "user_name", length = 50,unique = true)
	@NotBlank(message = "El nombre del usuario no puede estar vacio")
	@NotNull(message = "El nombre del usuario es requerido")
	private String userName;

	@Column(length = 50)
	@NotBlank(message = "La contraseña no puede estar vacio")
	@NotNull(message = "La contraseña es requerido")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>/?]).*$", message = "La contraseña no cumple con los requisitos")
	private String password;

	@Column(length = 120,unique = true)
	private String mail;

	@Column(name = "session_active", length = 1)
	private String sessionActive;

	@Column(length = 20)
	private String status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_usuarios", joinColumns = { @JoinColumn(name = "id_usuario") }, inverseJoinColumns = {
			@JoinColumn(name = "id_rol") })
	private Set<Rol> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getSessionActive() {
		return sessionActive;
	}

	public void setSessionActive(String sessionActive) {
		this.sessionActive = sessionActive;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	

}
