package core;

import javafx.scene.image.Image;
import jpeg.Process;

import java.io.File;
import java.net.URL;

import static enums.ColorType.RED;

public class FileBindings {

    public static final URL GUIMain = FileBindings.class.getClassLoader().getResource("graphics/MainWindow.fxml");

    public static Image favicon = new Image(FileBindings.class.getClassLoader().getResourceAsStream("favicon.png"));

    public static final String defaultImage = "images/Lenna(testImage).png";

}
