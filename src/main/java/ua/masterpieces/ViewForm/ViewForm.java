package ua.masterpieces.ViewForm;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ua.masterpieces.Connector;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Клас форми відображення шедевру
 */
public class ViewForm implements Initializable {

    public Label nameLabel;
    public Label categoryLabel;
    public Label authorLabel;
    public TextArea descriptionBox;
    public ImageView imageBox;
    public VBox vBox;

    public static String name;
    public static Stage currentStage;

    /**
     * Метод ініціалізації форми та підвантаження даних на форму
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            imageBox.fitWidthProperty().bind(vBox.widthProperty());
            imageBox.fitHeightProperty().bind(vBox.heightProperty());
        });
        try {
            PreparedStatement statement = Connector.connection.prepareStatement("SELECT * FROM masterpieces WHERE name = ?");
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                nameLabel.setText(nameLabel.getText() + set.getString("name"));
                categoryLabel.setText(categoryLabel.getText() + set.getString("category"));
                descriptionBox.setText(descriptionBox.getText() + set.getString("text"));
                authorLabel.setText(authorLabel.getText() + set.getString("author"));
                imageBox.setImage(new Image(new File(set.getString("imagePath")).toURI().toString()));
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}
