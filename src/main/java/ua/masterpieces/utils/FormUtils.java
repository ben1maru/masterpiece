package ua.masterpieces.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Утилітарний клас для форм
 */
public class FormUtils {

    /**
     * Метод створення форми
     * @param stageName шлях до .fxml файлу
     * @param title назва форми
     * @param currentStage форма-власник створюваної форми
     * @return ініціалізована форма
     */
    public static Stage createWindow(String stageName, String title, Stage currentStage) {
        Stage stage = null;
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(FormUtils.class.getResource(stageName)));
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(currentStage);
        }
        catch (Exception e) { e.printStackTrace(); }
        return stage;
    }
}
