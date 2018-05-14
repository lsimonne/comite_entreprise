package salaries.services;

import java.io.File;

import salaries.utils.OutilsChaine;
import salaries.dao.SalariesDAO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import salaries.model.Agence;
import salaries.model.Salarie;

public class Entreprise {

	private ObservableList<Salarie> allSalaries;
	private FilteredList<Salarie> listeSalaries;
	private SortedList<Salarie> sortedSalaries;
	private ObservableList<Agence> listeAgences;
	private SalariesDAO salariesDAO;
	private BooleanProperty isModified;
	private String fileName;

	public Entreprise(File file) {

		isModified = new SimpleBooleanProperty();
		isModified.set(false);
		try {
			salariesDAO = new SalariesDAO(file);
			allSalaries = FXCollections.observableArrayList(salariesDAO.lire());
			listeSalaries = new FilteredList<>(allSalaries, critere -> true);
			sortedSalaries = new SortedList<>(listeSalaries);
			setListeAgences();
			fileName = file.getName();
		} catch (Exception e) {
			fileName = "fileError";
		}
	}

	public void setListeAgences() {

		listeAgences = FXCollections.observableArrayList();
		for (Salarie salarie : allSalaries) {
			if (!isInListeAgence(salarie.getAgence()))
				listeAgences.add(salarie.getAgence());
		}
	}

	private boolean isInListeAgence(Agence agence) {

		for (Agence a : listeAgences) {
			if (a.equals(agence))
				return true;
		}
		return false;
	}

	public String getFileName() {

		return fileName;
	}

	public void setFileName(String fileName) {

		this.fileName = fileName;
	}

	public SortedList<Salarie> getSortedSalaries() {

		return sortedSalaries;
	}

	public BooleanProperty isModified() {

		return isModified;
	}

	public ObservableList<Agence> getListeAgences() {

		return listeAgences;
	}

	public void filtrer(String nom, String prenom, String agence,
			boolean chequesVacances, boolean chequesNoel) {

		listeSalaries.setPredicate(personne -> personne.getNom()
				.startsWith(nom.toUpperCase())
				&& personne.getPrenom()
						.startsWith(OutilsChaine.toNomPropre(prenom))
				&& (agence != null && (agence.equals("Toutes agences")
						|| agence.equals(personne.getAgence().getNom())))
				&& (!chequesVacances || personne.hasChequeVacances())
				&& (!chequesNoel || (personne.calculerChequeNoel() > 0)));
	}

	private void reinitFiltres() {

		listeSalaries.setPredicate(personne -> true);
	}

	public boolean ajouter(Salarie newSalarie) {

		if (allSalaries.indexOf(newSalarie) == -1) {
			allSalaries.add(newSalarie);
			reinitFiltres();
			isModified.set(true);
			return true;
		}
		return false;
	}

	public boolean sauver() {

		try {
			salariesDAO.sauver(listeSalaries);
			isModified.set(false);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean supprimer(Salarie salarieSupprime) {

		if (allSalaries.remove(salarieSupprime)) {
			reinitFiltres();
			isModified.set(true);
			return true;
		}
		return false;
	}

	
	
	
	public void setIsModified(boolean isModified) {
	
		this.isModified.set(isModified);
	}

	@Override
	public String toString() {

		return "Entreprise\nSalariés\n" + allSalaries + "\nisModified="
				+ isModified + "\nfileName=" + fileName + "]";
	}
}
