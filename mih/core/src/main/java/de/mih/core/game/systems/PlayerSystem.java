package de.mih.core.game.systems;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.game.Game;
import de.mih.core.game.MiH;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.events.order.SelectEvent;
import de.mih.core.game.player.Player;

public class PlayerSystem extends BaseSystem implements EventListener<SelectEvent>
{
	// EventListener<SelectEvent> selectEvent;

	public PlayerSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game);
		// game.getEventManager().register(this, SelectEntity_Event.class);
		// selectEvent = new EventListener<SelectEvent>() {
		//
		// @Override
		// public void handleEvent(SelectEvent event)
		// {
		// if (event.getClass().equals(SelectEvent.class)) {
		// SelectEvent e = (SelectEvent) event;
		//
		// if
		// (e.selectingplayer.selectedunits.contains((Integer)e.selectedentity))
		// {
		// game.getEntityManager().addComponent(e.selectedentity, new
		// AttachmentC(e.selectedentity,
		// AdvancedAssetManager.getInstance().getModelByName("selectioncircle")));
		// }
		// }
		// }
		// };
		game.getEventManager().register(SelectEvent.class, this);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return false;
	}

	@Override
	public void update(double dt)
	{
	}

	@Override
	public void update(double dt, int entity)
	{
	}

	@Override
	public void render()
	{
	}

	@Override
	public void render(int entity)
	{
	}

	@Override
	public void handleEvent(SelectEvent event)
	{
		if (event.selectingplayer.selectedunits.contains((Integer) event.selectedentity))
		{
			if(!game.getEntityManager().hasComponent(event.selectedentity, AttachmentC.class))
			{
				game.getEntityManager().addComponent(event.selectedentity, new AttachmentC(event.selectedentity));
			}
			game.getEntityManager().getComponent(event.selectedentity, AttachmentC.class).addAttachment(1, AdvancedAssetManager.getInstance().getModelByName("selectioncircle"));
			game.getEntityManager().getComponent(event.selectedentity, AttachmentC.class).addAttachment(2, AdvancedAssetManager.getInstance().getModelByName("cone"));
			//game.getEntityManager().addComponent(event.selectedentity, new AttachmentC(event.selectedentity,
//					AdvancedAssetManager.getInstance().getModelByName("selectioncircle"), 1));
//			game.getEntityManager().addComponent(event.selectedentity, new AttachmentC(event.selectedentity,
//					AdvancedAssetManager.getInstance().getModelByName("cone"), 2));
			System.out.println("ADDED CONE");
		}
	}

}
