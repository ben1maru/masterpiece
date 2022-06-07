package ua.masterpieces.RegisterForm;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import ua.masterpieces.Connector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Клас форми реєстрації
 */
public class RegisterForm {

    public static Stage currentStage;

    public TextField loginBox;
    public PasswordField passwordBox;

    /**
     * Метод реєстрації користувача
     */
    public void registerButtonClick() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (loginBox.getText().equals("") || passwordBox.getText().equals("")) {
            alert.setTitle("Помилка");
            alert.setHeaderText("Перевірте дані");
            alert.setContentText("Ви не ввели всі потрібні дані");
            alert.showAndWait();
        } else {
            try {
                PreparedStatement registerStatement = Connector.connection.prepareStatement("INSERT INTO users (login, password) VALUES(?, ?)");
                registerStatement.setString(1, loginBox.getText());
                registerStatement.setString(2, DigestUtils.md5Hex(passwordBox.getText()));
                registerStatement.executeUpdate();
            } catch (SQLException e) {
                alert.setTitle("Помилка");
                alert.setHeaderText("Перевірте дані");
                alert.setContentText("Користувач з таким іменем вже існує.\nАбо це просто " + e.getLocalizedMessage());
                alert.showAndWait();
                return;
            }
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Успіх");
            a.setHeaderText("Успішна реєстрація");
            a.setContentText("Вітаю, ви успішно зареєструвались в програмі");
            a.showAndWait();
            currentStage.close();
        }
    }
}
