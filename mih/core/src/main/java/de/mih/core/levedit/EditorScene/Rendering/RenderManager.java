package de.mih.core.levedit.EditorScene.Rendering;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;

/**
 * Created by Cataract on 28.09.2016.
 */
public class RenderManager {

    public final Shape3D REDBOX = new Box(1,1,1);
    {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setSpecularColor(Color.ORANGE);
        redMaterial.setDiffuseColor(Color.RED);
        REDBOX.setMaterial(redMaterial);
    }
}
