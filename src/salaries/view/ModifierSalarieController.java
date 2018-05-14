package salaries.view;

import salaries.model.Adresse;
import salaries.model.Agence;
import salaries.utils.AlertUtils;
import salaries.utils.Regex;
import contact.outils.composants.ControlledTextField;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import salaries.model.Salarie;

public class ModifierSalarieController {

	private Stage dialogStage;
	private Salarie salarie;
	private boolean okClicked;
	@FXML
	private AnchorPane rightPane;
	@FXML
	private ControlledTextField saisieNom;
	@FXML
	private ControlledTextField saisiePrenom;
	@FXML
	private ChoiceBox<String> saisieSexe;
	@FXML
	private DatePicker saisieDateDeNaissance;
	@FXML
	private ControlledTextField saisieTelephone;
	@FXML
	private ControlledTextField saisieNumeroVoie;
	@FXML
	private ControlledTextField saisieNomVoie;
	@FXML
	private ControlledTextField saisieCodePostal;
	@FXML
	private ControlledTextField saisieVille;
	@FXML
	private ControlledTextField saisieAgence;
	@FXML
	private CheckBox saisieRestaurantAgence;
	@FXML
	private DatePicker saisieDateEmbauche;

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;
	}

	/**
	 * Configure les choix de sexe </br>
	 * Remplit les donn�es du tableau d'enfants</br>
	 * Ajoute un �couteur de s�lection pour le tableau d'enfants
	 */
	@FXML
	private void initialize() {

		saisieSexe.getItems().addAll("F", "M");
	}

	/**
	 * Param�tre les �couteurs des champs de texte pour controller la saisie
	 */
	private void setControlledFieldsProperties() {

		saisieNom.setProperties(Regex.REGEX_NAME, "Nom");
		saisiePrenom.setProperties(Regex.REGEX_NAME, "Pr�nom");
		saisieNumeroVoie.setProperties(Regex.REGEX_NUMBER, "N�");
		saisieNomVoie.setProperties(Regex.REGEX_STREET_NAME, "Nom de voie");
		saisieCodePostal.setProperties(Regex.REGEX_ZIP_CODE, "Code postal");
		saisieVille.setProperties(Regex.REGEX_NAME, "Ville");
		saisieTelephone.setProperties(Regex.REGEX_PHONE_NUMBER, "T�l�phone");
		saisieAgence.setProperties(Regex.REGEX_NAME, "Agence");
	}

	/**
	 * Remplit les champs avec les d�tails du salari� et param�tre le contr�le
	 * de saisie
	 * 
	 * @param salarie
	 *            Le salari� � modifier
	 */
	public void setSalarie(Salarie salarie) {

		this.salarie = salarie;
		setFicheSalarie(salarie);
		setInfosCE(salarie);
		setControlledFieldsProperties();
	}

	/**
	 * Remplit les champs de la section "Infos CE" avec les d�tails du salari� �
	 * modifier
	 * 
	 * @param salarie
	 *            Le salari� � modifier
	 */
	private void setInfosCE(Salarie salarie) {

		saisieAgence.setTextField(salarie.getAgence().getNom());
		saisieRestaurantAgence.setSelected(salarie.getAgence().hasResto());
		saisieDateEmbauche.setValue(salarie.getDateEmbauche());
	}

	/**
	 * Remplit les champs de la section "Fiche Salari�" avec les d�tails du
	 * salari� � modifier
	 * 
	 * @param salarie
	 *            Le salari� � modifier
	 */
	private void setFicheSalarie(Salarie salarie) {

		saisieNom.setTextField(salarie.getNom());
		saisiePrenom.setTextField(salarie.getPrenom());
		saisieSexe.getSelectionModel().select(salarie.getSexe());
		saisieDateDeNaissance.setValue(salarie.getDateDeNaissance());
		saisieNumeroVoie.setTextField(salarie.getAdresse().getNumero());
		saisieNomVoie.setTextField(salarie.getAdresse().getNomVoie());
		saisieCodePostal.setTextField(salarie.getAdresse().getCodePostal());
		saisieVille.setTextField(salarie.getAdresse().getVille());
		saisieTelephone.setTextField(salarie.getTelephone());
	}

	public AnchorPane getRightPane() {

		return rightPane;
	}

	/**
	 * V�rifie la validit� des champs et modifie le salari� avec les nouvelles
	 * valeurs
	 */
	@FXML
	private void enregistrer() {

		if (!isValidInput()) {
			AlertUtils.showErrorAlert("Erreur de saisie", "Saisie invalide",
					"");
		} else {
			salarie.setAll(saisieNom.getTextField().getText(),
					saisiePrenom.getTextField().getText(),
					saisieSexe.getSelectionModel().getSelectedItem(),
					saisieDateDeNaissance.getValue(),
					saisieTelephone.getTextField().getText(),
					new Adresse(saisieNumeroVoie.getTextField().getText(),
							saisieNomVoie.getTextField().getText(),
							saisieCodePostal.getTextField().getText(),
							saisieVille.getTextField().getText()));
			Agence agence = new Agence(saisieAgence.getTextField().getText(),
					saisieRestaurantAgence.isSelected());
			salarie.setInfosEntreprise(agence, saisieDateEmbauche.getValue());
			// + if agency modified, modify all agencies?
			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * V�rifie la pr�sence et la validit� des champs pr�nom, nom, date de
	 * naissance, num�ro de voie, code postal et ville
	 */
	private boolean isValidInput() {

		return (saisieNom.getTextField().getText().matches(Regex.REGEX_NAME)
				&& saisiePrenom.getTextField().getText().matches(Regex.REGEX_NAME)
				&& saisieDateDeNaissance.getValue() != null
				&& saisieTelephone.getTextField().getText()
						.matches(Regex.REGEX_PHONE_NUMBER)
				&& saisieNumeroVoie.getTextField().getText()
						.matches(Regex.REGEX_NUMBER)
				&& saisieNomVoie.getTextField().getText()
						.matches(Regex.REGEX_STREET_NAME)
				&& saisieCodePostal.getTextField().getText()
						.matches(Regex.REGEX_ZIP_CODE)
				&& saisieVille.getTextField().getText().matches(Regex.REGEX_NAME));
	}

	@FXML
	private void annuler() {

		okClicked = false;
		dialogStage.close();
	}

	public boolean isOkClicked() {

		return okClicked;
	}
}
