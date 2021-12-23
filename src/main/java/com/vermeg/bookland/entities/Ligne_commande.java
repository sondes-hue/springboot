package com.vermeg.bookland.entities;

import java.util.Date;

import javax.persistence.*;
@Entity
public class Ligne_commande {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long id;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "book_id")
	    private Book book;
	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "commande_id")
	    private Commande commande;

	    private String commandeDate;
	    private int qte;
		public Ligne_commande(long id, Book book, Commande commande, String commandeDate, int qte) {
			super();
			this.id = id;
			this.book = book;
			this.commande = commande;
			this.commandeDate = commandeDate;
			this.qte = qte;
		}

		public int getQte() {
			return qte;
		}

		public void setQte(int qte) {
			this.qte = qte;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public Book getBook() {
			return book;
		}

		public void setBook(Book book) {
			this.book = book;
		}

		public Commande getCommande() {
			return commande;
		}

		public void setCommande(Commande commande) {
			this.commande = commande;
		}

		public String getCommandeDate() {
			return commandeDate;
		}

		public Ligne_commande() {
			super();
		}

		public void setCommandeDate(String commandeDate) {
			this.commandeDate = commandeDate;
		}
	    
}
