package com.gdc.model;

public class Cours {
	private int id;
    private String titre;
    private String description;
    private String niveau; 
    private int dureeHeures; 

    public Cours() {
    }

    public Cours(int id, String titre, String description, String niveau, int dureeHeures) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.niveau = niveau;
        this.dureeHeures = dureeHeures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    
    public int getDureeHeures() {
        return dureeHeures;
    }

    public void setDureeHeures(int dureeHeures) {
        this.dureeHeures = dureeHeures;
    }
    
    @Override
    public String toString() {
        return "Cours [id=" + id + ", titre=" + titre + ", niveau=" + niveau + "]";
    }
}
