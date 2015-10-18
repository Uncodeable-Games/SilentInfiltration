package de.mih.core.game.components.info;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.InventoryC;
import de.mih.core.game.player.inventory.Item;

public class InventoryComponentInfo implements ComponentInfo<InventoryC>
{

	private ArrayList<Item> items = new ArrayList<>();

	@Override
	public void readFields(Map<String, String> fields)
	{
		for (String key : fields.keySet())
		{
			if (key.equals("items"))
			{
				StringTokenizer st = new StringTokenizer(fields.get(key), "\n");
				while (st.hasMoreTokens())
				{
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), ",");
					Item item = new Item(st2.nextToken(), st2.nextToken(), Integer.parseInt(st2.nextToken()));
					while (st2.hasMoreTokens())
					{
						item.stats.add(st2.nextToken());
					}
					items.add(item);
				}
			}
		}
	}

	@Override
	public InventoryC generateComponent()
	{
		InventoryC tmp = new InventoryC();
		tmp.items = items;
		return tmp;
	}

}
