package de.mih.core.engine.ecs.events.orderevents;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.player.Player;

public class OrderToPoint_Event extends BaseEvent {
	
	public int actor;
	public Vector3 target_point;
	
	public OrderToPoint_Event(int e, Vector3 t) {
		actor = e;
		target_point = t;
	}
}
