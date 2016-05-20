package de.mih.core.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

@SuppressWarnings("rawtypes")
public class MoveSystem extends BaseSystem
{

	Tilemap       map;
	EntityManager entityM;
	EventManager  eventM;

	public MoveSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game);
		this.entityM = game.getEntityManager();
		this.eventM = game.getEventManager();
		this.map = game.getTilemap();
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return entityM.hasComponent(entityId, PositionC.class) && entityM.hasComponent(entityId, VelocityC.class);
	}

	double tmp;

	@Override
	public void update(double dt, int entity)
	{
		PositionC pos = entityM.getComponent(entity, PositionC.class);
		VelocityC vel = entityM.getComponent(entity, VelocityC.class);
		if (Math.abs(vel.velocity.x) < 0.5)
		{
			vel.velocity.x = 0;
		}
		if (Math.abs(vel.velocity.y) < 0.5)
		{
			vel.velocity.y = 0;
		}
		if (Math.abs(vel.velocity.z) < 0.5)
		{
			vel.velocity.z = 0;
		}
		
		if (vel.velocity.isZero() || vel.velocity.len() <= 0.1f)
			return;
		if (entityM.hasComponent(entity, ColliderC.class))
		{
			checkCollision(entity);
		}

		vel.velocity.add(vel.steering);
		vel.velocity.setLength(vel.maxspeed);

		double angle = Math.atan2(vel.velocity.x, vel.velocity.z);
		pos.setAngle((float) Math.toDegrees(angle));
		pos.setFacing(vel.velocity.cpy().nor());

		if (vel.velocity.x != 0 || vel.velocity.y != 0 || vel.velocity.z != 0)
		{
			pos.setPos((float) (pos.getX() + vel.velocity.x * dt), (float) (pos.getY() + vel.velocity.y * dt), (float) (pos.getZ() + vel.velocity.z * dt));
		}
	}

	private Vector3 vec_temp = new Vector3();
	private Circle  temp     = new Circle();

	private void checkCollision(int entity)
	{
		for (int i = 0; i < entityM.entityCount; i++)
		{
			if (entityM.hasComponent(i, ColliderC.class))
			{
				calcuteCollision(entity, i);
			}
		}
	}

	public void calcuteCollision(int entity1, int entity2)
	{
		/*
		 * ColliderC collider_1 = entityM.getComponent(entity1,
		 * ColliderC.class); VelocityC velocity_1 =
		 * entityM.getComponent(entity1, VelocityC.class);
		 * 
		 * PositionC position_2 = entityM.getComponent(entity2,
		 * PositionC.class); ColliderC collider_2 =
		 * entityM.getComponent(entity2, ColliderC.class); VelocityC velocity_2
		 * = entityM.hasComponent(entity2, VelocityC.class) ?
		 * entityM.getComponent(entity2, VelocityC.class) : null;
		 * 
		 * if(collider_1.collider instanceof Circle && collider_2.collider
		 * instanceof Circle){ if(velocity_2 == null) {
		 * calculateCollisionCircle((Circle)collider_1.collider,
		 * (Circle)collider_2.collider, velocity_1); } else {
		 * calculateCollisionCircle((Circle)collider_1.collider,
		 * (Circle)collider_2.collider, velocity_1, velocity_2); } }
		 * if(collider_1.collider instanceof Circle && collider_2.collider
		 * instanceof Rectangle) { calculateCollisionRect((Circle)
		 * collider_1.collider, (Rectangle) collider_2.collider, velocity_1,
		 * position_2); }
		 */
	}

	public void calculateCollisionCircle(Circle c1, Circle c2, VelocityC v1)
	{
		calculateCollisionCircle(c1, c2, v1, null);
	}

	public void calculateCollisionCircle(Circle c1, Circle c2, VelocityC v1, VelocityC v2)
	{
		temp.radius = c1.radius;
		temp.x = c1.x + v1.velocity.x * Gdx.graphics.getDeltaTime();
		temp.y = c1.y + v1.velocity.z * Gdx.graphics.getDeltaTime();

		if (!Intersector.overlaps(temp, c2))
			return;
		vec_temp.x = temp.x - c2.x;
		vec_temp.y = temp.y - c2.y;
		vec_temp.setLength(
				((temp.radius + c2.radius) - vec_temp.len()) * ((temp.radius + c2.radius) - vec_temp.len()) * 4f);

		v1.velocity.add(vec_temp.x, 0, vec_temp.y);
		if (v2 != null)
			v2.velocity.sub(vec_temp.x, 0, vec_temp.y);
	}

	private Rectangle rect        = new Rectangle();
	private Vector2   rect_center = new Vector2();

	public void calculateCollisionRect(Circle c1, Rectangle c2, VelocityC v1, PositionC p2)
	{

		temp.radius = c1.radius;
		temp.x = c1.x + v1.velocity.x * v1.drag * Gdx.graphics.getDeltaTime();
		temp.y = c1.y + v1.velocity.z * v1.drag * Gdx.graphics.getDeltaTime();

		if (!Intersector.overlaps(temp, rect))
			return;

		temp.x = c1.x + v1.velocity.x * v1.drag * Gdx.graphics.getDeltaTime();
		temp.y = 0;

		if (Intersector.overlaps(temp, rect))
		{
			v1.velocity.x = 0;
		}

		temp.x = 0;
		temp.y = c1.y + v1.velocity.z * v1.drag * Gdx.graphics.getDeltaTime();

		if (Intersector.overlaps(temp, rect))
		{
			v1.velocity.z = 0;
		}

		if (!Intersector.overlaps(c1, c2))
			return;

		// c2.getCenter(rect_center);
		rect_center.x = p2.getX();
		rect_center.y = p2.getZ();
		float xRotate = (float) (Math.cos(p2.getAngle()) * (c1.x - rect_center.x)
				- Math.sin(p2.getAngle()) * (c1.y - rect_center.y) + rect_center.x);
		float yRotate = (float) (Math.sin(p2.getAngle()) * (c1.x - rect_center.x)
				- Math.cos(p2.getAngle()) * (c1.y - rect_center.y) + rect_center.x);

		Vector2 cRotatet = new Vector2(xRotate, yRotate);

		if (cRotatet.x > rect_center.x)
			v1.velocity.x = (rect.width / 2f + c1.radius - cRotatet.x + rect_center.x);
		else
			v1.velocity.x = -(rect.width / 2f + c1.radius - cRotatet.x + rect_center.x);

		if (cRotatet.y > rect_center.y)
			v1.velocity.z = (rect.height / 2f + c1.radius - cRotatet.y + rect_center.y);
		else
			v1.velocity.z = -(rect.height / 2f + c1.radius - cRotatet.y + rect_center.y);
	}

	@Override
	public void update(double dt)
	{
	}

}
