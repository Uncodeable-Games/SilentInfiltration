package de.mih.core.game.ai.guard;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.physic.Geometry;
import de.mih.core.game.Game;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.StateMachineComponent;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.components.StateMachineComponent.State;

public class Observing extends State
{
	Game game;
	int targetEntity;
	//StateMachineComponent stateMachine;
	private StateMachineComponent own;

	public Observing(StateMachineComponent stateMachine, StateMachineComponent own, Game game)
	{
		super(stateMachine);
	//	own.entityID = this.stateMachine.entityID;

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

	@Override
	public void update()
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
//		if (!game.getActivePlayer().isSelectionEmpty())
//		{
			final int SIGHTVIEW = 12;
			final float SIGHTANGLE = 120;
			PositionC playerPos;// = new Vector3(4, 0, 4);
			//int selected = game.getActivePlayer().selectedunits.get(0);
			playerPos = game.getEntityManager().getComponent(stateMachine.entityID, PositionC.class);
			List<ColliderC> colliders = new ArrayList<>();
			game.getEntityManager().getEntitiesOfType(PositionC.class, VisualC.class).forEach(entity ->
			{
				if (!game.getEntityManager().hasComponent(entity, AttachmentC.class))
				{
					game.getEntityManager().addComponent(entity, new AttachmentC(entity));
				}
				
				PositionC position = game.getEntityManager().getComponent(entity, PositionC.class);
				Vector3 entityPos = position.getPos();
				boolean inRange = entityPos.dst(playerPos.position) < SIGHTVIEW;
				AttachmentC attachment = game.getEntityManager().getComponent(entity, AttachmentC.class);

				if(inRange)
				{
					Vector3 direction = playerPos.facing;
					direction.nor();
					Vector3 tmp = entityPos.cpy();
					tmp.sub(playerPos.position);
					boolean inCone = false;
					float scalar =  (direction.x * tmp.x + direction.y * tmp.y + direction.z * tmp.z);

					float angle2 = (float) Math.toDegrees( Math.acos(scalar / tmp.len()));
					if(tmp.len() > 0 && angle2 < SIGHTANGLE/2f)
					{
						inCone = true;
					}
					

					if( inCone)
					{
						if(game.getEntityManager().hasComponent(entity, ColliderC.class))
						{
							colliders.add(game.getEntityManager().getComponent(entity, ColliderC.class));
							attachment.addAttachment(4, AdvancedAssetManager.getInstance().getModelByName("center"));
						}
						else if(attachment.containsAttachment(4))
						{
							attachment.removeAttachment(4);
						}

					}
					else if(attachment.containsAttachment(4))
					{
						attachment.removeAttachment(4);
					}
					

				}
				else if(attachment.containsAttachment(4))
				{
					attachment.removeAttachment(4);
				}
				

			});
			Vector2 playerPos2D = new Vector2(playerPos.getX(),playerPos.getZ());
			List<Integer> entities = Geometry.getVisibleEntities(playerPos2D, colliders);
			entities.forEach(entity -> {
				AttachmentC attachment = game.getEntityManager().getComponent(entity, AttachmentC.class);
				
				attachment.addAttachment(4, AdvancedAssetManager.getInstance().getModelByName("center"));
				if(entity == this.targetEntity)
				{
					targetFound = true;
					//game.getEntityManager().getComponent(entity, PositionC.class).setY(10);
					System.out.println("TARGET FOUND!");
				}
			});
			entities.add(stateMachine.entityID);
			game.getSystemManager().limitRenderer(false);

			game.getSystemManager().setEntitiesToRender(entities);
//		}
//		else
//		{
//			game.getSystemManager().limitRenderer(false);
//		}
		
		if(targetFound)
		{
			//stateMachine.changeGameState("FOLLOW");
			game.getEventManager().fire(BaseEvent.newGlobalEvent("PLAYER_DETECTED"));
		}
		else
		{
			own.current.update();
		}
		
		

	}

}
