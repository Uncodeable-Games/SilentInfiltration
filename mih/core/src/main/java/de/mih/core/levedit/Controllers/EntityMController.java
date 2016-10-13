package de.mih.core.levedit.Controllers;

import com.sun.javafx.geom.Vec3d;
import de.mih.core.engine.ecs.component.Component;
import de.mih.core.levedit.Entities.Abstract.ComponentType;
import de.mih.core.levedit.Entities.Abstract.Editable;
import de.mih.core.levedit.Entities.Abstract.Entity;
import de.mih.core.levedit.Entities.Abstract.EntityType;
import de.mih.core.levedit.Entities.EntityManager;
import de.mih.core.levedit.Levedit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.lang.reflect.Field;

/**
 * Created by Cataract on 28.09.2016.
 */
public class EntityMController {

    private EntityManager entityManager;
    {
        entityManager = Levedit.getInstance().getEntityManager();
    }

    @FXML
    Button bt_OK;
    @FXML
    Button bt_New;
    @FXML
    Button bt_Delete;
    @FXML
    Button bt_Copy;
    @FXML
    Button bt_Paste;

    @FXML
    Button bt_Add;
    @FXML
    Button bt_Remove;

    @FXML
    ListView listview_types;
    @FXML
    ListView listview_comps;

    @FXML
    TextField tf_Name;
    @FXML
    Label label_Name;
    @FXML
    ChoiceBox choice_box;

    @FXML
    GridPane grid;

    private boolean namechange = false;

    public EntityMController() {
        Levedit.getInstance().setEntityMController(this);
    }

