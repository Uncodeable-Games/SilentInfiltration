package de.mih.core.levedit.Entities.Abstract;

import de.mih.core.engine.ecs.component.Component;

/**
 * Created by Cataract on 29.09.2016.
 */
public class ComponentType {
    private Class componentclass;

    public ComponentType(Class componentclass) {
        this.componentclass = componentclass;
    }

    public Component newComponent(){
        try {
            return (Component) componentclass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class getComponentclass() {
        return componentclass;
    }

    @Override
    public String toString() {
        return componentclass.getSimpleName();
    }
}
