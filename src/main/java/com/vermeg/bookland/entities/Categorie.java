package com.vermeg.bookland.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
@Entity
public class Categorie {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NotBlank(message = "Name is mandatory")
	@Column(name = "nom")
	private String nom;
	@NotBlank(message = "Description is mandatory")
	@Column(name = "description")
	private String description;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Categorie(long id, @NotBlank(message = "Name is mandatory") String nom,
			@NotBlank(message = "Description is mandatory") String description) {
		super();
		this.id = id;
		this.nom = nom;
		this.description = description;
	}
	public Categorie() {
		super();
	}
	
}
