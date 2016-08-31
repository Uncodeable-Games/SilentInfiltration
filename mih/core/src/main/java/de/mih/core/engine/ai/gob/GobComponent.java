package de.mih.core.engine.ai.gob;

import java.util.ArrayList;
import java.util.List;

import de.mih.core.engine.ecs.component.Component;

public class GobComponent extends Component {

	public GobState state;
	
	public Discontentment changePerTimeStep; //model for basic need development
	
	public ArrayList<Action> plannedActions;
	public Action currentAction = null;
	public double usedTime = 0;
	public boolean isPerformingAction = false;
	//Items
	public GobComponent(GobState start, Discontentment otherTime)
	{
		this.state = start;
		this.changePerTimeStep = otherTime;
		plannedActions = new ArrayList<>();
	}
}
