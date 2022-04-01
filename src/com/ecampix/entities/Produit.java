package com.ecampix.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Produit {

    private int id;
    private String type;
    private String typeAction;
    private String disponibilite;
    private int prix;
    private Date dateDebut;
    private Date dateFin;
    private String image;

    public Produit(int id, String type, String typeAction, String disponibilite, int prix, Date dateDebut, Date dateFin, String image) {
        this.id = id;
        this.type = type;
        this.typeAction = typeAction;
        this.disponibilite = disponibilite;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.image = image;
    }

    public Produit(String type, String typeAction, String disponibilite, int prix, Date dateDebut, Date dateFin, String image) {
        this.type = type;
        this.typeAction = typeAction;
        this.disponibilite = disponibilite;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Produit : " +
                "type : " + type +
                "/ typeAction : " + typeAction +
                "/ disponibilite : " + disponibilite +
                "/ prix : " + prix +
                "/ dateDebut : " + new SimpleDateFormat("dd-MM-yyyy").format(dateDebut) +
                "/ dateFin : " + new SimpleDateFormat("dd-MM-yyyy").format(dateFin);
    }
}
