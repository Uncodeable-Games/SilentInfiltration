package de.mih.core.game.systems;

import java.util.ArrayList;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.SelectEntity_Event;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.player.Player;

public class PlayerSystem extends BaseSystem {
	
	RenderSystem rs;

	public PlayerSystem(SystemManager systemManager, EntityManager entityManager, RenderSystem rs) {
		super(systemManager, entityManager);
		this.rs = rs;
		SelectEntity_Event.register(this);
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return false;
	}

	@Override
	public void update(double dt) {
	}

	@Override
	public void update(double dt, int entity) {
	}

	@Override
	public void render() {
	}

	@Override
	public void render(int entity) {
	}

	@Override
	public void onEventRecieve(Class<? extends BaseEvent> event, ArrayList<Object> params) {
		if (event.equals(SelectEntity_Event.class)) {
			Player p = (Player) params.get(0);
			int entity = (Integer) params.get(1);
			
			if (!p.selectedunits.contains((Integer)entity)) {
				p.clearSelection();
				p.selectUnit(entity);
				entityManager.addComponent(entity, new AttachmentC(entity, "selectioncircle.obj",rs));
			}
		}

	}

}
