package de.mih.core.game.components;

import java.util.ArrayList;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.world.object.BaseObject;
import de.mih.core.game.systems.RenderSystem;
import de.mih.core.game.world.objects.ChairO;

public class InteractableC extends Component {

	public final static String name = "interactable";

	public BaseObject baseobject;

	public InteractableC() {
	}

	@Override
	public void setField(String fieldName, String fieldValue) {
		if (fieldName.equals("object")) {
			switch (fieldValue) {
			case "ChairO":
				baseobject = new ChairO(this);
				break;
			}
		}
	}

	@Override
	public Component cpy() {
		InteractableC inter = new InteractableC();
		inter.baseobject = baseobject;
		inter.baseobject.interactable = inter;
		return inter;
	}

}
