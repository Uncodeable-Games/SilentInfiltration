package de.mih.core.levedit.Entities.Components;

import com.sun.javafx.geom.Vec3d;
import de.mih.core.levedit.Entities.Abstract.Component;
import de.mih.core.levedit.Entities.Abstract.Editable;

/**
 * Created by Cataract on 20.09.2016.
 */
public class PositionC extends Component {

    @Editable(value="Position",bool=true)
    private Vec3d position = new Vec3d();

    @Editable(value="Facing",bool=true)
    private Vec3d facing = new Vec3d();

    public Vec3d getPosition() {
        return position;
    }

    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public void setFacing(Vec3d facing) {
        this.facing = facing;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x,y,z);
    }

    public Vec3d getFacing() {
        return facing;
    }

    public void setFacing(float x, float y, float z) {
        this.position.set(x,y,z);
    }
}
