package com.vermeg.bookland.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Commande {
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private long id;
	 @ManyToOne( cascade = CascadeType.ALL )
	 @JoinColumn( name="idUser" )
	 private User user;
	 private String commandDate;
	 @OneToMany(cascade=CascadeType.ALL, mappedBy = "commande")  
	 private List<Ligne_commande> ligne_commandes; 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getCommandDate() {
		return commandDate;
	}
	public void setCommandDate(String commandDate) {
		this.commandDate = commandDate;
	}
	public Commande(long id, User user, String commandDate) {
		super();
		this.id = id;
		this.user = user;
		this.commandDate = commandDate;
	}
	public Commande() {
		super();
	}
	 
	 
}
