package ua.masterpieces.AuthForm;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import ua.masterpieces.Connector;
import ua.masterpieces.Constants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Клас форми авторизації
 */
public class AuthForm {

    public static Stage currentStage;

    public TextField usernameBox;
    public PasswordField passwordBox;

    /**
     * Метод авторизації
     * @throws SQLException виникає при проблемах з підключенням до БД
     */
    public void authButtonClick() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (usernameBox.getText().equals("") && passwordBox.getText().equals("")) {
            alert.setTitle("Помилка");
            alert.setHeaderText("Перевірте дані");
            alert.setContentText("Ви не ввели логін та пароль");
            alert.showAndWait();
            return;
        }
        else if (usernameBox.getText().equals("")) {
            alert.setTitle("Помилка");
            alert.setHeaderText("Перевірте дані");
            alert.setContentText("Ви не ввели логін");
            alert.showAndWait();
        }
        else if (passwordBox.getText().equals("")) {
            alert.setTitle("Помилка");
            alert.setHeaderText("Перевірте дані");
            alert.setContentText("Ви не ввели пароль");
            alert.showAndWait();
        }
        else {
            PreparedStatement statement = Connector.connection.prepareStatement("SELECT * FROM users WHERE login = ?");
            statement.setString(1, usernameBox.getText());
            ResultSet set = statement.executeQuery();
            int length = 0;

            while (set.next()) {
                length++;
                if (set.getString("password").equals(DigestUtils.md5Hex(passwordBox.getText()))) {
                    Constants.username = set.getString("login");
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Успіх");
                    a.setHeaderText("Вітаю, " + usernameBox.getText());
                    a.showAndWait();
                    currentStage.close();
                } else {
                    alert.setTitle("Помилка");
                    alert.setHeaderText("Перевірте дані");
                    alert.setContentText("Введено неправильний пароль");
                    alert.showAndWait();
                }
            }
            if (length == 0) {
                alert.setTitle("Помилка");
                alert.setHeaderText("Відсутність даних");
                alert.setContentText("База даних не містить такого користувача");
                alert.showAndWait();
            }
        }
    }
}
