package tn.greenly.entites;

import java.util.Date;
public class Module {
    private int id;
    private String nomModule;
    private String descriptionModule;
    private int nbHeures;
    private String niveau;
    private String categorie;
    private boolean statut;
    private Date datecreationModule;

    // Constructeur sans argument
    public Module() {
    }

    // Constructeur avec tous les attributs
    public Module(int id, String nomModule, String descriptionModule, int nbHeures, String niveau, String categorie, boolean statut, Date datecreationModule) {
        this.id = id;
        this.nomModule = nomModule;
        this.descriptionModule = descriptionModule;
        this.nbHeures = nbHeures;
        this.niveau = niveau;
        this.categorie = categorie;
        this.statut = statut;
        this.datecreationModule = datecreationModule;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomModule() {
        return nomModule;
    }

    public void setNomModule(String nomModule) {
        this.nomModule = nomModule;
    }

    public String getDescriptionModule() {
        return descriptionModule;
    }

    public void setDescriptionModule(String descriptionModule) {
        this.descriptionModule = descriptionModule;
    }

    public int getNbHeures() {
        return nbHeures;
    }

    public void setNbHeures(int nbHeures) {
        this.nbHeures = nbHeures;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public Date getDatecreationModule() {
        return datecreationModule;
    }

    public void setDatecreationModule(Date datecreationModule) {
        this.datecreationModule = datecreationModule;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", nomModule='" + nomModule + '\'' +
                ", descriptionModule='" + descriptionModule + '\'' +
                ", nbHeures=" + nbHeures +
                ", niveau='" + niveau + '\'' +
                ", categorie='" + categorie + '\'' +
                ", statut=" + statut +
                ", datecreationModule=" + datecreationModule +
                '}';
    }
}
