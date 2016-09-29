package de.mih.core.levedit.Entities.Abstract;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;

import java.util.ArrayList;

/**
 * Created by Cataract on 20.09.2016.
 */
public abstract class Entity{

    private String name;

    private Shape3D shape;

    private ArrayList<Component> components = new ArrayList<>();

    public Entity() {}

    public Entity(Shape3D shape) {
        this.shape = shape;
        setEvents();
    }

    public Shape3D getShape() {
        return shape;
    }

    public void setShape(Shape3D shape) {
        this.shape = shape;
        setEvents();
    }

    private void setEvents(){
        shape.setOnMouseDragged(event -> {
            onDrag(event);
        });
        shape.setOnMousePressed(event -> {
            onSelect(event);
        });
    }

    public void addComponent(Component component){
        components.add(component);
    }

    public ArrayList<Component> getComponents(){
        return components;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void onSelect(MouseEvent event);

    public abstract void onDrag(MouseEvent event);
}
