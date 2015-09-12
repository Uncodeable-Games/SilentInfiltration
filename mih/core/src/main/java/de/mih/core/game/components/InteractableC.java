package de.mih.core.game.components;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ecs.Component;
import de.mih.core.game.MiH;
import de.mih.core.game.player.Interaction;

public class InteractableC extends Component {

	public final static String name = "interactable";

	public ArrayList<Interaction> interactions = new ArrayList<Interaction>();

	public InteractableC() {
	}

	@Override
	public void setField(String fieldName, String fieldValue) {
		if (fieldName.equals("interactions")) {
			StringTokenizer st = new StringTokenizer(fieldValue,",\n");
			while (st.hasMoreTokens()) {
				String tmp = st.nextToken();
				if (tmp.equals("\n")) continue;
				Interaction inter = new Interaction(tmp,
						MiH.assetManager.get(st.nextToken(), Texture.class));
				try {
					inter.listener = (Interaction.InteractionListener) Interaction.class.getField(st.nextToken())
							.get(inter.listener);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e) {e.printStackTrace();}
				interactions.add(inter);
			}
			
		}

	}

	@Override
	public Component cpy() {
		InteractableC inter = new InteractableC();
		inter.interactions = interactions;
		return inter;
	}

}
