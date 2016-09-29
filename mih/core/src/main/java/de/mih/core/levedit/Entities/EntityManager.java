package de.mih.core.levedit.Entities;

import de.mih.core.levedit.Entities.Abstract.Entity;
import de.mih.core.levedit.Entities.Abstract.EntityType;

import java.util.ArrayList;

/**
 * Created by Cataract on 28.09.2016.
 */

public class EntityManager {
    ArrayList<Entity> entitiesInMap = new ArrayList<>();
    ArrayList<EntityType> entityTypes = new ArrayList<>();

    public EntityType newEntityType(String name){
        EntityType entityType = new EntityType(name);
        entityTypes.add(entityType);
        return entityType;
    }

    public void removeEntityType(EntityType type){
        entityTypes.remove(type);
    }

    public ArrayList<Entity> getEntitiesInMap() {
        return entitiesInMap;
    }

    public ArrayList<EntityType> getEntityTypes() {
        return entityTypes;
    }
}
