package de.mih.core.game.ai.gob;

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
	
	/***
	 * Spezielle Action, die sich um die Bewegung kümmert
	 * 
	 * @param name
	 * @param time
	 * @param entityId
	 * @param target
	 */
	public MoveToAction(String name, double time, int entityId, Vector3 target) { //, ArrayList<String> enabledItems) {
		super(name, time, new Discontentment());
		this.disc.addGoal(goalNames.HUNGER,0.8);
		this.disc.addGoal(goalNames.FUN,0.8);
		this.disc.addGoal(goalNames.SLEEP,0.8);

		this.entityId = entityId;
		this.target = target;
		GameLogic.getCurrentGame().getEventManager().register(this);
	}
	
	@Override
	public void start()
	{
		//Wenn diese Action gestartet wird, wird ein OrderToPointEvent ausgelöst, das eine Bewegung des "NeedyBot"
		// veranlasst
		GameLogic.getCurrentGame().getEventManager().fire(new OrderToPointEvent(entityId, target));
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
		//Stellt fest ob die Bewegung beendet wurde
		if(event instanceof OrderFinishedEvent)
		{
			OrderFinishedEvent cast = (OrderFinishedEvent) event;
			if(cast.entityId == this.entityId)
			{
				targetReached = true;
			}
		}
	}
	
	public Vector3 getTarget() { return target; }
	
	@Override
	public Action clone()
	{
		MoveToAction clone = new MoveToAction(name,time,entityId, target);
		clone.usedItems = this.usedItems;
		clone.generatedItems = this.generatedItems;
		return clone;
	}

}
