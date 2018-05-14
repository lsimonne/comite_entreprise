package salaries.view;

import salaries.utils.Affichage;

import java.util.Optional;

import salaries.utils.AlertUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import salaries.GestionSalariesApp;
import salaries.model.Agence;
import salaries.model.Personne;
import salaries.model.Salarie;
import salaries.services.Entreprise;

public class SalariesController {

	private GestionSalariesApp app;
	private Entreprise entreprise;
	private Salarie selectedSalarie;
	private AnchorPane rightPane;
	@FXML
	private SplitPane splitPane;
	@FXML
	private VBox nullSelectionText;
	@FXML
	private TableView<Salarie> salariesTable;
	@FXML
	private TableColumn<Salarie, String> nomColumn;
	@FXML
	private TableColumn<Salarie, String> prenomColumn;
	@FXML
	private TableColumn<Salarie, String> sexeColumn;
	@FXML
	private TableColumn<Salarie, String> dateDeNaissanceColumn;
	@FXML
	private TableColumn<Salarie, String> telephoneColumn;
	@FXML
	private TableColumn<Salarie, String> adresseColumn;
	@FXML
	private TableColumn<Salarie, String> agenceColumn;
	@FXML
	private TableColumn<Salarie, String> dateEmbaucheColumn;
	@FXML
	private TableColumn<Salarie, String> chequesVacancesColumn;
	@FXML
	private TableColumn<Salarie, String> chequesNoelColumn;
	@FXML
	private TextField rechercheNom;
	@FXML
	private TextField recherchePrenom;
	@FXML
	private ChoiceBox<String> agenceChoicebox;
	@FXML
	private CheckBox chequesVacancesCheckbox;
	@FXML
	private CheckBox chequesNoelCheckbox;
	@FXML
	private Label noSelectionLabel;
	@FXML
	private Button ajouterIcon;
	@FXML
	private Button modifierIcon;
	@FXML
	private Button supprimerIcon;

	/**
	 * Remplit les données du tableau de salariés </br>
	 * Ajoute les écouteurs des champs de recherche </br>
	 * Ajoute l'écouteur de sélection de salarié
	 * 
	 */
	@FXML
	private void initialize() {

		setRightPane();
		fillSalariesCells();
		addSearchListeners();
		addSalarieSelectionListener();
		// load searchbar view
	}

	private void setRightPane() {

		Label label = new Label(
				"Sélectionnez un salarié dans la liste pour afficher les détails");
		VBox vBox = new VBox(label);
		vBox.setAlignment(Pos.CENTER);
		rightPane = new AnchorPane(vBox);
		AnchorPane.setTopAnchor(vBox, 0.0);
		AnchorPane.setBottomAnchor(vBox, 0.0);
		AnchorPane.setLeftAnchor(vBox, 0.0);
		AnchorPane.setRightAnchor(vBox, 0.0);
	}

	private void fillSalariesCells() {

		nomColumn.setCellValueFactory(
				celldata -> celldata.getValue().getNomProperty());
		prenomColumn.setCellValueFactory(
				celldata -> celldata.getValue().getPrenomProperty());
		agenceColumn.setCellValueFactory(
				celldata -> celldata.getValue().getAgenceProperty());
		chequesNoelColumn.setCellValueFactory(
				celldata -> celldata.getValue().getChequeNoelProperty());
	}

