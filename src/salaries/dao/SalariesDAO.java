package salaries.dao;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javafx.collections.ObservableList;
import salaries.utils.FrenchDate;
import salaries.dao.FichierTexte;
import salaries.model.Agence;
import salaries.model.Personne;
import salaries.model.Salarie;
import salaries.model.Adresse;

public class SalariesDAO {

	private FichierTexte fichier;
	private static final String CODE_SALARIE = "Salarie";

	public SalariesDAO(File file) {

		this.fichier = new FichierTexte(file);
	}

	public List<Salarie> lire() {

		List<Salarie> listSalaries = new ArrayList<>();
		List<String> lignes = fichier.lire();
		ListIterator<String> iterator = lignes.listIterator();
		while (iterator.hasNext()) {
			Salarie salarie = stringToSalarie(iterator.next());
			getEnfants(salarie, iterator);
			listSalaries.add(salarie);
		}
		return listSalaries;
	}

	/**
	 * Parse les lignes suivant une ligne "Salari�" et ajoute les enfants
	 * jusqu'� rencontrer une ligne salari� ou la fin de la liste
	 * 
	 * @param salarie
	 *            Le salari� auquel on va ajouter des enfants
	 * @param iterator
	 *            l'it�rateur de la liste de lignes, � la position du premier
	 *            enfant
	 */
	private void getEnfants(Salarie salarie, ListIterator<String> iterator) {

		String next;
		boolean isEnfant = true;
		while (iterator.hasNext() && isEnfant) {
			next = iterator.next();
			if (!next.startsWith(CODE_SALARIE)) {
				salarie.addEnfant(stringToPersonne(next, true));
			} else {
				iterator.previous();
				isEnfant = false;
			}
		}
	}

	/**
	 * Parse une ligne "Salari�", en commen�ant par les champs de Personne (nom,
	 * pr�nom, sexe, date de naissance, t�l�phone, adresse), et compl�te par les
	 * champs relatifs � l'entreprise (agence, date d'embauche)
	 * 
	 * @param ligne
	 *            La ligne � parser
	 * @return un nouveau Salari� avec toutes les informations de la ligne
	 */
	private Salarie stringToSalarie(String ligne) {

		List<String> parse = Arrays.asList(ligne.split("\\|"));
		Salarie salarie = personneToSalarie(stringToPersonne(ligne, false));
		salarie.setInfosEntreprise(
				new Agence(parse.get(5), stringToBoolean(parse.get(6))),
				FrenchDate.stringToDate(parse.get(7)));
		return salarie;
	}

	/**
	 * Parse une String pour renvoyer une Personne
	 * 
	 * @param personne
	 *            La personne sous forme de String
	 * @param isEnfant
	 *            Si vrai, la ligne ne comporte pas de nom, donc les champs sont
	 *            d�cal�s d'un index vers la gauche apr�s le pr�nom
	 * @return Une nouvelle personne
	 */
	private Personne stringToPersonne(String personne, boolean isEnfant) {

		List<String> parse = Arrays.asList(personne.split("\\|"));
		String nom = "";
		int i = 1;
		// handle file errors?
		String prenom = parse.get(i++);
		if (!isEnfant) {
			nom = parse.size() > i ? parse.get(i) : "";
			i++;
		}
		String sexe = parse.size() > i ? parse.get(i) : "";
		i++;
		LocalDate dateDeNaissance = parse.size() > i
				? FrenchDate.stringToDate(parse.get(i))
				: null;
		String telephone = parse.size() > 8 ? parse.get(8) : "";
		return new Personne(prenom, nom, sexe, dateDeNaissance, telephone,
				strToAdresse(parse));
	}

	/**
	 * Convertit les trois champs correspondant � l'adresse en Adresse
	 * 
	 * @param parse
	 *            La ligne � parser
	 * @return La nouvelle Adresse
	 */
	private Adresse strToAdresse(List<String> parse) {

		if (parse.size() > 10) {
			List<String> parseAdresse = Arrays.asList(parse.get(9).split(" "));
			StringBuilder nomVoie = new StringBuilder();
			for (int i = 1; i < parseAdresse.size(); i++) {
				nomVoie.append(parseAdresse.get(i) + " ");
			}
			return new Adresse(parseAdresse.get(0), nomVoie.toString(),
					parse.get(10), parse.get(11));
		}
		return new Adresse();
	}

	/**
	 * Cr�e un Salari� � partir d'une Personne
	 * 
	 * @param personne
	 *            La Personne � convertir
	 * @return Un nouveau Salari� qui correspond � la Personne
	 */
	private Salarie personneToSalarie(Personne personne) {

		if (personne == null)
			return null;
		return new Salarie(personne);
	}

	/**
	 * Convertit les String "TRUE" et "FALSE" en booleen
	 * 
	 * @param bool
	 *            La String � convertir
	 * @return Le bool�en correspondant � bool
	 */
	private boolean stringToBoolean(String bool) {

		// no file error handling
		return bool.equalsIgnoreCase("TRUE");
	}

	public void sauver(ObservableList<Salarie> salaries) {

		List<String> lignes = new ArrayList<>();
		for (Salarie salarie : salaries) {
			lignes.add(salarieToCSV(salarie));
			for (Personne enfant : salarie.getEnfants()) {
				lignes.add(enfantToCSV(enfant));
			}
		}
		fichier.ecrireDansFichierTexte(lignes);
	}

	private String salarieToCSV(Salarie salarie) {

		return ("Salarie|" + salarie.getPrenom() + "|" + salarie.getNom() + "|"
				+ salarie.getSexe() + "|"
				+ FrenchDate.dateHeureToString(salarie.getDateDeNaissance())
				+ "|" + salarie.getAgence().getNom() + "|"
				+ salarie.getAgence().hasResto() + "|"
				+ FrenchDate.dateHeureToString(salarie.getDateEmbauche()) + "|"
				+ salarie.getTelephone() + "|"
				+ adresseToCSV(salarie.getAdresse()));
	}

	private String adresseToCSV(Adresse adresse) {

		return adresse.getNumero() + " " + adresse.getNomVoie() + "|"
				+ adresse.getCodePostal() + "|" + adresse.getVille();
	}

	private String enfantToCSV(Personne enfant) {

		return ("Enfant|" + enfant.getPrenom() + "|" + enfant.getSexe() + "|"
				+ FrenchDate.dateHeureToString(enfant.getDateDeNaissance())
				+ "||||||||");
	}
}
