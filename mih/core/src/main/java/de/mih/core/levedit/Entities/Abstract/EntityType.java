package de.mih.core.levedit.Entities.Abstract;

import de.mih.core.engine.ecs.component.Component;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape3D;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Cataract on 28.09.2016.
 */
public class EntityType {
    private String name;

    private ArrayList<Component> components = new ArrayList<>();

    public EntityType(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Entity createEntity() {
        Entity entity = new Entity() {
            @Override
            public void onSelect(MouseEvent event) {

            }

            @Override
            public void onDrag(MouseEvent event) {

            }
        };
        for (Component component : getComponents()) {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                ByteArrayOutputStream bos =
                        new ByteArrayOutputStream(); // A
                oos = new ObjectOutputStream(bos); // B
                // serialize and pass the object
                oos.writeObject(component);   // C
                oos.flush();               // D
                ByteArrayInputStream bin =
                        new ByteArrayInputStream(bos.toByteArray()); // E
                ois = new ObjectInputStream(bin);                  // F
                // return the new object
                entity.addComponent((Component) ois.readObject()); // G
            } catch (Exception e) {
                System.out.println("Exception in ObjectCloner = " + e);
            } finally {
                try {
                    oos.close();
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return entity;
    }

    @Override
    public String toString() {
        return name;
    }
}
