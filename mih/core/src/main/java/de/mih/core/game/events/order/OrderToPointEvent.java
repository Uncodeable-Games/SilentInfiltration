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
	public int target;

	public OrderToPointEvent(int actor, int target)
	{
		this.actor = actor;
		this.target = target;
	}
}
