package de.mih.core.engine.ai.gob;

import java.util.ArrayList;
import java.util.List;

import de.mih.core.engine.ecs.component.Component;
/***
 * Komponente für das Goal oriented behaviour in dem genutzten Entity-Component-System
 * Enthält den aktuellen Zustand, ein Discontentment Objekt in dem Bedürfnisse liegen, die mit jedem Zeitschritt
 * erhöht werden. Dies soll individuelle Bedürfnisse für Entities ermöglichen.
 * 
 * Desweiteren wird die aktuelle Action sowie die geplanten Actions gespeichert.
 * UsedTime und isPerformingAction werden zur Ablaufkontrolle im BehaviourSystem gespeichert
 * @author pathfinder
 *
 */
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
