package salaries.view;

import java.io.File;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import salaries.GestionSalariesApp;
import salaries.utils.Affichage;
import salaries.utils.AlertUtils;

public class MenuController {

	private GestionSalariesApp app;
	@FXML
	private MenuItem fichierSauver;
	@FXML
	private MenuItem editionAjouter;
	@FXML
	private MenuItem editionModifier;
	@FXML
	private MenuItem editionSupprimer;
	@FXML
	private Menu edition;
	@FXML
	private Menu mode;
	@FXML
	private CheckMenuItem affichageNormal;
	@FXML
	private CheckMenuItem affichageDetaille;
	@FXML
	private Button saveIcon;
	public void setApp(GestionSalariesApp app) {

		this.app = app;
	}

	/**
	 * Ouvre un selecteur de fichier, tente d'ouvrir et de parser le fichier
	 * sélectionné.</br>
	 * Affiche un message d'erreur si un problème survient lors de la
	 * lecture.</br>
	 * Sinon, initialise l'Entreprise de l'application, affiche la vue de
	 * gestion des salariés, et active les menus et boutons sauver, ajouter, et
	 * les changements de mode
	 */
	@FXML
	private void fichierOuvrir() {

		FileChooser fileChooser = new FileChooser();
		File selectedFile;
		fileChooser.setInitialDirectory(new File("\\"));
		fileChooser.setSelectedExtensionFilter(
				new ExtensionFilter("Text", "*.txt"));
		selectedFile = fileChooser.showOpenDialog(app.getPrimaryStage());
		if (selectedFile != null) {
			app.setEntreprise(selectedFile);
			if (app.getEntreprise().getFileName().equals("fileError"))
				AlertUtils.showErrorAlert("File error", "Wrong file format",
						"There was a problem reading the selected file ("
								+ selectedFile.getName()
								+ "). Please check its format");
			else {
				app.afficherGestionSalaries();
				enableFileMenus();
			}
		}
	}

	private void enableFileMenus() {

		edition.setDisable(false);
		editionAjouter.setDisable(false);
		app.getEntreprise().isModified().addListener(
				(obs, oldValue, newValue) -> handleSaveAbility(
						newValue.booleanValue()));
		handleSaveAbility(false);
		mode.setDisable(false);
	}

	private void handleSaveAbility(boolean enable) {

		fichierSauver.setDisable(!enable);
		saveIcon.setDisable(!enable);
	}

	/**
	 * Sauvegarde la liste dans le fichier, et affiche une confirmation
	 */
	@FXML
	private void fichierSauver() {

		if (app.getEntreprise().sauver())
			AlertUtils.showInfoAlert("Sauvegarde réussie", "",
					"La liste a bien été sauvegardée dans le fichier "
							+ app.getEntreprise().getFileName());
	}

	/**
	 * Demande confirmation à l'utilisateur avant de quitter
	 */
	@FXML
	private void fichierQuitter() {

		Optional<ButtonType> result;
		result = AlertUtils.savePrompt(app.getPrimaryStage());
		if (result.isPresent()) {
			if (result.get().equals(ButtonType.YES))
				app.getEntreprise().sauver();
			if (!result.get().equals(ButtonType.CANCEL)
					&& !result.get().equals(ButtonType.CLOSE))
				Platform.exit();
		}
	}

	@FXML
	private void editionAjouter() {

		app.getSalariesController().ajouter();
	}

	@FXML
	private void editionModifier() {

		app.getSalariesController().modifier();
	}

	@FXML
	private void editionSupprimer() {

		app.getSalariesController().supprimer();
	}

	@FXML
	private void selectNormalMode() {

		if (!affichageNormal.isSelected())
			affichageNormal.setSelected(true);
		affichageDetaille.setSelected(false);
		app.setMode(Affichage.NORMAL);
	}

	@FXML
	private void selectDetailedMode() {

		if (!affichageDetaille.isSelected())
			affichageDetaille.setSelected(true);
		affichageNormal.setSelected(false);
		app.setMode(Affichage.DETAILED);
	}

	@FXML
	private void aideAPropos() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Gestion de salariés");
		alert.setHeaderText("A propos:");
		alert.setContentText("Application de gestion de salariés");
		alert.showAndWait();
	}

	public void setEditionAbility(boolean disable) {

		editionModifier.setDisable(disable);
		editionSupprimer.setDisable(disable);
	}
}
