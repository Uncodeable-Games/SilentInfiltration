package de.silentinfiltration.engine.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;


/**
 * Systems work on entities that have the wanted components.
 * 
 * @author Tobias
 *
 */
public abstract class BaseSystem {
	// TODO: how do we link components and systems without searching on every
	// entity? (efficiency!!)
	// TODO(Nico): Make an entity-class instead of just in int? So we could make
	// a list in every component, which stores all the entities having this
	// component. Pro: Efficiency!!, Easy to delete entities. Contra: Entities take more space, Handling entities is a bit slower.
	protected EntityManager entityManager;
	protected EventManager eventManager;
	protected Map<Integer, List<Event>> eventQueue;

	public BaseSystem(SystemManager systemManager, EntityManager entityManager,
			EventManager eventManager) {
		systemManager.register(this);
		this.entityManager = entityManager;
		this.eventManager = eventManager;
		this.eventQueue = new HashMap<Integer, List<Event>>();
	}

	/**
	 * check if the enitity has the needed components
	 * 
	 * @param entity
	 * @return
	 */
	public abstract boolean matchesSystem(int entityId);

	/**
	 * Call for the game-loop, here all needed changes on the data is made.
	 * 
	 * @param dt
	 *            Time in ms since the last call
	 */
	public abstract void update(long dt, int entity) throws ComponentNotFoundEx;

	/**
	 * Call for the game-loop, only needed for systems that want to render
	 * something.
	 */
	public abstract void render(int entity) throws ComponentNotFoundEx;

	public void receiveEvent(Event e, float dt) throws ComponentNotFoundEx {
		List<Event> list = eventQueue.get(e.entityID);
		if (list == null) {
			list = new ArrayList<Event>();
			eventQueue.put(e.entityID, list);
		}
		list.add(e);
	}
///	public abstract <T extends Event > void receiveE(T event);
}
