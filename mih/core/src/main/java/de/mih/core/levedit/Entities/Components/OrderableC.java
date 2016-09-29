package de.mih.core.levedit.Entities.Components;

import com.sun.javafx.geom.Vec3d;
import de.mih.core.levedit.Entities.Abstract.Component;
import de.mih.core.levedit.Entities.Abstract.Editable;

/**
 * Created by Cataract on 29.09.2016.
 */
public class OrderableC extends Component {
    @Editable("Accept Orders")
    private boolean isOderable = true;
    @Editable("Order")
    private String order = "move";
    @Editable("Wasd")
    private boolean wasd= false;
    @Editable("Target")
    private Vec3d target = new Vec3d(1,2,4);
}
