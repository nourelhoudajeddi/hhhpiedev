package tn.greenly.entites;

import java.util.Date;

public class Formation {
    private int id;
    private String nomFormation;
    private String descriptionFormation;
    private int dureeFormation;
    private String modeFormation;
    private Date dateDebutFormation;
    private Date dateFinFormation;
   // private int module_id;
   private Module module;


    // Constructeur sans argument
    public Formation() {}

    // Constructeur avec tous les attributs
    public Formation(int id, String nomFormation, String descriptionFormation, int dureeFormation,
                     String modeFormation, Date dateDebutFormation, Date dateFinFormation, Module module) {
        this.id = id;
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
        this.dureeFormation = dureeFormation;
        this.modeFormation = modeFormation;
        this.dateDebutFormation = dateDebutFormation;
        this.dateFinFormation = dateFinFormation;
       // this.module_id = module.getId();
        this.module = module;


    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomFormation() {
        return nomFormation;
    }

    public void setNomFormation(String nomFormation) {
        this.nomFormation = nomFormation;
    }

    public String getDescriptionFormation() {
        return descriptionFormation;
    }

   /* public int getModule_id() {
        return module_id;
    }
    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }*/
   public Module getModule() {
       return module;
   }

    public void setModule(Module module) {
        this.module = module;
    }


    public void setDescriptionFormation(String descriptionFormation) {
        this.descriptionFormation = descriptionFormation;
    }

    public int getDureeFormation() {
        return dureeFormation;
    }

    public void setDureeFormation(int dureeFormation) {
        this.dureeFormation = dureeFormation;
    }

    public String getModeFormation() {
        return modeFormation;
    }

    public void setModeFormation(String modeFormation) {
        this.modeFormation = modeFormation;
    }

    public Date getDateDebutFormation() {
        return dateDebutFormation;
    }

    public void setDateDebutFormation(Date dateDebutFormation) {
        this.dateDebutFormation = dateDebutFormation;
    }

    public Date getDateFinFormation() {
        return dateFinFormation;
    }

    public void setDateFinFormation(Date dateFinFormation) {
        this.dateFinFormation = dateFinFormation;
    }
    public String getNomModule() {
        return module != null ? module.getNomModule() : "";
    }


    // Méthode de validation pour la date de fin
    public boolean validateDates() {
        if (dateDebutFormation != null && dateFinFormation != null) {
            return !dateFinFormation.before(dateDebutFormation);  // La date de fin doit être après la date de début
        }
        return true;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", nomFormation='" + nomFormation + '\'' +
                ", descriptionFormation='" + descriptionFormation + '\'' +
                ", dureeFormation=" + dureeFormation +
                ", modeFormation='" + modeFormation + '\'' +
                ", dateDebutFormation=" + dateDebutFormation +
                ", dateFinFormation=" + dateFinFormation +
               // ", module_id=" + module_id +
                ", module=" + (module != null ? module.getNomModule() : "null") +

                '}';
    }
}
