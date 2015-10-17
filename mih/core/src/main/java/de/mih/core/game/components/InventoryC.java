package de.mih.core.game.components;

import java.util.ArrayList;
import java.util.StringTokenizer;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.game.player.inventory.Item;


public class InventoryC extends Component {

	public ArrayList<Item> items = new ArrayList<Item>();

}
