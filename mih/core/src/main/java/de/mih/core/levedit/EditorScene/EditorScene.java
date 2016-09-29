package de.mih.core.levedit.EditorScene;

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec2f;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Created by Cataract on 20.09.2016.
 */

public class EditorScene extends SubScene{

    private final PerspectiveCamera camera = new PerspectiveCamera(true);

    private Group root = new Group();

    Vec2d mousePosition = new Vec2d();

    double zoomFactor = -400d;

    public EditorScene(double width, double height) {
        super(new Group(), width, height);

        camera.setNearClip(1);
        camera.setFarClip(2000);
        camera.setFieldOfView(100);
        camera.setTranslateZ(zoomFactor);

        setRoot(root);
        setFill(Color.ALICEBLUE);
        setCamera(camera);

        setOnScroll(event -> {
            if (event.getDeltaY() >0){
                zoomFactor += 10;

            } else {
                zoomFactor -= 10;
            }
            camera.setTranslateZ(zoomFactor);
        });

        setOnMousePressed(event -> {
            mousePosition.set(event.getX(),event.getY());
        });

        setOnMouseDragged(event -> {
            event.consume();
            double tmp = -zoomFactor / 200;
            if (tmp < 0) tmp = -1d/tmp;
            camera.setTranslateX(camera.getTranslateX() - (event.getX() - mousePosition.x) * tmp);
            camera.setTranslateY(camera.getTranslateY() - (event.getY() - mousePosition.y) * tmp);
            mousePosition.set(event.getX(),event.getY());
        });
    }



    public void addObject(Node... objects) {
        root.getChildren().addAll(objects);
    }

    public void removeObject(Node... objects){
        root.getChildren().removeAll(objects);
    }
}
