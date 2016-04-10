package de.mih.core.game.components;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ecs.component.Component;

import java.util.ArrayList;

public class OrderableC extends Component
{

	public BaseOrder             currentorder;
	public BehaviorTree<Integer> btree;
	public boolean               isinit;
	
	ArrayList<BaseOrder> orders = new ArrayList<>();
	
	public void newOrder(BaseOrder order)
	{
		currentorder = order;
		isinit = false;
	}
	
	public void addOrder(BaseOrder order)
	{
		order.entityID = this.entityID;
		if (currentorder == null)
			currentorder = order;
		orders.add(order);
	}
	
	public boolean hasOrder()
	{
		return !orders.isEmpty();
	}
	
	public BaseOrder getOrder()
	{
		BaseOrder first = orders.get(0);
		orders.remove(0);
		return first;
	}

	public void removeOrder(BaseOrder order)
	{
		if (orders.contains(order))
		{
			orders.remove(order);
		}
	}
}
