package de.mih.core.engine.levedit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by Cataract on 13.09.2016.
 */
public class levedit extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = Paths.get("assets/levedit/levedit.fxml").toUri().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root,root.prefWidth(679.0),679.0);
        primaryStage.setTitle("LevEdit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startlevedit(){
        launch();
    }
}
