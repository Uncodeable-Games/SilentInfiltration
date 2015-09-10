package de.mih.core.game.systems;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.SelectEntity_Event;
import de.mih.core.game.MiH;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.player.Player;

public class PlayerSystem extends BaseSystem {
	
	EntityManager entityM = EntityManager.getInstance();
	EventManager eventM = EventManager.getInstance();
	
	RenderSystem rs;

	public PlayerSystem(RenderSystem rs) {
		super();
		this.rs = rs;
		eventM.register(this, SelectEntity_Event.class);
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
	public void onEventRecieve(BaseEvent event) {
		if (event.getClass().equals(SelectEntity_Event.class)) {
			SelectEntity_Event e = (SelectEntity_Event) event;
			
			if (!e.selectingplayer.selectedunits.contains((Integer)e.selectedentity)) {
				e.selectingplayer.clearSelection();
				e.selectingplayer.selectUnit(e.selectedentity);
				entityM.addComponent(e.selectedentity, new AttachmentC(e.selectedentity, MiH.assetManager.get("assets/models/selectioncircle.obj",Model.class),rs));
			}
		}
	}

}
