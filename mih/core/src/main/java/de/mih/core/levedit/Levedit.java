package de.mih.core.levedit;

import de.mih.core.levedit.EditorScene.Map.MapHandler;
import de.mih.core.levedit.Entities.ComponentManager;
import de.mih.core.levedit.Entities.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.nio.file.Paths;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created by Cataract on 13.09.2016.
 */

public class Levedit extends Application{

    private MapHandler mapHandler = new MapHandler();
    private EntityManager entityManager = new EntityManager();
    private ComponentManager componentManager = new ComponentManager();

    private Stage primaryStage;

    public static Levedit instance;

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Paths.get("assets/levedit/levedit.fxml").toUri().toURL());

        Scene scene = new Scene(root,1256.0 ,679.0);
        primaryStage.setTitle("LevEdit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }

    public MapHandler getMapHandler() {
        return mapHandler;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public ComponentManager getComponentManager() {
        return componentManager;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Levedit getInstance() {
        return instance;
    }
}