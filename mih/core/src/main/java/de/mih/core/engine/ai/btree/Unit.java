package de.mih.core.engine.ai.btree;

import java.util.HashMap;

import com.badlogic.gdx.ai.btree.BehaviorTree;

import de.mih.core.engine.ai.orders.BaseOrder;
import de.mih.core.engine.ai.orders.MoveOrder;
import de.mih.core.engine.ecs.EntityManager;

public class Unit {

	static HashMap<Integer,Unit> allunits = new HashMap<Integer,Unit>();
	
	public EntityManager entityM;
	
	int entity;
	
	public BaseOrder currentorder;
	
	public BehaviorTree<Unit> btree = new BehaviorTree<Unit>();
	
	public Unit(int e, EntityManager em){
		entityM = em;
		entity = e;
		allunits.put(e, this);
	}
	
	public int getEntity(){
		return entity;
	}
	
	public void newOrder(BaseOrder order){
		currentorder = order;
		if (order.getClass().equals(MoveOrder.class)){
			btree = BTreeParser.readInBTree("assets/btrees/movetotile.tree", this);
		}
	}
	
	public static Unit getUnitByEntity(int e){
		return allunits.get(e);
	}
	
	public static boolean isEntityAUnit(int e){
		return allunits.containsKey(e);
	}
	
	public static HashMap<Integer, Unit> getAllUnits(){
		return allunits;
	}
	
}
