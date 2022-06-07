package ua.masterpieces;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ua.masterpieces.MainForm.MainForm;

import java.util.Objects;

/**
 * Головний клас програми, запускає головну форму
 */
public class Main extends Application {

    /**
     * Метод запуску головної форми
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Connector.init();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainForm.fxml")));
        MainForm.currentStage = primaryStage;
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }


    public void start() {
        launch();
    }

}
