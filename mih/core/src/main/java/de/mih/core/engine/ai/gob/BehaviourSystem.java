package de.mih.core.engine.ai.gob;

import java.util.List;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class BehaviourSystem extends BaseSystem {
	List<Integer> allItems;
	
	public BehaviourSystem(SystemManager systemManager, GameLogic gameLogic) {
		super(systemManager, gameLogic);
		// TODO Auto-generated constructor stub
	}
	
	public BehaviourSystem(SystemManager systemManager, GameLogic gameLogic, int priority) {
		super(systemManager, gameLogic, priority);
		// TODO Auto-generated constructor stub
	}


	@Override
	public boolean matchesSystem(int entityId) {
		return gameLogic.getEntityManager().hasComponent(entityId, GobComponent.class) &&
				gameLogic.getEntityManager().hasComponent(entityId, VelocityC.class)  &&
				gameLogic.getEntityManager().hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt) {
		this.allItems = gameLogic.getEntityManager().getEntitiesOfType(ItemComponent.class, PositionC.class);

	}

	@Override
	public void update(double dt, int entity) {
		GobComponent gobC = gameLogic.getEntityManager().getComponent(entity, GobComponent.class);
		//Add items for move to actions, with correct calculated time
		for(Integer itemID : this.allItems)
		{
			Vector3 itemPosition = gameLogic.getEntityManager().getComponent(itemID, PositionC.class).getPos();
			double distance = itemPosition.dst(gameLogic.getEntityManager().getComponent(entity, PositionC.class).getPos());
			double maxspeed = gameLogic.getEntityManager().getComponent(entity, VelocityC.class).maxspeed;
			double time = distance  / maxspeed;
			String name = "Move to " + gameLogic.getEntityManager().getComponent(itemID, ItemComponent.class).itemName;
			ItemComponent moveItem = gobC.state.getItem(name);
			if(distance <= 1)
			{
				gobC.state.addItem(gameLogic.getEntityManager().getComponent(itemID, ItemComponent.class));
				continue;
			}
			else
			{
				gobC.state.removeItem(gameLogic.getEntityManager().getComponent(itemID, ItemComponent.class));
			}
			if(moveItem == null)
			{
				moveItem = new ItemComponent();
				moveItem.itemName = name;
				gobC.state.addItem(moveItem);
			}
			moveItem.usableActions.clear();
			MoveToAction action = new MoveToAction(moveItem.itemName, time, entity, itemPosition);
			//action.addUsedItem(moveItem);
			action.addGeneratedItem(gameLogic.getEntityManager().getComponent(itemID, ItemComponent.class));
			moveItem.usableActions.add(action);
		}
		//System.out.println("added move items");
		if(!gobC.isPerformingAction || gobC.currentAction == null || !gobC.currentAction.isStarted())
		{
			if(gobC.plannedActions.isEmpty())
			{
				gobC.plannedActions = findBestActionSequence(gobC, 3);
				System.out.print("best sequence: ");
				for(Action a : gobC.plannedActions)
				{
					System.out.print(a.name);
				}
				System.out.println();
			}
			if(!gobC.plannedActions.isEmpty())
			{
				//Cloning the action so we can manipulate data only for this entity
				gobC.currentAction = gobC.plannedActions.get(0).clone();
				gobC.plannedActions.remove(0);
				gobC.isPerformingAction = true;
				System.out.println("starting new action: " + gobC.currentAction.name);
				gobC.currentAction.start();
				gobC.usedTime = 0;
			}

		}
		else if(gobC.currentAction.isStarted())
		{
			gobC.usedTime += dt;
			//System.out.println(dt);
			if(gobC.usedTime >= gobC.currentAction.getTime())
			{
				gobC.currentAction.finish();
			}
			if(gobC.currentAction.isFinished)
			{
				gobC.currentAction.apply(gobC.state, gobC.changePerTimeStep);
				gobC.currentAction = null;
				gobC.isPerformingAction = false;
				System.out.println("finished action, new disc: " + gobC.state.disc);
			}
		}
		
	}
	
	//TODO check
	public ArrayList<Action> findBestActionSequence(GobComponent gob, int depth)
	{
		GobState current = new GobState(gob.state);
		Discontentment overTime = gob.changePerTimeStep;
		
		
		HashMap<TmpGobState, Double> evaluatedStates = new HashMap<>();
		TmpGobState best = null;
		double bestValue = Double.MAX_VALUE;
		
		ArrayList<TmpGobState> states = new ArrayList<>();
		states.add(new TmpGobState(current));
		while(!states.isEmpty())
		{
			TmpGobState iterator = states.get(0);
			states.remove(0);
			if(iterator.depth >= depth)
			{
				continue;
			}
			if(evaluatedStates.containsKey(iterator.parent))
			{
				evaluatedStates.remove(iterator.parent);
			}
			//TODO: Irgendwo muss eine MoveAction untergebracht werden!
			for(ItemComponent item : iterator.getItems())
			{
				for(Action action : item.usableActions)
				{
					TmpGobState tmp = new TmpGobState(current);
					tmp.parent = iterator;
					tmp.depth = iterator.depth + 1;
					action.apply(tmp, overTime);
					tmp.appliedAction = action;
					
					double currentCost = tmp.disc.getTotal();
					/*if(currentCost <= bestValue)
					{
						bestValue = currentCost;
						best = iterator;
					}*/
					states.add(tmp);
					evaluatedStates.put(tmp, currentCost);
				}
			}
		}
		for(TmpGobState i : evaluatedStates.keySet())
		{
			double iValue = evaluatedStates.get(i);
			if(iValue <= bestValue)
			{
				best = i;
				bestValue = iValue;
			}
		}
		//best = evaluatedStates.
		ArrayList<Action> result = new ArrayList<>();
		while(true)
		{
			//TODO check
			if(best.parent == null)
				break;
			System.out.println(best.appliedAction);
			result.add(0,best.appliedAction);

			best = (TmpGobState) best.parent;
		}
		return result;
	}
	
	class TmpGobState extends GobState
	{
		public int depth;
		public GobState parent = null;
		public Action appliedAction = null;
		
		public TmpGobState(GobState copy)
		{
			super(copy);
		}
	}

}
