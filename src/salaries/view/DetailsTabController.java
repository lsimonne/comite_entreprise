package salaries.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import salaries.GestionSalariesApp;
import salaries.model.Salarie;
import salaries.utils.FrenchDate;

public class DetailsTabController {

	private GestionSalariesApp app;
	private Salarie selectedSalarie;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab droitsTab;
	@FXML
	private Tab enfantsTab;
	@FXML
	private Label fullNameDetail;
	@FXML
	private Label fullNameDetailEnfants;
	@FXML
	private Label fullNameDetailDroits;
	@FXML
	private GridPane ficheSalarieGrid;
	@FXML
	private GridPane infosCeGrid;
	@FXML
	private Label detailNom;
	@FXML
	private Label detailPrenom;
	@FXML
	private Label detailSexe;
	@FXML
	private Label detailDateDeNaissance;
	@FXML
	private Label detailAdresse;
	@FXML
	private Label detailTelephone;
	@FXML
	private Label detailAgence;
	@FXML
	private CheckBox detailRestaurant;
	@FXML
	private Label detailDateEmbauche;
	@FXML
	private Label detailChequesVacances;
	@FXML
	private Label detailChequesNoel;
	@FXML
	private Button modifierIconRight;
	@FXML
	private Button modifierIconDroits;

	public void afficherDetailSalarie() {

		if (selectedSalarie != null) {
			setAllDetails();
		}
	}

	/**
	 * Remplit les Labels avec les détails du salarié selectionné
	 */
	private void setAllDetails() {

		fullNameDetail.setText(
				selectedSalarie.getPrenom() + " " + selectedSalarie.getNom());
		fullNameDetailDroits.setText(
				selectedSalarie.getPrenom() + " " + selectedSalarie.getNom());
		setDetailsFicheSalarie();
		setDetailsInfosCE();
	}

	private void setDetailsFicheSalarie() {

		detailNom.setText(selectedSalarie.getNom());
		detailPrenom.setText(selectedSalarie.getPrenom());
		detailSexe.setText(selectedSalarie.getSexe());
		if (selectedSalarie.getDateDeNaissance() != null)
			detailDateDeNaissance.setText(FrenchDate
					.dateHeureToString(selectedSalarie.getDateDeNaissance()));
		else
			detailDateDeNaissance.setText("");
		detailTelephone.setText(selectedSalarie.getTelephone());
		if (selectedSalarie.getAdresse() != null)
			detailAdresse.setText(selectedSalarie.getAdresse().toString());
	}

	private void setDetailsInfosCE() {

		detailAgence.setText(selectedSalarie.getAgence().getNom());
		detailRestaurant.setSelected(selectedSalarie.getAgence().hasResto());
		if (selectedSalarie.getDateEmbauche() != null)
			detailDateEmbauche.setText(FrenchDate
					.dateHeureToString(selectedSalarie.getDateEmbauche()));
		else
			detailDateEmbauche.setText("");
		detailChequesVacances
				.setText(selectedSalarie.hasChequeVacances() ? "oui" : "non");
		detailChequesNoel
				.setText(selectedSalarie.getChequeNoelProperty().get());
	}

	public void setApp(GestionSalariesApp app) {

		this.app = app;
	}

	public void setSelectedSalarie(Salarie selectedSalarie) {

		this.selectedSalarie = selectedSalarie;
		afficherDetailSalarie();
	}

	@FXML
	private void modifier() {

		app.getSalariesController().modifier();
	}
}
