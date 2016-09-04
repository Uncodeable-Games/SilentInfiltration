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

public class BehaviourSystem extends BaseSystem
{
	List<Integer> allItems;

	/***
	 * Hier werden die Berechnungen für das Goal-Oriented-Behaviour
	 * durchgeführt. es gibt zwei Update-Methoden, eine zur initialisierung und
	 * eine die für jedes Entity durchgeführt wird.
	 * 
	 * @param systemManager
	 * @param gameLogic
	 */

	public BehaviourSystem(SystemManager systemManager, GameLogic gameLogic)
	{
		super(systemManager, gameLogic);
		// TODO Auto-generated constructor stub
	}

	public BehaviourSystem(SystemManager systemManager, GameLogic gameLogic, int priority)
	{
		super(systemManager, gameLogic, priority);
		// TODO Auto-generated constructor stub
	}

	// Prüft ob das entity in diesem System genutzt werden kann.
	@Override
	public boolean matchesSystem(int entityId)
	{
		return gameLogic.getEntityManager().hasComponent(entityId, GobComponent.class)
				&& gameLogic.getEntityManager().hasComponent(entityId, VelocityC.class)
				&& gameLogic.getEntityManager().hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt)
	{
		// Alle Entities mit einer Position und einer ItemKomponnete werden
		// eingeholt,
		// Dies wird für die MoveToAction benutzt, mit der die Distanz des
		// Akteurs (Entity im Update) berechnet wird
		// und somit die Dauer der Action approximiert wird.
		this.allItems = gameLogic.getEntityManager().getEntitiesOfType(ItemComponent.class, PositionC.class);

	}

	@Override
	public void update(double dt, int entity)
	{
		GobComponent gobC = gameLogic.getEntityManager().getComponent(entity, GobComponent.class);
		Vector3 position = gameLogic.getEntityManager().getComponent(entity, PositionC.class).getPos();
		double maxspeed = gameLogic.getEntityManager().getComponent(entity, VelocityC.class).maxspeed;

		// In jedem Update wird für entities mit der GobComponent berechnet wie
		// viel die MoveToAction kosten soll
		for (Integer itemID : this.allItems)
		{
			Vector3 itemPosition = gameLogic.getEntityManager().getComponent(itemID, PositionC.class).getPos();
			double distance = itemPosition.dst(position);
			double time = distance / maxspeed;
			String name = "Move to " + gameLogic.getEntityManager().getComponent(itemID, ItemComponent.class).itemName;
			ItemComponent moveItem = gobC.state.getItem(name);
			// Ist die Distanz zu dem Entitie das eine nutzbare
			// Itemcomponent hat geringer als 1, wird
			// dieses Item zu den verfügbaren Items hinzugefügt, und kann
			// nun genutzt werden.
			if (distance <= 1)
			{
				gobC.state.addItem(gameLogic.getEntityManager().getComponent(itemID, ItemComponent.class));
				continue;
			}
			if (moveItem == null)
			{
				moveItem = new ItemComponent();
				moveItem.itemName = name;
				gobC.state.addItem(moveItem);
			}
			else if (gobC.state
					.getItem(gameLogic.getEntityManager().getComponent(itemID, ItemComponent.class).itemName) != null)
			{
				continue;
			}
			// Das Bewegungsitem wird zurückgesetzt und mit einer
			// aktualisierten MoveToAction ausgestattet
			moveItem.usableActions.clear();
			MoveToAction action = new MoveToAction(moveItem.itemName, time, entity, itemPosition);
			for (Integer itemId2 : this.allItems)
			{
				// Alle durch andere ItemComponenten hinzugefügten Items
				// werden auf die Used liste gesetzt.
				// d.h wenn eine Bewegung durchgeführt wurde, können nur
				// noch die Actions des aktuellen Items genutzt werden
				// somit wird sichergestellt, dass eine nicht genutztes
				// aufgenommenes Item verschwindet, wenn man sich
				// vom "Spender" entfernt
				if (itemId2.equals(itemID))
					continue;
				action.addUsedItem(gameLogic.getEntityManager().getComponent(itemId2, ItemComponent.class));
			}
			action.addUsedItem(moveItem);
			action.addGeneratedItem(gameLogic.getEntityManager().getComponent(itemID, ItemComponent.class));
			moveItem.usableActions.add(action);
		}

		// Wenn zurzeit keine Aktion durchgeführt wird, wird eine neue
		// zugewiesen
		if (!gobC.isPerformingAction || gobC.currentAction == null || !gobC.currentAction.isStarted())
		{
			// wenn keine Aktions geplant sind, wird eine neue Sequenz generiert
			if (gobC.plannedActions.isEmpty())
			{
				gobC.plannedActions = findBestActionSequence(gobC, position, maxspeed, 4);

				System.out.print("best sequence: ");
				for (Action a : gobC.plannedActions)
				{
					System.out.print(a.name + " ");
				}
				System.out.println();
			}
			// Wenn es noch geplante Actions gibt, wird die nächste aktiv
			if (!gobC.plannedActions.isEmpty())
			{
				gobC.currentAction = gobC.plannedActions.get(0);
				gobC.plannedActions.remove(0);
				gobC.isPerformingAction = true;
				System.out.println("starting new action: " + gobC.currentAction.name);
				gobC.currentAction.reset();
				gobC.currentAction.start();
				gobC.usedTime = 0;
			}

		}
		else if (gobC.currentAction.isStarted())
		{
			// wenn eine Aktion läuft, wird geprüft ob die Dauer abgeschlossen
			// ist und ob
			// die Aktion als beendet deklariert wurde. Dies ist nur nicht der
			// Fall wenn eine MoveToAction
			// noch nicht auf das Event der Bewegung reagiert hat.
			gobC.usedTime += dt;
			if (gobC.usedTime >= gobC.currentAction.getTime())
			{
				gobC.currentAction.finish();
			}
			if (gobC.currentAction.isFinished)
			{
				gobC.currentAction.apply(gobC.state, gobC.changePerTimeStep);
				gobC.currentAction = null;
				gobC.isPerformingAction = false;
				System.out.println("finished action, new disc: " + gobC.state.disc);
			}
		}

	}

