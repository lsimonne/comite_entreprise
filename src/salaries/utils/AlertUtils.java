package salaries.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class AlertUtils {

	private AlertUtils() {};

	public static void showInfoAlert(String title, String header,
			String content) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public static void showErrorAlert(String title, String header,
			String content) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public static Optional<ButtonType> showSupprConfirmAlert(String title,
			String content) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setContentText(content);
		return alert.showAndWait();
	}
	
	/**
	 * demande à l'utilisateur s'il veut sauvegarder
	 */
	public static Optional<ButtonType> savePrompt(Stage primaryStage) {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Quitter");
			alert.setHeaderText("Voulez-vous sauvegarder les modifcations?");
			alert.getButtonTypes().remove(0);
			alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
			alert.initModality(Modality.WINDOW_MODAL);
			alert.initOwner(primaryStage);
			return alert.showAndWait();	
		}
}
