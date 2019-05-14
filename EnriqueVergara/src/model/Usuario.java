package model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the usuarios database table.
 * 
 */
@Entity
@Table(name="usuarios")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String correo;

	private String nombre;

	//bi-directional many-to-one association to Partida
	@OneToMany(mappedBy="usuarioBean")
	private List<Partida> partidas;

	public Usuario() {
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Partida> getPartidas() {
		return this.partidas;
	}

	public void setPartidas(List<Partida> partidas) {
		this.partidas = partidas;
	}

	public Partida addPartida(Partida partida) {
		getPartidas().add(partida);
		partida.setUsuarioBean(this);

		return partida;
	}

	public Partida removePartida(Partida partida) {
		getPartidas().remove(partida);
		partida.setUsuarioBean(null);

		return partida;
	}

}