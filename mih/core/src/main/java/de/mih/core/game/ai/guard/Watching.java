package de.mih.core.game.ai.guard;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.StateMachineComponent;
import de.mih.core.game.components.StateMachineComponent.State;

public class Watching extends State
{

	public boolean timed = false;
	public double timeLimit = 0f;
	public double time = 0f;
	public String timer_nextState = "";
	
	public float maxFacing;
	public float minFacing;
	public float rotateSpeed;
	boolean up = true;
	
	private Game game;
	
	public Watching(StateMachineComponent stateMachine, Game game) {
		super(stateMachine);
		this.game = game;
	}

	
	@Override
	public void onEnter()
	{
		int self = this.stateMachine.entityID;
		PositionC posC = game.getEntityManager().getComponent(self, PositionC.class);
		float currentAngle = posC.getAngle();
		if(currentAngle >= minFacing && currentAngle <= maxFacing)
			up = true;
		else 
			up = currentAngle > maxFacing;
	//	time = 0;

	}

	@Override
	public void onLeave()
	{
		time = 0;
	}

	@Override
	public void update(double deltaTime)
	{
		if(timed)
		{
			time += deltaTime;
//			System.out.println(time + " " + timeLimit);
		}
		int self = this.stateMachine.entityID;
		PositionC posC = game.getEntityManager().getComponent(self, PositionC.class);
		float currentAngle = posC.getAngle();
		if(up)
		{
			if(currentAngle <= maxFacing)
				currentAngle += rotateSpeed;
			else
				up = false;
		}
		else
		{
			if(currentAngle >= minFacing)
				currentAngle -= rotateSpeed;
			else
				up = true;
		}
		float radian = (float) Math.toRadians(currentAngle);
		posC.facing.x = -(float) ( Math.cos(radian));
		posC.facing.z = (float) ( Math.sin(radian));

		posC.setAngle(currentAngle);
		if(timed && time >= timeLimit)
		{
			stateMachine.changeGameState(timer_nextState);
//			System.out.println(stateMachine.current);
		}
	}

}
