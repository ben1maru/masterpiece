package ua.masterpieces.DeleteMasterpieceForm;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import ua.masterpieces.Connector;
import ua.masterpieces.Constants;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Клас форми видалення шедеврів
 */
public class DeleteMasterpieceForm implements Initializable {

    public ChoiceBox<String> masterpieceBox;
    public static Stage currentStage;

    /**
     * Метод видалення шедеврів
     */
    public void deleteButtonClick() {
        try {
            if (masterpieceBox.getValue() != null) {
                String imagePath = null;
                PreparedStatement statementForImage = Connector.connection.prepareStatement("SELECT * FROM masterpieces WHERE name = ?");
                statementForImage.setString(1, masterpieceBox.getValue());
                ResultSet set = statementForImage.executeQuery();
                while (set.next()) {
                    imagePath = set.getString("imagePath");
                }


                PreparedStatement statement = Connector.connection.prepareStatement("DELETE FROM masterpieces WHERE name = ?");
                statement.setString(1, masterpieceBox.getValue());
                statement.executeUpdate();
                if (!imagePath.equals(""))
                    Files.delete(new File(imagePath).toPath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Успіх");
                alert.setContentText("Шедевр успішно видалено");
                alert.showAndWait();
                currentStage.close();
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Метод ініціалізації форми
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateMasterpieces();
    }

    /**
     * Метод оновлення шедеврів
     */
    public void updateMasterpieces() {
        masterpieceBox.getItems().clear();
        try {
            PreparedStatement statement = Connector.connection.prepareStatement("SELECT * FROM masterpieces WHERE createdByUser = ?");
            statement.setString(1, Constants.username);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                masterpieceBox.getItems().add(set.getString("name"));
            }
        }
        catch (Exception ignored) { }
    }
}
