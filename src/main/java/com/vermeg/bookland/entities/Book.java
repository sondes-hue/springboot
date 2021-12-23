package com.vermeg.bookland.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NotBlank(message = "Title is mandatory")
	private String title;
	@NotBlank(message = "Author is mandatory")
	private String author;
	@Min(value = 1, message = "Price is mandatory")
	private float price;
	@Size(max = 10,min = 10, message = "Please enter a valid date")
	private String pdate;
	@Min(value = 1, message = "Quantity is mandatory")
	private int qte;
	@NotBlank(message = "Description is mandatory")
	private String description;
	private String picture;

	@OneToMany(cascade=CascadeType.ALL, mappedBy = "book")
	private List<Ligne_commande> ligne_commandes;

	/**** Many To One ****/
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "categorie_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Categorie categorie;

	public Book(long id,
				@NotBlank(message = "Title is mandatory") String title,
				@NotBlank(message = "Author is mandatory") String author,
				@NotBlank(message = "Price is mandatory") float price,
				@Size(max = 10, min = 10, message = "Please enter a valid date") String pdate,
				@NotBlank(message = "Quantity is mandatory") int qte,
				@NotBlank(message = "Description is mandatory") String description,
				String picture,
				Categorie categorie) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.price = price;
		this.pdate = pdate;
		this.qte = qte;
		this.description = description;
		this.picture = picture;
		this.categorie = categorie;
	}
	
	public Book() {
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getPdate() {
		return pdate;
	}
	public void setPdate(String pdate) {
		this.pdate = pdate;
	}
	public int getQte() {
		return qte;
	}
	public void setQte(int qte) {
		this.qte = qte;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", price=" + price + ", pdate=" + pdate
				+ ", qte=" + qte + ", description=" + description + ", picture=" + picture + ", categorie=" + categorie
				+ "]";
	}
	
}
