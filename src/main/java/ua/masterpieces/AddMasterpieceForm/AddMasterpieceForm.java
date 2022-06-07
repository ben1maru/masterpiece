package ua.masterpieces.AddMasterpieceForm;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ua.masterpieces.Connector;
import ua.masterpieces.Constants;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Клас форми додавання шедеврів
 */
public class AddMasterpieceForm implements Initializable {

    public TextField masterpieceNameTextBox;
    public TextField masterpieceAuthorTextBox;
    public Button addMasterpieceButton;
    public ChoiceBox<String> masterpieceComboBox;
    public Button masterpieceImageButton;
    public TextArea masterpieceTextTextBox;

    public String pathToImage;

    public static Stage currentStage;

    /**
     * Метод додавання шедевру в базу даних
     */
    public void addMasterpieceButtonClick() {
        if (masterpieceNameTextBox.getText().equals("") || masterpieceTextTextBox.getText().equals("") || masterpieceAuthorTextBox.getText().equals("") || masterpieceComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Помилка");
            alert.setContentText("Ви не ввели всі необхідні дані");
            alert.show();
        }
        else {
            try {
                PreparedStatement preparedStatement = Connector.connection.prepareStatement("INSERT INTO masterpieces (name, text, imagePath, category, author, createdByUser) VALUES (?,?,?,?,?,?)");
                preparedStatement.setString(1, masterpieceNameTextBox.getText());
                preparedStatement.setString(2, masterpieceTextTextBox.getText());
                if (pathToImage != null)
                    preparedStatement.setString(3, "images/" + pathToImage + ".png");
                else
                    preparedStatement.setString(3, "");
                preparedStatement.setString(4, masterpieceComboBox.getValue());
                preparedStatement.setString(5, masterpieceAuthorTextBox.getText());
                preparedStatement.setString(6, Constants.username);
                preparedStatement.executeUpdate();
                String pathToFinalImage = "images/" + masterpieceNameTextBox.getText() + ".png";
                Path path = new File(pathToFinalImage).toPath();
                if (pathToImage != null)
                    Files.copy(Paths.get(pathToImage), path, StandardCopyOption.REPLACE_EXISTING);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Успіх");
                alert.setContentText("Шедевр успішно додано");
                alert.showAndWait();
                currentStage.close();
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Помилка");
                alert.setContentText("Перевірте введені дані\nШедевр з таким іменем, скоріше за все, вже існує");
                alert.show();
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод вибору зображення
     */
    public void pickImageClick() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(currentStage);
        if (file != null) {
            pathToImage = file.getAbsolutePath();
            masterpieceImageButton.setText(pathToImage.substring(pathToImage.lastIndexOf("\\") + 1));
        }
    }

    /**
     * Метод ініціалізації форми
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            PreparedStatement statement = Connector.connection.prepareStatement("SELECT * FROM categories");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                masterpieceComboBox.getItems().add(set.getString("categoryName"));
            }
        }
        catch (Exception ignored) { }
    }
}
