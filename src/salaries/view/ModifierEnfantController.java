package salaries.view;

import contact.outils.composants.ControlledTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import salaries.model.Personne;
import salaries.utils.AlertUtils;
import salaries.utils.Regex;

public class ModifierEnfantController {

	private Stage dialogStage;
	private boolean okClicked;
	private Personne enfant;
	@FXML
	private ControlledTextField saisiePrenom;
	@FXML
	private DatePicker saisieDateDeNaissance;
	@FXML
	private ChoiceBox<String> saisieSexe;

	@FXML
	private void initialize() {

		saisieSexe.getItems().addAll("F", "M");
	}

	public void setStage(Stage dialogStage) {

		this.dialogStage = dialogStage;
	}

	public void setEnfant(Personne enfant) {

		this.enfant = enfant;
		saisiePrenom.setTextField(enfant.getPrenom());
		saisiePrenom.setProperties(Regex.REGEX_NAME, "Prénom");
		saisieSexe.getSelectionModel().select(enfant.getSexe());
		saisieDateDeNaissance.setValue(enfant.getDateDeNaissance());
	}

	@FXML
	private void enregistrer() {

		if (isValidInput()) {
			enfant.setBasic("", saisiePrenom.getTextField().getText(),
					saisieSexe.getSelectionModel().getSelectedItem(),
					saisieDateDeNaissance.getValue());
			okClicked = true;
			dialogStage.close();
		} else
			AlertUtils.showErrorAlert("Erreur de saisie", "Saisie invalide",
					"");
	}

	private boolean isValidInput() {

		return (saisieDateDeNaissance.getValue() != null
				&& saisiePrenom.getTextField().getText().matches(Regex.REGEX_NAME));
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
