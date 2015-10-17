package de.mih.core.game.components.info;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.player.Interaction;

public class InteractableComponentInfo extends ComponentInfo<InteractableC>
{

	private ArrayList<Interaction> interactions = new ArrayList<>();

	@Override
	public void readFields(Map<String, String> fields)
	{

		for(String key : fields.keySet())
		{
			if(key.equals("interactions"))
			{
				StringTokenizer st2 = new StringTokenizer(fields.get(key), "\n");
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
	}

	@Override
	public InteractableC generateComponent()
	{
		InteractableC inter = new InteractableC();
		inter.interactions = interactions;
		return inter;
	}

}
