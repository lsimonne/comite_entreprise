package salaries;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import salaries.model.Personne;
import salaries.model.Salarie;
import salaries.services.Entreprise;
import salaries.utils.Affichage;
import salaries.utils.AlertUtils;
import salaries.view.DetailsTabController;
import salaries.view.EnfantsController;
import salaries.view.MenuController;
import salaries.view.ModifierEnfantController;
import salaries.view.ModifierSalarieController;
import salaries.view.SalariesController;
import salaries.view.Vue;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class GestionSalariesApp extends Application {

	private Stage primaryStage;
	private Stage modifSalarie;
	private BorderPane root;
	private Entreprise entreprise;
	private SalariesController salariesController;
	private MenuController menuController;
	private VBox nullSelectionText;
	private Affichage mode;

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		mode = Affichage.NORMAL;
		try {
			root = new BorderPane();
			this.primaryStage = primaryStage;
			primaryStage.setTitle("Gestion de salariés");
			setAppIcon(primaryStage);
			initMenuLayout();
			setSavePromptOnClose();
		} catch (Exception e) {
		}
	}

	/**
	 * Active le prompt de sauvegarde à la fermeture de l'application
	 */
	private void setSavePromptOnClose() {

		primaryStage.setOnCloseRequest(event -> {
			if (entreprise != null && entreprise.isModified().get()) {
				Optional<ButtonType> result = AlertUtils
						.savePrompt(primaryStage);
				if (result.isPresent() && result.get().equals(ButtonType.YES))
					entreprise.sauver();
			}
		});
	}

	/**
	 * Charge la vue du menu
	 */
	private void initMenuLayout() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
					GestionSalariesApp.class.getResource("view/Menu.fxml"));
			root = loader.load();
			menuController = loader.getController();
			menuController.setApp(this);
		} catch (Exception e) {
		}
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		positionWindowCenter();
	}

	/**
	 * Charge la vue de gestion de salariés
	 */
	public void afficherGestionSalaries() {

		FXMLLoader loader = new FXMLLoader();
		Vue vue;
		vue = new Vue("view/Salaries.fxml", 1400, 900);
		loader.setLocation(getClass().getResource(vue.getLocation()));
		primaryStage.setHeight(vue.getHeight());
		primaryStage.setWidth(vue.getWidth());
		positionWindowCenter();
		try {
			AnchorPane gestionSalaries = loader.load();
			root.setCenter(gestionSalaries);
			salariesController = loader.getController();
			salariesController.setApp(this);
			this.nullSelectionText = salariesController.getNullSelectionText();
		} catch (Exception e) {
		}
	}

	public void positionWindowCenter() {

		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(
				(primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
		primaryStage.setY(
				(primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
	}

	/**
	 * Charge la vue d'onglets à droite du SplitPane</br>
	 * 
	 * @param selectedSalarie
	 *            Le salarié selectionné
	 */
	public void afficherDetailsSalarie(Salarie selectedSalarie) {

		SplitPane splitPane = salariesController.getSplitPane();
		AnchorPane oldRightPane = new AnchorPane();
		if (splitPane.getItems().get(1) instanceof AnchorPane)
			oldRightPane = (AnchorPane) splitPane.getItems().get(1);
		oldRightPane.getChildren().clear();
		if (selectedSalarie != null) {
			// afficher details salarie
			// afficher enfants
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/DetailsTab.fxml"));
			try {
				AnchorPane newRightPane = loader.load();
				DetailsTabController detailsController = loader.getController();
				oldRightPane.getChildren().addAll(newRightPane.getChildren());
				detailsController.setApp(this);
				detailsController.setSelectedSalarie(selectedSalarie);
				afficherEnfants(oldRightPane, selectedSalarie);
			} catch (IOException e) {
			}
		} else {
			oldRightPane.getChildren().add(nullSelectionText);
		}
	}

	private void afficherEnfants(AnchorPane rightPane,
			Salarie parent) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/Enfants.fxml"));
		try {
			AnchorPane vueEnfants = loader.load();
			EnfantsController enfantsController = loader.getController();
			enfantsController.setApp(this);
			enfantsController.setParent(parent);
			if (rightPane.getChildren().get(0) instanceof TabPane) {
				TabPane tabPane = (TabPane) rightPane.getChildren().get(0);
				Tab enfantsTab = tabPane.getTabs().get(2);
				enfantsTab.setContent(vueEnfants);
				vueEnfants.setPrefWidth(tabPane.getPrefWidth());
			}
			else {
				VBox vBox = (VBox) rightPane.getChildren().get(0);
				vBox.getChildren().add(vueEnfants);
			}
			
		} catch (IOException e) {
		}
	}

	public boolean afficherModifSalarie(Salarie salarie) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/ModifierSalarie.fxml"));
		try {
			BorderPane borderPane = loader.load();
			modifSalarie = initModalWindow("Edition de salarié", primaryStage);
			setAppIcon(modifSalarie);
			Scene modifScene = new Scene(borderPane);
			modifSalarie.setScene(modifScene);
			ModifierSalarieController modifController = loader.getController();
			modifController.setSalarie(salarie);
			modifController.setDialogStage(modifSalarie);
			afficherEnfants(modifController.getRightPane(), salarie);
			modifSalarie.showAndWait();
			
			return modifController.isOkClicked();
		} catch (IOException e) {
			return false;
		}
	}

	private void setAppIcon(Stage stage) {

		stage.getIcons().add(new Image(GestionSalariesApp.class
				.getResource("data/icon.jpg").toExternalForm()));
	}

	public boolean afficherModifierEnfant(Personne enfant) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/ModifierEnfant.fxml"));
		try {
			BorderPane borderPane = loader.load();
			Stage modifEnfant = initModalWindow("Edition d'enfant",
					modifSalarie);
			setAppIcon(modifEnfant);
			Scene modifEnfantScene = new Scene(borderPane);
			modifEnfant.setScene(modifEnfantScene);
			ModifierEnfantController modifController = loader.getController();
			modifController.setStage(modifEnfant);
			modifController.setEnfant(enfant);
			modifEnfant.showAndWait();
			return modifController.isOkClicked();
		} catch (IOException a) {
			return false;
		}
	}

	private Stage initModalWindow(String title, Stage owner) {

		Stage newStage = new Stage();
		newStage.setTitle(title);
		newStage.initModality(Modality.WINDOW_MODAL);
		newStage.initOwner(owner);
		return newStage;
	}

	public Entreprise getEntreprise() {

		return entreprise;
	}

	public void setEntreprise(File file) {

		entreprise = new Entreprise(file);
	}

	public Stage getPrimaryStage() {

		return primaryStage;
	}

	public SalariesController getSalariesController() {

		return salariesController;
	}

	public MenuController getMenuController() {

		return menuController;
	}

	public Affichage getMode() {

		return mode;
	}

	public void setMode(Affichage mode) {

		this.mode = mode;
		salariesController.setMode(mode);
	}
}