    @FXML
    public void initialize() {
        bt_OK.setOnAction(event -> {
            ((Stage) bt_OK.getScene().getWindow()).close();
            Levedit.getInstance().setEntityMController(null);
            Levedit.getInstance().getMainController().updateEntityTypeList();
        });
        bt_New.setOnAction(event -> {
            EntityType type = entityManager.newEntityType(tf_Name.getText());
            listview_types.getItems().add(type);
            listview_types.getSelectionModel().select(type);

            if (listview_types.getItems().size() == 1) {
                tf_Name.setDisable(false);
                label_Name.setDisable(false);
                choice_box.setDisable(false);
                bt_Add.setDisable(false);
                bt_Delete.setDisable(false);
                listview_comps.setDisable(false);

                choice_box.getSelectionModel().select(0);
            }
        });

        bt_Delete.setOnAction(event -> {
            EntityType entityType = (EntityType) listview_types.getSelectionModel().getSelectedItem();
            listview_types.getItems().remove(entityType);
            entityManager.removeEntityType(entityType);
            if (listview_types.getItems().isEmpty()) {
                tf_Name.setDisable(true);
                tf_Name.setText("Entity");
                label_Name.setDisable(true);
                choice_box.setDisable(true);
                bt_Add.setDisable(true);
                bt_Delete.setDisable(true);
                listview_comps.setDisable(true);

                listview_comps.getItems().clear();
            }
        });

        listview_types.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            grid.getChildren().clear();
            if (newValue == null) return;
            if (!namechange) {
                EntityType type = (EntityType) newValue;
                tf_Name.setText(type.toString());
                listview_comps.getItems().clear();
                for (Component component : type.getComponents()) {
                    listview_comps.getItems().add(component);
                }
                if (!listview_comps.getItems().isEmpty()) listview_comps.getSelectionModel().select(0);
            } else {
                namechange = false;
            }
        });

        bt_Add.setOnAction(event -> {
            EntityType entityType = (EntityType) listview_types.getSelectionModel().getSelectedItem();
            ComponentType componentType = (ComponentType) choice_box.getSelectionModel().getSelectedItem();
            for (Component component : entityType.getComponents()) {
                if (component.getClass() == componentType.getComponentclass()) {
                    listview_comps.getSelectionModel().select(component);
                    return;
                }
            }
            Component component = componentType.newComponent();
            entityType.addComponent(component);
            listview_comps.getItems().add(component);
            listview_comps.getSelectionModel().select(component);
        });

        bt_Remove.setOnAction(event -> {
            if (!listview_comps.getSelectionModel().isEmpty()) {
                EntityType entityType = (EntityType) listview_types.getSelectionModel().getSelectedItem();
                Component componentType = (Component) listview_comps.getSelectionModel().getSelectedItem();
                entityType.removeComponent(componentType);
                listview_comps.getItems().remove(componentType);
            }
        });

        listview_comps.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            grid.getChildren().clear();
            if (newValue != null) {
                bt_Remove.setDisable(false);
                try {
                    updateGrid((Component) newValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                bt_Remove.setDisable(true);
            }
        });

        tf_Name.setOnAction(event -> {
            if (!listview_types.getSelectionModel().isEmpty()) {
                namechange = true;
                EntityType selected = (EntityType) listview_types.getSelectionModel().getSelectedItem();
                selected.setName(tf_Name.getText());
                int index = listview_types.getSelectionModel().getSelectedIndex();
                listview_types.getItems().remove(index);
                listview_types.getItems().add(index, selected);
                listview_types.getSelectionModel().select(index);

            }
        });

        for (ComponentType type : Levedit.instance.getComponentManager().getAllComponents()) {
            choice_box.getItems().add(type);
        }

        if (!entityManager.getEntityTypes().isEmpty()) {
            for (EntityType entityType : entityManager.getEntityTypes()) {
                listview_types.getItems().add(entityType);
            }

            tf_Name.setDisable(false);
            label_Name.setDisable(false);
            choice_box.setDisable(false);
            bt_Add.setDisable(false);
            bt_Delete.setDisable(false);
            listview_comps.setDisable(false);
            choice_box.getSelectionModel().select(0);

            listview_types.getSelectionModel().select(0);
        }
    }

    private void updateGrid(Component component) throws IllegalAccessException {
        Class<? extends Component> objClass = component.getClass();
        Field[] fields = objClass.getDeclaredFields();

        int count = 0;
        for (Field field : fields) {
            if (field.getAnnotation(Editable.class) != null && !field.getAnnotation(Editable.class).bool()) {

                field.setAccessible(true);

                switch (field.getType().getSimpleName()) {
                    case "String": {
                        updateString(component,field,count++);
                        break;
                    }
                    case "boolean": {
                        updateBoolean(component,field,count++);
                        break;
                    }
                    case "float": {
                        updateFloat(component,field,count++);
                        break;
                    }
                    case "double": {
                        updateDouble(component,field,count++);
                        break;
                    }
                    case "int": {
                        updateInt(component,field,count++);
                        break;
                    }
                    case "Vector3": {
                        updateVector3(component,field,count++);
                        break;
                    }
                }
            }
        }
    }

    private void updateString(Component component,Field field,int count) throws IllegalAccessException {
        String desc = field.getAnnotation(Editable.class).value();
        grid.add(new Label(desc + ": "), 0, count);

        TextField textField = new TextField();
        textField.setOnAction(event -> {
            try {
                field.set(component, textField.getText());
                for(Entity e : ((EntityType)listview_types.getSelectionModel().getSelectedItem()).getGeneratedEntities()){
                    for (Component c : e.getComponents()){
                        if (c.getClass() == component.getClass()) field.set(c,textField.getText());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        if ((String) field.get(component) != null && (String) field.get(component) != "")
            textField.setText((String) field.get(component));
        else textField.setText(desc);

        grid.add(textField, 1, count);
    }

    private void updateBoolean(Component component, Field field, int count) throws IllegalAccessException{
        String desc = field.getAnnotation(Editable.class).value();
        grid.add(new Label(desc + ": "), 0, count);

        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            try {
                field.setBoolean(component, checkBox.isSelected());
                for(Entity e : ((EntityType)listview_types.getSelectionModel().getSelectedItem()).getGeneratedEntities()){
                    for (Component c : e.getComponents()){
                        if (c.getClass() == component.getClass()) field.setBoolean(c, checkBox.isSelected());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        checkBox.setSelected(field.getBoolean(component));

        grid.add(checkBox, 1, count);
    }

    private void updateFloat(Component component, Field field, int count) throws IllegalAccessException{
        String desc = field.getAnnotation(Editable.class).value();
        grid.add(new Label(desc + ": "), 0, count);
        TextField textField = new TextField();
        textField.setOnAction(event -> {
            float value = 1.0f;
            try {
                value = Float.parseFloat(textField.getText());
            } catch (NumberFormatException e) {
                textField.setText("1.0f");
            }
            try {
                field.setFloat(component, value);
                for(Entity e : ((EntityType)listview_types.getSelectionModel().getSelectedItem()).getGeneratedEntities()){
                    for (Component c : e.getComponents()){
                        if (c.getClass() == component.getClass()) field.setFloat(c, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        textField.setText("" + field.getFloat(component));

        grid.add(textField, 1, count);
    }

    private void updateDouble(Component component, Field field, int count) throws IllegalAccessException{
        String desc = field.getAnnotation(Editable.class).value();
        grid.add(new Label(desc + ": "), 0, count);
        TextField textField = new TextField();
        textField.setOnAction(event -> {
            double value = 1.0;
            try {
                value = Double.parseDouble(textField.getText());
            } catch (NumberFormatException e) {
                textField.setText("1.0d");
            }
            try {
                field.setDouble(component, value);
                for(Entity e : ((EntityType)listview_types.getSelectionModel().getSelectedItem()).getGeneratedEntities()){
                    for (Component c : e.getComponents()){
                        if (c.getClass() == component.getClass())  field.setDouble(c, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        textField.setText("" + field.getDouble(component));
        grid.add(textField, 1, count);
    }

    private void updateInt(Component component, Field field, int count) throws IllegalAccessException{
        String desc = field.getAnnotation(Editable.class).value();
        grid.add(new Label(desc + ": "), 0, count);
        TextField textField = new TextField();
        textField.setOnAction(event -> {
            int value = 1;
            try {
                value = Integer.parseInt(textField.getText());
            } catch (NumberFormatException e) {
                textField.setText("1");
            }
            try {
                field.setInt(component, value);
                for(Entity e : ((EntityType)listview_types.getSelectionModel().getSelectedItem()).getGeneratedEntities()){
                    for (Component c : e.getComponents()){
                        if (c.getClass() == component.getClass()) field.setInt(c, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        textField.setText("" + field.getInt(component));
        grid.add(textField, 1, count);
    }

    private void updateVector3(Component component, Field field, int count) throws IllegalAccessException{
        String desc = field.getAnnotation(Editable.class).value();
        grid.add(new Label(desc + ": "), 0, count);

        HBox hBox = new HBox();

        //x
        TextField x = new TextField();
        x.setMaxWidth(60);
        x.setOnAction(event -> {
            Vec3d vec3d = null;
            try {
                vec3d = (Vec3d) field.get(component);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            double value = 1.0;
            try {
                value = Double.parseDouble(x.getText());
            } catch (NumberFormatException e) {
                x.setText("1.0");
            }
            vec3d.set(value,vec3d.y,vec3d.z);
            for(Entity e : ((EntityType)listview_types.getSelectionModel().getSelectedItem()).getGeneratedEntities()){
                for (Component c : e.getComponents()){
                    if (c.getClass() == component.getClass()){
                        try {
                            vec3d = (Vec3d) field.get(c);
                            vec3d.set(value,vec3d.y,vec3d.z);
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        x.setText("" + ((Vec3d) field.get(component)).x);
        hBox.getChildren().add(x);

        //y
        TextField y = new TextField();
        y.setMaxWidth(60);
        y.setOnAction(event -> {
            Vec3d vec3d = null;
            try {
                vec3d = (Vec3d) field.get(component);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            double value = 1.0;
            try {
                value = Double.parseDouble(y.getText());
            } catch (NumberFormatException e) {
                y.setText("1.0");
            }
            vec3d.set(vec3d.x,value,vec3d.z);
            for(Entity e : ((EntityType)listview_types.getSelectionModel().getSelectedItem()).getGeneratedEntities()){
                for (Component c : e.getComponents()){
                    if (c.getClass() == component.getClass()){
                        try {
                            vec3d = (Vec3d) field.get(c);
                            vec3d.set(vec3d.x,value,vec3d.z);
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        y.setText("" + ((Vec3d) field.get(component)).y);
        hBox.getChildren().add(y);

        //z
        TextField z = new TextField();
        z.setMaxWidth(60);
        z.setOnAction(event -> {
            Vec3d vec3d = null;
            try {
                vec3d = (Vec3d) field.get(component);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            double value = 1.0;
            try {
                value = Double.parseDouble(z.getText());
            } catch (NumberFormatException e) {
                z.setText("1.0");
            }
            vec3d.set(vec3d.x,vec3d.y,value);
            for(Entity e : ((EntityType)listview_types.getSelectionModel().getSelectedItem()).getGeneratedEntities()){
                for (Component c : e.getComponents()){
                    if (c.getClass() == component.getClass()){
                        try {
                            vec3d = (Vec3d) field.get(c);
                            vec3d.set(vec3d.x,vec3d.y,value);
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        z.setText("" + ((Vec3d) field.get(component)).z);
        hBox.getChildren().add(z);

        grid.add(hBox,1,count);
    }


}
