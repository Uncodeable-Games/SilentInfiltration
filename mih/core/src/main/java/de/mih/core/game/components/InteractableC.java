package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.game.player.Interaction;

import java.util.ArrayList;

public class InteractableC extends Component
{

	public ArrayList<Interaction> interactions = new ArrayList<Interaction>();

	public InteractableC()
	{
	}
}
