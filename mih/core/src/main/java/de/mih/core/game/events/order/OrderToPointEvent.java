package de.mih.core.game.events.order;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.player.Player;

public class OrderToPointEvent extends BaseEvent
{

	public int actor;
	public Vector2 target_point;

	public OrderToPointEvent(int e, Vector2 t)
	{
		actor = e;
		target_point = t;
	}
}
