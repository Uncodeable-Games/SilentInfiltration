package de.mih.core.game.ai.guard;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.physic.Line;
import de.mih.core.game.Game;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.StateMachineComponent;
import de.mih.core.game.components.StateMachineComponent.State;

import java.util.List;

public class Observing extends State
{
	boolean firstiteration = true;
	;
	Game game;
	int  targetEntity;
	// StateMachineComponent stateMachine;
	private StateMachineComponent own;

	public Observing(StateMachineComponent stateMachine, StateMachineComponent own, Game game)
	{
		super(stateMachine);
		// own.entityID = this.stateMachine.entityID;

		this.game = game;
		this.own = own;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onEnter()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeave()
	{
		// TODO Auto-generated method stub

	}

	public void setTarget(int target)
	{
		System.out.println("TARGET_ " + target);
		targetEntity = target;
	}

	boolean targetFound = false;
	boolean lastState   = false;
	public Line sight;

	@Override
	public void update()
	{
//		if (targetFound)
//			return;
//		System.out.println("observe " + this.stateMachine.entityID);

		game.getEntityManager().getEntitiesOfType(AttachmentC.class).forEach(entity ->
		{
			if (!game.getEntityManager().hasComponent(entity, AttachmentC.class))
			{
				game.getEntityManager().addComponent(entity, new AttachmentC(entity));
			}

			AttachmentC attachment = game.getEntityManager().getComponent(entity, AttachmentC.class);
			if (attachment.containsAttachment(4))
			{
				attachment.removeAttachment(4);
			}
		});
		// if (!game.getActivePlayer().isSelectionEmpty())
		// {
		final int   SIGHTVIEW  = 12;
		final float SIGHTANGLE = 60;
		PositionC   playerPos;// = new Vector3(4, 0, 4);
		// int selected = game.getActivePlayer().selectedunits.get(0);
		playerPos = game.getEntityManager().getComponent(stateMachine.entityID, PositionC.class);

		if (!game.getEntityManager().hasComponent(targetEntity, AttachmentC.class))
		{
			game.getEntityManager().addComponent(targetEntity, new AttachmentC(targetEntity));
		}

		PositionC   position   = game.getEntityManager().getComponent(targetEntity, PositionC.class);
		Vector3     entityPos  = position.getPos();
		boolean     inRange    = entityPos.dst(playerPos.position) < SIGHTVIEW;
		AttachmentC attachment = game.getEntityManager().getComponent(targetEntity, AttachmentC.class);
//		System.out.println("in range: " + inRange);
		if (inRange)
		{
			Vector3 direction = playerPos.facing;
			direction.nor();
			Vector3 tmp = entityPos.cpy();
			tmp.sub(playerPos.position);
			boolean inCone = false;
			float   scalar = (direction.x * tmp.x + direction.z * tmp.z);

			float angle2 = (float) Math.toDegrees(Math.acos(scalar / tmp.len()));
			if (tmp.len() > 0 && angle2 < SIGHTANGLE / 2f)
			{
				inCone = true;
			}
//			System.out.println(inCone);

			if (inCone)
			{

				//attachment.addAttachment(4, AdvancedAssetManager.getInstance().getModelByName("redbox"));

				targetFound = true;
				List<Line> walls = game.getTilemap().colLines;
				// Lines sind da, noch gegen entities pr�fen
				sight = new Line(new Vector2(playerPos.position.x, playerPos.position.z),
						new Vector2(position.position.x, position.position.z));
				//	System.out.println("sight: " + sight.from + " " + sight.to);
				for (Line wall : walls)
				{
					Vector2 intersection = new Vector2();
					if (wall.intersects(sight, intersection))
					{
						targetFound = false;
						lastState = false;
						own.current.update();
						//System.out.println("blocked by wall" + " " + intersection);
						return;
					}
				}
				if (!lastState)
				{
					lastState = false;
					//attachment.addAttachment(4, AdvancedAssetManager.getInstance().getModelByName("center"));
					//System.out.println("TARGET FOUND!");
					game.getEventManager().fire(BaseEvent.newLocalEvent("PLAYER_DETECTED", position.getPos().cpy()));
				}

				//	return;

			} else
			{
				targetFound = false;
				lastState = false;
				if (attachment.containsAttachment(4))
					attachment.removeAttachment(4);
			}
		} else
		{
			targetFound = false;
			lastState = false;
			if (attachment.containsAttachment(4))
				attachment.removeAttachment(4);
		}
		own.current.update();
	}
}
