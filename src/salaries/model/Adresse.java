package salaries.model;

import salaries.utils.OutilsChaine;

public class Adresse {

	private String numero;
	private String nomVoie;
	private String codePostal;
	private String ville;

	public Adresse(String numero, String nomVoie,
			String codePostal, String ville) {

		this.numero = numero;
		this.nomVoie = OutilsChaine.toNomPropre(nomVoie);
		this.codePostal = codePostal;
		this.ville = ville.toUpperCase();
	}

	public Adresse() {

		numero = nomVoie = codePostal = ville = "NR";
	}

	public void setAll(String numero, String nomVoie,
			String codePostal, String ville) {

		this.numero = numero;
		this.nomVoie = OutilsChaine.toNomPropre(nomVoie);
		this.codePostal = codePostal;
		this.ville = ville.toUpperCase();
	}

	public String getNumero() {

		return numero.equals("NR") ? "" : numero;
	}

	public String getNomVoie() {

		return nomVoie.equals("NR") ? "" : nomVoie;
	}

	public String getCodePostal() {

		return codePostal.equals("NR") ? "" : codePostal;
	}

	public String getVille() {

		return ville.equals("NR") ? "" : ville;
	}

	public String toString() {

		return String.format("%s %s%n%s %s%n", getNumero(),
				getNomVoie(), getCodePostal(), getVille());
	}
}
