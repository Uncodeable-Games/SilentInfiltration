package de.mih.core.game.components;

import com.badlogic.gdx.ai.btree.BehaviorTree;

import de.mih.core.engine.ai.BTreeParser;
import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ecs.Component;
import de.mih.core.game.ai.orders.MoveOrder;

public class OrderableC extends Component {

	public BaseOrder currentorder;
	public BehaviorTree<Integer> btree;
	public boolean isinit;
	
	public void newOrder(BaseOrder order){
		currentorder = order;
		isinit = false;
	}

	@Override
	public void setField(String fieldName, String fieldValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component cpy() {
		// TODO Auto-generated method stub
		return null;
	}
}
