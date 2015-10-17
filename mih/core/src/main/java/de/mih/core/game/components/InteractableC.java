package de.mih.core.game.components;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.game.MiH;
import de.mih.core.game.player.Interaction;

public class InteractableC extends Component
{

	public ArrayList<Interaction> interactions = new ArrayList<Interaction>();

	public InteractableC()
	{
	}

}
