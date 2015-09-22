package de.mih.core.game.components;

import java.util.ArrayList;
import java.util.StringTokenizer;

import de.mih.core.engine.ecs.Component;
import de.mih.core.game.player.inventory.Item;


public class InventoryC extends Component {

	public static String name = "inventory";
	public ArrayList<Item> items = new ArrayList<Item>();

	@Override
	public void setField(String fieldName, String fieldValue) {
		if (fieldName.equals("items")) {
			StringTokenizer st = new StringTokenizer(fieldValue, "\n");
			while (st.hasMoreTokens()) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken(), ",");
				Item item = new Item(st2.nextToken(), st2.nextToken(), Integer.parseInt(st2.nextToken()));
				while (st2.hasMoreTokens()) {
					item.stats.add(st2.nextToken());
				}
				items.add(item);
			}
		}
	}

	@Override
	public Component cpy() {
		InventoryC tmp = new InventoryC();
		for (Item i : this.items){
			tmp.items.add(i);
		}
		return tmp;
	}

}
