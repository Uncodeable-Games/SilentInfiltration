package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;

import java.util.HashMap;

public class StateMachineComponent extends Component
{
	
	public HashMap<String, State> states = new HashMap<>();
	public State current;
	
	public static abstract class State
	{
		public StateMachineComponent stateMachine;

		public State(StateMachineComponent stateMachine)
		{
			this.stateMachine = stateMachine;
		}

		public abstract void onEnter();

		public abstract void onLeave();

		public abstract void update();
	}

	;
	
	public void addState(String name, State state)
	{
		states.put(name, state);
	}
	
	public void changeGameState(String newState)
	{
		current.onLeave();
		current = states.get(newState);
		current.onEnter();
	}
}