	public ArrayList<Action> findBestActionSequence(GobComponent gob, Vector3 position, double maxspeed, int depth)
	{
		GobState current = new GobState(gob.state);
		Discontentment overTime = gob.changePerTimeStep;

		HashMap<TmpGobState, Double> evaluatedStates = new HashMap<>();
		TmpGobState best = null;
		double bestValue = Double.MAX_VALUE;

		// Initialer State wird in die Liste gesteckt
		ArrayList<TmpGobState> states = new ArrayList<>();
		TmpGobState currentTmp = new TmpGobState(current);
		currentTmp.position = position;
		states.add(currentTmp);

		while (!states.isEmpty())
		{
			// Erster state in der Liste wird entfernt
			TmpGobState iterator = states.get(0);
			states.remove(0);
			if (iterator.depth >= depth)
			{
				// Wenn der aktuelle State die maximale Tiefe erreicht hat, wird
				// er in eine Ergebnis Hashmap geschrieben
				// zusammen mit dem insgesammten Discontentment wert
				evaluatedStates.put(iterator, iterator.disc.getTotal());
				continue;
			}

			// Für alle verfügbaren Items des aktueleln States
			for (ItemComponent item : iterator.getItems())
			{
				for (Action action : item.usableActions)
				{
					// Ein neuer TmpState wird erzeugt (hält zusätzliche Daten
					// für die berechnung der besten Sequenz
					TmpGobState tmp = new TmpGobState(iterator);
					tmp.parent = iterator;
					tmp.depth = iterator.depth + 1;

					if (action instanceof MoveToAction)
					{
						// Ist die geplante Action eine MoveToAction wird die
						// Dauer angepasst
						// Dies ist notwendig, da sich in diesem Schritt das
						// Entity nicht wirklich bewegt
						// und somit ohne anpassung der Position eine Falsche
						// Dauer der Atkion genutzt werden würde
						MoveToAction moveAction = (MoveToAction) action;
						moveAction.time = iterator.position.dst(moveAction.target) / maxspeed;
						tmp.position = moveAction.target;
					}
					else
					{
						tmp.position = iterator.position;
					}

					action.apply(tmp, overTime);
					tmp.appliedAction = action;

					// Der neue State wird zur Liste hinzugefügt
					states.add(tmp);
				}
			}
		}
		// Nachdem alle möglichen Aktionen durchgeführt wurden, wird die
		// Sequenz gewählt die die kleinste
		// Discontentment Werte hat
		for (TmpGobState i : evaluatedStates.keySet())
		{
			double iValue = evaluatedStates.get(i);
			if (iValue <= bestValue)
			{
				best = i;
				bestValue = iValue;
			}
		}
		// Über die parent beziehung wird die Sequenz aufgebaut und
		// zurückgegeben
		ArrayList<Action> result = new ArrayList<>();
		while (true)
		{
			if (best.parent == null)
				break;
			System.out.println(best.appliedAction);
			result.add(0, best.appliedAction);

			best = (TmpGobState) best.parent;
		}
		return result;
	}

	class TmpGobState extends GobState
	{
		public Vector3 position;
		public int depth;
		public GobState parent = null;
		public Action appliedAction = null;

		public TmpGobState(GobState copy)
		{
			disc = new Discontentment(copy.disc);
			possessedItems = new HashMap<>();
			for (ItemComponent item : copy.possessedItems.values())
			{
				possessedItems.put(item.itemName, item);
			}
		}
	}

}
