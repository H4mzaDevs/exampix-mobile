package com.ecampix.entities;

import java.util.Date;

public class Commande {

    private int id;
    private int prix;
    private int total;
    private int numero;
    private int idUser;
    private Date date;
    private Produit produit;

    public Commande(int id, int prix, int total, int numero, int idUser, Date date, Produit produit) {
        this.id = id;
        this.prix = prix;
        this.total = total;
        this.numero = numero;
        this.idUser = idUser;
        this.date = date;
        this.produit = produit;
    }

    public Commande(int prix, int total, int numero, int idUser, Date date, Produit produit) {
        this.prix = prix;
        this.total = total;
        this.numero = numero;
        this.idUser = idUser;
        this.date = date;
        this.produit = produit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
}
