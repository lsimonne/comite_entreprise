package salaries.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import salaries.utils.FrenchDate;

public class Salarie extends Personne {

	private Agence agence;
	private LocalDate dateEmbauche;
	private StringProperty agenceProperty;
	private StringProperty dateEmbaucheProperty;

	public Salarie(Personne personne) {

		super(personne.getPrenom(), personne.getNom(), personne.getSexe(),
				personne.getDateDeNaissance(), personne.getTelephone(),
				personne.getAdresse());
		agence = new Agence("", false);
		dateEmbauche = LocalDate.now();
		agenceProperty = new SimpleStringProperty();
		dateEmbaucheProperty = new SimpleStringProperty();
	}

	public Salarie() {

	}
	
	public void setInfosEntreprise(Agence agence, LocalDate dateEmbauche) {

		this.agence = agence;
		this.dateEmbauche = dateEmbauche;
		agenceProperty.set(this.agence.getNom());
		try {
			dateEmbaucheProperty
					.set(FrenchDate.dateHeureToString(this.dateEmbauche));
		} catch (Exception e) {
			dateEmbaucheProperty.set("");
		}
	}

	public int calculerChequeNoel() {

		int montant = 0;
		for (Personne enfant : super.enfants) {
			int age = enfant
					.getAge(LocalDate.of(LocalDate.now().getYear(), 12, 31));
			if (age <= 10)
				montant += 20;
			else if (age <= 15)
				montant += 30;
			else if (age <= 18)
				montant += 50;
		}
		return montant;
	}

	@Override
	public String toString() {

		return "------SALARIE------\n" + super.toString() + "AGENCE: "
				+ agence.getNom() + ", ARRIVEE LE "
				+ FrenchDate.dateHeureToString(dateEmbauche) + "-----------\n";
	}
	
	public StringProperty getChequeNoelProperty() {

		return new SimpleStringProperty(Integer.toString(calculerChequeNoel()));
	}

	public Agence getAgence() {

		return agence;
	}

	public LocalDate getDateEmbauche() {

		return dateEmbauche;
	}

	public StringProperty getDateEmbaucheProperty() {

		return dateEmbaucheProperty;
	}
	
	public boolean hasChequeVacances() {

		LocalDate date = LocalDate.of(LocalDate.now().getYear(), 7, 1);
		int mois;
		mois = dateEmbauche.until(date).getMonths()
				+ dateEmbauche.until(date).getYears() * 12;
		return (mois >= 9 ? true : false);
	}

	public StringProperty getAgenceProperty() {

		return agenceProperty;
	}
}
