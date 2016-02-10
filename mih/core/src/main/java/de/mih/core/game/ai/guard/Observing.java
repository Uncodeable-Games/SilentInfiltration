package de.mih.core.game.ai.guard;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.physic.Line;
import de.mih.core.game.Game;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.StateMachineComponent;
import de.mih.core.game.components.StateMachineComponent.State;

public class Observing extends State
{

	final int PLAYER_CAPTURED_LIMIT = 2;
	final int SIGHTVIEW = 6;
	final float HALF_SIGHTANGLE = 35;

	boolean firstiteration = true;;
	Game game;
	int targetEntity;
	// StateMachineComponent stateMachine;
	private StateMachineComponent own;

	public Observing(StateMachineComponent stateMachine, StateMachineComponent own, Game game)
	{
		super(stateMachine);

		this.game = game;
		this.own = own;
	}

	@Override
	public void onEnter()
	{

	}

	@Override
	public void onLeave()
	{

	}

	public void setTarget(int target)
	{
//		System.out.println("TARGET_ " + target);
		targetEntity = target;
	}

	boolean targetFound = false;
	boolean lastState = false;
	public Line sight;

	float sight_time = 0;

	@Override
	public void update(double deltaTime)
	{
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

		PositionC playerPos;

		playerPos = game.getEntityManager().getComponent(stateMachine.entityID, PositionC.class);

		if (!game.getEntityManager().hasComponent(targetEntity, AttachmentC.class))
		{
			game.getEntityManager().addComponent(targetEntity, new AttachmentC(targetEntity));
		}

		PositionC position = game.getEntityManager().getComponent(targetEntity, PositionC.class);
		Vector3 entityPos = position.getPos();
		boolean inRange = entityPos.dst(playerPos.position) < SIGHTVIEW;
//		System.out.println(entityPos.dst(playerPos.position));
		AttachmentC attachment = game.getEntityManager().getComponent(targetEntity, AttachmentC.class);
		if (inRange)
		{
			Vector3 direction = playerPos.facing;
			boolean inCone = false;

			direction.nor();
			Vector3 tmp = entityPos.cpy();
			tmp.sub(playerPos.position);

			float scalar = (direction.x * tmp.x + direction.z * tmp.z);
			float angle2 = (float) Math.toDegrees(Math.acos(scalar / tmp.len()));

			if (tmp.len() > 0 && angle2 <= HALF_SIGHTANGLE)
			{
				inCone = true;
			}

			if (inCone)
			{

				attachment.addAttachment(4, AdvancedAssetManager.getInstance().getModelByName("redbox"));

				targetFound = true;
				List<Line> walls = game.getTilemap().colLines;
				// Lines sind da, noch gegen entities prüfen
				sight = new Line(new Vector2(playerPos.position.x, playerPos.position.z),
						new Vector2(position.position.x, position.position.z));

				for (Line wall : walls)
				{
					Vector2 intersection = new Vector2();
					if (wall.intersects(sight, intersection))
					{
						targetFound = false;
						lastState = false;
						own.current.update(deltaTime);
						return;
					}
				}
				if (!lastState)
				{
					lastState = false;
					attachment.addAttachment(4, AdvancedAssetManager.getInstance().getModelByName("center"));
					sight_time += deltaTime;
					//Simulates that if the player is seen to short the enemy doesnt react
					if (sight_time < PLAYER_CAPTURED_LIMIT)
					{
						game.getEventManager()
								.fire(BaseEvent.newLocalEvent("PLAYER_DETECTED", position.getPos().cpy()));
					}
					else
					{
						game.getEventManager()
								.fire(BaseEvent.newLocalEvent("PLAYER_CAPTURED", position.getPos().cpy()));
					}
				}

			}
			else
			{
				targetFound = false;
				lastState = false;
				sight_time = 0;
				if (attachment.containsAttachment(4))
					attachment.removeAttachment(4);
			}

		}
		else
		{
			targetFound = false;
			lastState = false;
			sight_time = 0;
			if (attachment.containsAttachment(4))
				attachment.removeAttachment(4);
		}

		own.current.update(deltaTime);
	}

}
