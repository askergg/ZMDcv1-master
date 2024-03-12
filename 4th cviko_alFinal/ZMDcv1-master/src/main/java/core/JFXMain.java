package core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JFXMain extends Application {
    private static Stage primarystage;


    @Override
    public void start(Stage stage) throws Exception {
        primarystage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(FileBindings.GUIMain);

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primarystage.setScene(scene);

        primarystage.setTitle("xcecha07 JPEG");
        primarystage.getIcons().add(FileBindings.favicon);

        primarystage.show();

        primarystage.setOnCloseRequest((e) -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
