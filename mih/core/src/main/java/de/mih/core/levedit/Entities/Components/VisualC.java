package de.mih.core.levedit.Entities.Components;


import de.mih.core.levedit.Entities.Abstract.Component;
import de.mih.core.levedit.Entities.Abstract.Editable;

/**
 * Created by Cataract on 28.09.2016.
 */
public class VisualC extends Component {
    @Editable("Path to Modelfile")
    private String modeltype;
    @Editable("Hide Model")
    private boolean ishidden = false;
    @Editable("Scale")
    private float scale = 1f;
    @Editable("Use Model in Editor")
    private boolean useModel = true;

    public String getModeltype() {
        return modeltype;
    }

    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }

    public boolean ishidden() {
        return ishidden;
    }

    public void setIshidden(boolean ishidden) {
        this.ishidden = ishidden;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isUseModel() {
        return useModel;
    }

    public void setUseModel(boolean useModel) {
        this.useModel = useModel;
    }
}
