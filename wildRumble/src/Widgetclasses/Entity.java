package Widgetclasses;

import java.util.LinkedList;

public class Entity {
	
	static LinkedList<Entity> llentity = new LinkedList<Entity>();
	
	Entity(){
		llentity.add(this);
	}

}
