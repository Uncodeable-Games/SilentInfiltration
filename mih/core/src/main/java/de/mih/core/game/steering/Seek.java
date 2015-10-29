package de.mih.core.game.steering;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class Seek extends Steering {

	VelocityC selfVelocity;
	PositionC selfPosition;
	
	public Seek()
	{
		
	}
	public Seek(VelocityC velocity, PositionC position)
	{
		this.selfPosition = position;
		this.selfVelocity = velocity;
	}
	
	@Override
	public void apply(VelocityC velocity, PositionC position) {
		Vector3 currentPosition = selfPosition.getPos();
		Vector3 currentVelocity = selfVelocity.velocity;

		Vector3 desiredPosition = position.getPos();
		Vector3 desiredVelocity = new Vector3(desiredPosition).sub(currentPosition);

		// move with maximum acceleration
		Vector3 accel = new Vector3(desiredVelocity).sub(currentVelocity);// (desiredVelocity-currentVelocity);
		accel.nor();
		//accel *= self->getMaxAccel();

		//self->setAccel(accel);
		selfVelocity.steering = accel;
	}

}
