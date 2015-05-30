package de.silentinfiltration.engine.ai;

import org.lwjgl.util.vector.Vector2f;

// Orders: "move" "interact" "attack" ...

public class SimpleOrder extends Order {
	public String order = "move";
	public Object target = null;
	SimpleOrder(String s , int e, Order so){
		super(e,so);
		order = s;
	}
	
	SimpleOrder(String s, int e, Object t, Order so){
		super(e,so);
		order = s;
		target = t;
	}
	

public static SimpleOrder simpleMoveOrder(int e, Object t, Order superOrder){
	return new SimpleOrder("move", e, t, superOrder);
}

public static SimpleOrder simpleInteractOrder(int e, Object t, Order superOrder){
	return new SimpleOrder("interact", e, t, superOrder);
}

public static SimpleOrder simpleStopOrder(int e, Order superOrder){
	return new SimpleOrder("stop", e, superOrder);
}

}