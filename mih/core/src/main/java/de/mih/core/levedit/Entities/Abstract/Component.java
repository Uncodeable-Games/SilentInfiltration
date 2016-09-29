package de.mih.core.levedit.Entities.Abstract;

/**
 * Created by Cataract on 20.09.2016.
 */
public abstract class Component {
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
