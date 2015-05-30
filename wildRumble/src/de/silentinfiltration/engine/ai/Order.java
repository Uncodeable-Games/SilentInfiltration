package de.silentinfiltration.engine.ai;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Order {

	public int entity = -1;
	
	public Order superOrder = null;
	
	public Deque<Order> orderQueue = new ArrayDeque<Order>();
	
	public Order(int e, Order so){
		entity = e;
		superOrder = so;
	}
	
	public boolean clearStack(){
		if (superOrder !=null){
			orderQueue.pop();
			if (orderQueue.isEmpty()) return superOrder.clearStack();
		}
		return false;
	}
}
