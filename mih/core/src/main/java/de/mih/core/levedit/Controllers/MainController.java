package de.mih.core.levedit.Controllers;

import de.mih.core.levedit.EditorScene.EditorScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Cataract on 28.09.2016.
 */
public class MainController {

    @FXML
    protected void newMap(){
        // Create the custom dialog.
        Dialog<ArrayList<Object>> dialog = new Dialog<>();
        dialog.setTitle("Create Map");
        dialog.setHeaderText("Create a new Map");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField mapname = new TextField("UntitledMap");
        TextField tilesize = new TextField("2");
        TextField height = new TextField("20");
        TextField width = new TextField("20");

        grid.add(new Label("Map Name:"), 0, 0);
        grid.add(mapname, 1, 0);
        grid.add(new Label("Tilesize:"), 0, 1);
        grid.add(tilesize, 1, 1);
        grid.add(new Label("Height:"), 0, 2);
        grid.add(height, 1, 2);
        grid.add(new Label("Width:"), 0, 3);
        grid.add(width, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(mapname.getText());
                objects.add(Integer.parseInt(tilesize.getText()));
                objects.add(Integer.parseInt(height.getText()));
                objects.add(Integer.parseInt(width.getText()));
                return objects;
            }
            return null;
        });

        Optional<ArrayList<Object>> result = dialog.showAndWait();

        result.ifPresent(mapInfo -> {
            enableEditor();
            //mapHandler.newMap((String) mapInfo.get(0),(int)mapInfo.get(1),(int)mapInfo.get(2),(int)mapInfo.get(3));
        });
    }

    @FXML
    Pane subPane;
    @FXML
    Pane pan_main;
    @FXML
    Button bt_NewEntity;
    @FXML
    Button bt_DeleteEntity;
    @FXML
    Button bt_CopyEntity;
    @FXML
    Button bt_PasteEntity;
    @FXML
    Button bt_SaveMap;
    @FXML
    Button bt_EntityManager;
    @FXML
    TitledPane acc_visualisation;
    @FXML
    TitledPane acc_components;

    private EditorScene editorScene;

    private void enableEditor(){
        editorScene = new EditorScene(subPane.getWidth(),subPane.getHeight());
        subPane.getChildren().add(editorScene);
        bt_NewEntity.setDisable(false);

        bt_SaveMap.setDisable(false);
        bt_EntityManager.setDisable(false);

        bt_EntityManager.setOnAction(event -> {

            Parent root = null;
            try {
                root = FXMLLoader.load(Paths.get("assets/levedit/entityM.fxml").toUri().toURL());
                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(pan_main.getScene().getWindow());
                stage.setTitle("Entity Manager");
                stage.setScene(new Scene(root,858 ,625));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
