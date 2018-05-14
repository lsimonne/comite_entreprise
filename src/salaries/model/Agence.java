package salaries.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import salaries.utils.OutilsChaine;

public class Agence {

	private String nom;
	private boolean hasResto;
	private StringProperty nomProperty;

	public Agence(String nom, boolean hasResto) {

		this.nom = OutilsChaine.toNomPropre(nom);
		this.hasResto = hasResto;
		nomProperty = new SimpleStringProperty();
		nomProperty.set(this.nom);
	}

	public StringProperty getNomProperty() {

		return nomProperty;
	}

	public Agence() {

		this("", false);
	}

	public boolean hasResto() {

		return hasResto;
	}

	public String getNom() {

		return nom;
	}

	public String toString() {

		return (String.format("L'agence %s %s %s", nom,
				hasResto ? "a un" : "n'a pas de", "restaurant"));
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (hasResto ? 1231 : 1237);
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agence other = (Agence) obj;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}
}
