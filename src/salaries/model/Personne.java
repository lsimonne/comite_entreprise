package salaries.model;

import java.time.LocalDate;

import salaries.model.Adresse;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import salaries.utils.FrenchDate;
import salaries.utils.OutilsChaine;

public class Personne {

	protected String nom;
	protected String prenom;
	protected String sexe;
	protected LocalDate dateDeNaissance;
	private String telephone;
	private Adresse adresse;
	protected ObservableList<Personne> enfants;
	private StringProperty nomProperty;
	private StringProperty prenomProperty;
	private StringProperty dateNaissanceProperty;
	private StringProperty sexeProperty;
	private StringProperty adresseProperty;
	private StringProperty telephoneProperty;

	public Personne(String prenom, String nom, String sexe,
			LocalDate dateDeNaissance, String telephone, Adresse adresse) {

		initProperties();
		setAll(nom, prenom, sexe, dateDeNaissance, telephone, adresse);
		enfants = FXCollections.observableArrayList();
	}

	public Personne() {

		prenom = nom = sexe = telephone = "";
		adresse = new Adresse("", "", "", "");
		dateDeNaissance = LocalDate.now();
		enfants = FXCollections.observableArrayList();
		initProperties();
	}

	private void initProperties() {

		nomProperty = new SimpleStringProperty();
		prenomProperty = new SimpleStringProperty();
		dateNaissanceProperty = new SimpleStringProperty();
		sexeProperty = new SimpleStringProperty();
		adresseProperty = new SimpleStringProperty();
		telephoneProperty = new SimpleStringProperty();
	}

	public void setAll(String nom, String prenom, String sexe,
			LocalDate dateNaissance, String telephone, Adresse adresse) {

		setBasic(nom, prenom, sexe, dateNaissance);
		this.adresse = adresse;
		this.telephone = telephone;
		adresseProperty.set(this.adresse.toString());
		telephoneProperty.set(telephone);
	}
	
	public void setBasic(String nom, String prenom, String sexe, LocalDate dateDeNaissance) {
		this.nom = nom.toUpperCase();
		this.prenom = OutilsChaine.toNomPropre(prenom);
		this.sexe = sexe.toUpperCase();
		this.dateDeNaissance = dateDeNaissance;
		nomProperty.set(this.nom);
		prenomProperty.set(this.prenom);
		sexeProperty.set(this.sexe);
		try {
			dateNaissanceProperty
					.set(FrenchDate.dateHeureToString(this.dateDeNaissance));
		} catch (Exception e) {
			dateNaissanceProperty.set("");
		}
	}
	
	public void addEnfant(Personne enfant) {

		enfants.add(enfant);
	}
	
	public void removeEnfant(Personne enfant) {
		enfants.remove(enfant);
	}

	public int getAge(LocalDate date) {

		return dateDeNaissance.until(date).getYears();
	}
	
	public StringProperty getNomProperty() {

		return nomProperty;
	}

	public StringProperty getPrenomProperty() {

		return prenomProperty;
	}

	public StringProperty getDateDeNaissanceProperty() {

		return dateNaissanceProperty;
	}

	public StringProperty getSexeProperty() {

		return sexeProperty;
	}

	public StringProperty getAdresseProperty() {

		return adresseProperty;
	}

	public StringProperty getTelephoneProperty() {

		return telephoneProperty;
	}
	
	public String getNom() {

		return nom;
	}

	public String getPrenom() {

		return prenom;
	}

	public String getNomComplet() {

		return prenom + " " + nom;
	}

	public String getSexe() {

		return sexe;
	}

	public LocalDate getDateDeNaissance() {

		return dateDeNaissance;
	}

	public String getTelephone() {

		return telephone;
	}

	public Adresse getAdresse() {

		return adresse;
	}

	public ObservableList<Personne> getEnfants() {

		return enfants;
	}
	
	public String toString() {

		StringBuilder ret = new StringBuilder();
		ret.append(String.format("PRENOM: %s, NOM: %s, DATE DE NAISSANCE: %s",
				prenom, nom, FrenchDate.dateHeureToString(dateDeNaissance)));
		if (!enfants.isEmpty())
			ret.append("\nENFANTS:\n");
		for (Personne enfant : enfants) {
			ret.append(String.format("%s%n", enfant));
		}
		return ret.toString();
	}

}
