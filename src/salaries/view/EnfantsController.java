package salaries.view;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import salaries.GestionSalariesApp;
import salaries.model.Personne;
import salaries.model.Salarie;
import salaries.utils.AlertUtils;

public class EnfantsController {

	private GestionSalariesApp app;
	private Salarie parent;
	private Personne selectedEnfant;
	@FXML
	private Button ajouterIconEnfants;
	@FXML
	private Button supprimerIconEnfants;
	@FXML
	private Button modifierIconEnfants;
	@FXML
	private Label fullNameDetailEnfant;
	@FXML
	private TableView<Personne> enfantsTable;
	@FXML
	private TableColumn<Personne, String> prenomColumn;
	@FXML
	private TableColumn<Personne, String> sexeColumn;
	@FXML
	private TableColumn<Personne, String> dateDeNaissanceColumn;

	@FXML
	private void initialize() {

		fillCells();
		addEnfantSelectionListener();
		enableDoubleClick();
	}

	private void fillCells() {

		prenomColumn.setCellValueFactory(
				celldata -> celldata.getValue().getPrenomProperty());
		sexeColumn.setCellValueFactory(
				celldata -> celldata.getValue().getSexeProperty());
		dateDeNaissanceColumn.setCellValueFactory(
				celldata -> celldata.getValue().getDateDeNaissanceProperty());
	}

	/**
	 * Ajoute un écouteur de sélection pour activer les boutons modifier et
	 * supprimer lorsqu'un enfant est sélectionné
	 */
	private void addEnfantSelectionListener() {

		enfantsTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue,
						newValue) -> setSelectedEnfant(newValue));
	}

	/**
	 * Active le déclenchement de modification d'enfant avec un double clic sur
	 * la ligne
	 */
	private void enableDoubleClick() {

		enfantsTable.setRowFactory(tv -> {
			TableRow<Personne> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && !row.isEmpty())
					modifierEnfant();
			});
			return row;
		});
	}

	/**
	 * Affecte l'enfant sélectionné à selectedEnfant</br>
	 * Si un enfant est sélectionné, active les boutons modifier et supprimer,
	 * sinon les désactive
	 * 
	 * @param selectedEnfant
	 *            L'enfant qui vient d'être sélectionné
	 */
	private void setSelectedEnfant(Personne selectedEnfant) {

		this.selectedEnfant = selectedEnfant;
		if (selectedEnfant == null)
			setDisableButtons(true);
		else
			setDisableButtons(false);
	}

	private void setDisableButtons(boolean disable) {

		supprimerIconEnfants.setDisable(disable);
		modifierIconEnfants.setDisable(disable);
	}

	public void setParent(Salarie parent) {

		this.parent = parent;
		enfantsTable.setItems(parent.getEnfants());
	}

	public void setApp(GestionSalariesApp app) {

		this.app = app;
	}

	@FXML
	private void ajouterEnfant() {

		selectedEnfant = new Personne();
		if (app.afficherModifierEnfant(selectedEnfant))
			parent.addEnfant(selectedEnfant);
	}

	@FXML
	private void supprimerEnfant() {

		Optional<ButtonType> result = AlertUtils.showSupprConfirmAlert(
				"Suppression d'un enfant", "Etes-vous sûr de vouloir supprimer "
						+ selectedEnfant.getPrenom() + " ?");
		if (result.isPresent() && result.get() == ButtonType.OK) {
			parent.removeEnfant(selectedEnfant);
			enfantsTable.getSelectionModel().select(null);
		}
	}

	@FXML
	private void modifierEnfant() {

		app.afficherModifierEnfant(selectedEnfant);
	}
}
