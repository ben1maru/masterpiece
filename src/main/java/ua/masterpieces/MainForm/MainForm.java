package ua.masterpieces.MainForm;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ua.masterpieces.AddMasterpieceForm.AddMasterpieceForm;
import ua.masterpieces.AuthForm.AuthForm;
import ua.masterpieces.Connector;
import ua.masterpieces.Constants;
import ua.masterpieces.DeleteMasterpieceForm.DeleteMasterpieceForm;
import ua.masterpieces.RegisterForm.RegisterForm;
import ua.masterpieces.ViewForm.ViewForm;
import ua.masterpieces.utils.FormUtils;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Клас головної форми
 */
public class MainForm implements Initializable {

    public static Stage currentStage;
    public MenuItem authButton;
    public MenuItem regButton;
    public MenuItem logoffButton;
    public Menu editButton;
    public VBox vBox;
    public ChoiceBox<String> categoryBox;
    public Button updateButton;

    /**
     * Метод відкриття форми авторизації
     */
    public void authButtonClick() {
        AuthForm.currentStage = FormUtils.createWindow("/fxml/AuthForm.fxml", "Авторизація", currentStage);
        AuthForm.currentStage.showAndWait();
        if (!Constants.username.equals("")) {
            editButton.setVisible(true);
        }
    }

    /**
     * Метод відкриття форми реєстрації
     */
    public void regButtonClick() {
        RegisterForm.currentStage = FormUtils.createWindow("/fxml/RegisterForm.fxml", "Реєстрація", currentStage);
        RegisterForm.currentStage.showAndWait();
    }

    /**
     * Метод виходу з акаунту
     */
    public void logoffButtonClick() {
        Constants.username = "";
        editButton.setVisible(false);
    }

    /**
     * Метод відкриття форми додавання шедевру
     */
    public void addMasterpieceClick() {
        AddMasterpieceForm.currentStage = FormUtils.createWindow("/fxml/AddMasterpieceForm.fxml", "Додати шедевр", currentStage);
        AddMasterpieceForm.currentStage.showAndWait();
        updateButtonClick();
    }

    /**
     * Метод відкриття форми видалення шедевру
     */
    public void deleteMasterpieceClick() {
        DeleteMasterpieceForm.currentStage = FormUtils.createWindow("/fxml/DeleteMasterpieceForm.fxml", "Видалити шедевр", currentStage);
        DeleteMasterpieceForm.currentStage.showAndWait();
        updateButtonClick();
    }

    /**
     * Метод відкриття форми "Про програму"
     */
    public void aboutProgramClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Про програму");
        alert.setHeaderText("Шедеври мистецтва");
        alert.setContentText("Автор даного шедевру:\nРачкулинець Вадім, студент групи КН-32");
        alert.show();
    }

    /**
     * Метод відкриття форми "Про автора"
     */
    public void aboutAuthorClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Про автора");
        alert.setHeaderText("Рачкулинець Вадім, студент групи КН-32");
        alert.setContentText("Або не студент, залежить від оцінки за КП");
        alert.show();
    }

    /**
     * Метод оновлення кнопок
     */
    public void updateButtonClick() {
        vBox.getChildren().clear();
        try {
            PreparedStatement statement;
            if (categoryBox.getValue().equals("Всі")) {
                statement = Connector.connection.prepareStatement("SELECT * FROM masterpieces");
            }
            else {
                statement = Connector.connection.prepareStatement("SELECT * FROM masterpieces WHERE category = ?");
                statement.setString(1, categoryBox.getValue());
            }
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Button button = new Button(set.getString("name"));
                button.setMaxWidth(Double.MAX_VALUE);
                button.setOnAction(this::masterpieceButtonClick);
                vBox.getChildren().add(button);
            }
        }
        catch (Exception ignored) { }
    }

    /**
     * Метод оновлення шедеврів
     */
    public void masterpieceButtonClick(ActionEvent event) {
        Button b = (Button) event.getSource();
        ViewForm.name = b.getText();
        ViewForm.currentStage = FormUtils.createWindow("/fxml/ViewForm.fxml", b.getText(), currentStage);
        ViewForm.currentStage.show();
    }

    /**
     * Метод додавання категорій
     */
    public void addCategories() {
        categoryBox.getItems().add("Всі");
        try {
            PreparedStatement statement = Connector.connection.prepareStatement("SELECT * FROM categories");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                categoryBox.getItems().add(set.getString("categoryName"));
            }
        }
        catch (Exception ignored) { }
    }

    /**
     * Метод оновлення шедеврів при зміні категорії
     */
    public void onValueChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        updateButtonClick();
    }

    /**
     * Метод ініціалізації форми
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCategories();
        Platform.runLater(() -> {
            categoryBox.valueProperty().addListener(this::onValueChanged);
            categoryBox.setValue("Всі");
        });
        updateButtonClick();
    }
}