	/**
	 * Ajoute les écouteurs des champs de recherche
	 */
	private void addSearchListeners() {

		addTextFieldListeners();
		agenceChoicebox.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldValue, newValue) -> entreprise.filtrer(
						rechercheNom.getText(), recherchePrenom.getText(),
						newValue, chequesVacancesCheckbox.isSelected(),
						chequesNoelCheckbox.isSelected()));
		addCheckboxesListeners();
	}

	private void addTextFieldListeners() {

		rechercheNom.textProperty()
				.addListener((observable, oldValue, newValue) -> {
					if (!oldValue.equals(newValue))
						entreprise.filtrer(newValue, recherchePrenom.getText(),
								agenceChoicebox.getSelectionModel()
										.getSelectedItem(),
								chequesVacancesCheckbox.isSelected(),
								chequesNoelCheckbox.isSelected());
				});
		recherchePrenom.textProperty()
				.addListener((observable, oldValue, newValue) -> {
					if (!oldValue.equals(newValue))
						entreprise.filtrer(rechercheNom.getText(), newValue,
								agenceChoicebox.getSelectionModel()
										.getSelectedItem(),
								chequesVacancesCheckbox.isSelected(),
								chequesNoelCheckbox.isSelected());
				});
	}

	private void addCheckboxesListeners() {

		chequesVacancesCheckbox.selectedProperty().addListener(
				(obs, oldSelected, newSelected) -> entreprise.filtrer(
						rechercheNom.getText(), recherchePrenom.getText(),
						agenceChoicebox.getSelectionModel().getSelectedItem(),
						newSelected, chequesNoelCheckbox.isSelected()));
		chequesNoelCheckbox.selectedProperty()
				.addListener((obs, oldSelected, newSelected) -> {
					entreprise.filtrer(rechercheNom.getText(),
							recherchePrenom.getText(),
							agenceChoicebox.getSelectionModel()
									.getSelectedItem(),
							chequesVacancesCheckbox.isSelected(), newSelected);
					if (app.getMode().equals(Affichage.NORMAL))
						setChequesNoelColumn(newSelected);
				});
	}

	private void setChequesNoelColumn(boolean selected) {

		if (selected)
			chequesNoelColumn.setVisible(true);
		else
			chequesNoelColumn.setVisible(false);
	}

	/**
	 * Ajoute un écouteur de sélection pour afficher les détails et activer les
	 * boutons modifier et supprimer lorsqu'un salarié est sélectionné
	 */
	private void addSalarieSelectionListener() {

		salariesTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					selectedSalarie = newValue;
					if (app.getMode().equals(Affichage.NORMAL))
						app.afficherDetailsSalarie(newValue);
					app.getMenuController().setEditionAbility(newValue == null);
					setModifSupprButtonAbility(newValue == null);
				});
	}

	/**
	 * Active ou désactive les boutons modifier et supprimer
	 * 
	 * @param disable
	 *            True s'il faut les désactiver
	 */
	private void setModifSupprButtonAbility(boolean disable) {

		modifierIcon.setDisable(disable);
		supprimerIcon.setDisable(disable);
	}

	/**
	 * Affecte le controlleur principal et l'entreprise à app et entreprise</br>
	 * Lie les comparatorProperties pour activer le tri des colonnes </br>
	 * Set les items du tableau de salariés à la liste de salariés de
	 * l'entreprise</br>
	 * Initialize le menu déroulant de recherche par agence</br>
	 * Active le double clic sur les rangées du tableau de salariés
	 * 
	 * @param app
	 *            Le controlleur principal
	 */
	public void setApp(GestionSalariesApp app) {

		this.app = app;
		entreprise = app.getEntreprise();
		entreprise.getSortedSalaries().comparatorProperty()
				.bind(salariesTable.comparatorProperty());
		salariesTable.setItems(entreprise.getSortedSalaries());
		setAgenceChoiceBox();
		enableDoubleClick();
		fillDetailedModeCells();
		/*
		 * actualise le tableau pour corriger le décalage du au chargement de la
		 * scroll bar après le tableau
		 */
		Platform.runLater(() -> salariesTable.refresh());
	}

	/**
	 * Initialise le menu déroulant de recherche par agence avec les valeurs de
	 * la liste d'agences de l'entreprise
	 */
	private void setAgenceChoiceBox() {

		agenceChoicebox.getItems().clear();
		agenceChoicebox.getItems().add("Toutes agences");
		for (Agence a : entreprise.getListeAgences()) {
			agenceChoicebox.getItems().add(a.getNom());
		}
		agenceChoicebox.getSelectionModel().select(0);
	}

	/**
	 * Active le déclenchement de modification de salarié avec un double clic
	 * sur la ligne
	 */
	private void enableDoubleClick() {

		salariesTable.setRowFactory(tv -> {
			TableRow<Salarie> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && !row.isEmpty())
					modifier();
			});
			return row;
		});
	}

	private void fillDetailedModeCells() {

		sexeColumn.setCellValueFactory(
				celldata -> celldata.getValue().getSexeProperty());
		dateDeNaissanceColumn.setCellValueFactory(
				celldata -> celldata.getValue().getDateDeNaissanceProperty());
		telephoneColumn.setCellValueFactory(
				celldata -> celldata.getValue().getTelephoneProperty());
		adresseColumn.setCellValueFactory(
				celldata -> celldata.getValue().getAdresseProperty());
		dateEmbaucheColumn.setCellValueFactory(
				celldata -> celldata.getValue().getDateEmbaucheProperty());
		chequesVacancesColumn.setCellValueFactory(
				celldata -> celldata.getValue().hasChequeVacances()
						? new SimpleStringProperty("oui")
						: new SimpleStringProperty("non"));
	}

	/**
	 * Ouvre une fenêtre de modification de salarié avec un Salarié vide
	 */
	@FXML
	public void ajouter() {

		Salarie newSalarie = new Salarie(new Personne());
		if (app.afficherModifSalarie(newSalarie)
				&& entreprise.ajouter(newSalarie)) {
			selectedSalarie = newSalarie;
			updateListeAgences();
			salariesTable.getSelectionModel().select(newSalarie);
			salariesTable.scrollTo(newSalarie);
		}
	}

	/**
	 * Ouvre une fenêtre de modification du salarié sélectionné
	 */
	@FXML
	public void modifier() {

		if (app.afficherModifSalarie(selectedSalarie)) {
			entreprise.setIsModified(true);
			updateListeAgences();
		}
	}

	/**
	 * Supprime le salarié modifié après confirmation
	 */
	@FXML
	public void supprimer() {

		Optional<ButtonType> result = AlertUtils.showSupprConfirmAlert(
				"Suppression d'un salarié",
				"Etes-vous sûr de vouloir supprimer "
						+ selectedSalarie.getNomComplet() + " ?");
		if (result.isPresent() && result.get() == ButtonType.OK
				&& entreprise.supprimer(selectedSalarie)) {
			salariesTable.getSelectionModel().select(null);
			updateListeAgences();
			AlertUtils.showInfoAlert("Suppression réussie",
					"Le salarié a bien été supprimé", "");
		}
	}

	private void updateListeAgences() {

		entreprise.setListeAgences();
		setAgenceChoiceBox();
	}

	public void setMode(Affichage mode) {

		setDetailedColumnsVisibiliy(mode.equals(Affichage.DETAILED));
		if (selectedSalarie != null)
			scrollToSelected();
		if (mode.equals(Affichage.NORMAL)) {
			splitPane.getItems().add(rightPane);
			if (selectedSalarie != null)
				app.afficherDetailsSalarie(selectedSalarie);
		} else
			splitPane.getItems().remove(1);
	}

	private void setDetailedColumnsVisibiliy(boolean visible) {

		sexeColumn.setVisible(visible);
		dateDeNaissanceColumn.setVisible(visible);
		telephoneColumn.setVisible(visible);
		adresseColumn.setVisible(visible);
		dateEmbaucheColumn.setVisible(visible);
		dateEmbaucheColumn.setVisible(visible);
		chequesVacancesColumn.setVisible(visible);
	}

	public SplitPane getSplitPane() {

		return splitPane;
	}

	public VBox getNullSelectionText() {

		return nullSelectionText;
	}

	private void scrollToSelected() {

		salariesTable.scrollTo(selectedSalarie);
	}
}
