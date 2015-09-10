package de.mih.core.game.components;

import com.badlogic.gdx.ai.btree.BehaviorTree;

import de.mih.core.engine.ai.BTreeParser;
import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ai.orders.MoveOrder;
import de.mih.core.engine.ecs.Component;

public class OrderableC extends Component {

	public BaseOrder currentorder;
	public BehaviorTree<Integer> btree;
	public boolean isinit;
	
	public void newOrder(BaseOrder order){
		currentorder = order;
		isinit = false;
	}
}
