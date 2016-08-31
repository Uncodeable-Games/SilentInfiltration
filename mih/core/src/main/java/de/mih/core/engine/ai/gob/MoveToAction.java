package de.mih.core.engine.ai.gob;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.gob.Discontentment.goalNames;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.Game;
import de.mih.core.game.GameLogic;
import de.mih.core.game.events.order.OrderFinishedEvent;
import de.mih.core.game.events.order.OrderToPointEvent;

public class MoveToAction extends Action implements EventListener{

	public boolean targetReached = false;
	int entityId;
	Vector3 target;
	
	public MoveToAction(String name, double time, int entityId, Vector3 target) { //, ArrayList<String> enabledItems) {
		super(name, time, new Discontentment());
		//this.disc.addGoal(goalNames.HUNGER, 5);
		//this.disc.addGoal(goalNames.FUN, 5);

		this.entityId = entityId;
		this.target = target;
		GameLogic.getCurrentGame().getEventManager().register(this);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void start()
	{
		GameLogic.getCurrentGame().getEventManager().fire(new OrderToPointEvent(entityId, target));
		//Order kram
		super.start();
	}
	
	@Override
	public boolean isFinished()
	{
		if(targetReached)
		{
			Game.getCurrentGame().getEventManager().unregister(this);
		}
		return isFinished && targetReached;
	}

	@Override
	public void handleEvent(BaseEvent event) {
		if(event instanceof OrderFinishedEvent)
		{
			OrderFinishedEvent cast = (OrderFinishedEvent) event;
			if(cast.entityId == this.entityId)
			{
				targetReached = true;
			}
		}
	}
	
	@Override
	public Action clone()
	{
		MoveToAction clone = new MoveToAction(name,time,entityId, target);
		clone.usedItems = this.usedItems;
		clone.generatedItems = this.usedItems;
		return clone;
	}

}
