package de.mih.core.game.components;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.io.AdvancedAssetManager;
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
			StringTokenizer st2 = new StringTokenizer(fieldValue, "\n");
			while (st2.hasMoreTokens()) {
				StringTokenizer st = new StringTokenizer(st2.nextToken(), ",");
				Interaction inter = new Interaction(st.nextToken(),
						AdvancedAssetManager.getInstance().assetManager.get(st.nextToken(), Texture.class));
				try {
					inter.listener = (Interaction.InteractionListener) Interaction.class.getField(st.nextToken())
							.get(inter.listener);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e) {
					e.printStackTrace();
				}
				while (st.hasMoreTokens()){
					inter.filter.add(st.nextToken());
				}
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

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
