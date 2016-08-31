package de.mih.core.engine.ai.gob;

import java.util.ArrayList;

import de.mih.core.engine.ecs.component.Component;

public class ItemComponent extends Component {
	public String itemName;
	public ArrayList<Action> usableActions = new ArrayList<>();
}
