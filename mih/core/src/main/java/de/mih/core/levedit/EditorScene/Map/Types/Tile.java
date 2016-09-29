package de.mih.core.levedit.EditorScene.Map.Types;

import com.sun.javafx.geom.Vec2f;
import de.mih.core.levedit.Entities.Abstract.Entity;
import javafx.scene.input.MouseEvent;

/**
 * Created by Cataract on 13.09.2016.
 */
public class Tile extends Entity {
    private Vec2f pos = new Vec2f();

    public Tile() {
    }

    @Override
    public void onSelect(MouseEvent event) {
    }

    @Override
    public void onDrag(MouseEvent event) {
    }

    public Tile(float x, float y) {
        this.pos.set(x,y);
    }
}
