package de.mih.core.engine.ai.orders;

import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.OrderToPoint_Event;
import de.mih.core.game.components.TilemapC;

public class MoveOrder extends BaseOrder {
	
	public Map<Integer,Integer> path;
	public TilemapC tilemap;
	public Vector3 target;
	
	public MoveOrder(Vector3 t, Map<Integer,Integer> p, TilemapC map) {
		target = t;
		path = p;
		tilemap = map;
	}
}
