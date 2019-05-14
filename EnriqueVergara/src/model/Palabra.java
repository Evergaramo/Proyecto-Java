package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the palabras database table.
 * 
 */
@Entity
@Table(name="palabras")
@NamedQuery(name="Palabra.findAll", query="SELECT p FROM Palabra p")
public class Palabra implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String palabra;

	public Palabra() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPalabra() {
		return this.palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

}