package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("rawtypes")
public class MoveSystem extends BaseSystem {

	Tilemap map;
	EntityManager entityM = EntityManager.getInstance();
	EventManager eventM = EventManager.getInstance();
	

	public MoveSystem(Game game) {
		super(game);
		this.map = map;
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityM.hasComponent(entityId, PositionC.class)
				&& entityM.hasComponent(entityId, VelocityC.class);
	}

	double tmp;

	@Override
	public void update(double dt, int entity) {
		PositionC pos = entityM.getComponent(entity, PositionC.class);
		VelocityC vel = entityM.getComponent(entity, VelocityC.class);
		if (Math.abs(vel.velocity.x) < 0.5) {
			vel.velocity.x = 0;
		}
		if (Math.abs(vel.velocity.y) < 0.5) {
			vel.velocity.y = 0;
		}
		if (Math.abs(vel.velocity.z) < 0.5) {
			vel.velocity.z = 0;
		}

		if (entityM.hasComponent(entity, ColliderC.class)) {
			checkCollision(entity);
		}

		vel.velocity.x = vel.velocity.x * vel.drag;
		vel.velocity.y = vel.velocity.y * vel.drag;
		vel.velocity.z = vel.velocity.z * vel.drag;

		tmp = pos.position.x + vel.velocity.x * dt;
		pos.position.x = (float) tmp;

		tmp = pos.position.y + vel.velocity.y * dt;
		pos.position.y = (float) tmp;

		tmp = pos.position.z + vel.velocity.z * dt;
		pos.position.z = (float) tmp;
	}

	Vector3 vec_temp = new Vector3();
	Circle temp = new Circle();

	float distx_circrect = 0;
	float disty_circrect = 0;

	void checkCollision(int entity) {
		ColliderC collider = entityM.getComponent(entity, ColliderC.class);
		PositionC position = entityM.getComponent(entity, PositionC.class);
		collider.circle.setPosition(position.position.x, position.position.z);
		for (int i = 0; i < entityM.entityCount; i++) {
			if (entityM.hasComponent(i, ColliderC.class)) {
				calculateCollisionCircle(entity, i);
				continue;
			}
			//TODO: Just check surrounding Tiles!
//			if (entityM.hasComponent(i, NodeC.class) && entityM.getComponent(i, NodeC.class).blocked) {
//				calculateCollisionRect(entity, i);
//				continue;
//			}
		}
	}

	public void calculateCollisionCircle(int entity1, int entity2) {
		ColliderC collider_1 = entityM.getComponent(entity1, ColliderC.class);
		VelocityC velocity_1 = entityM.getComponent(entity1, VelocityC.class);

		ColliderC collider_2 = entityM.getComponent(entity2, ColliderC.class);
		VelocityC velocity_2 = entityM.getComponent(entity2, VelocityC.class);

		temp.radius = collider_1.circle.radius;
		temp.x = collider_1.circle.x + velocity_1.velocity.x * velocity_1.drag * Gdx.graphics.getDeltaTime();
		temp.y = collider_1.circle.y + velocity_1.velocity.z * velocity_1.drag * Gdx.graphics.getDeltaTime();

		if (!Intersector.overlaps(temp, collider_2.circle))
			return;
		vec_temp.x = temp.x - collider_2.circle.x;
		vec_temp.y = temp.y - collider_2.circle.y;
		vec_temp.setLength(((temp.radius + collider_2.circle.radius) - vec_temp.len())
				* ((temp.radius + collider_2.circle.radius) - vec_temp.len()) * 4f);

		velocity_1.velocity.add(vec_temp.x, 0, vec_temp.y);
		velocity_2.velocity.sub(vec_temp.x, 0, vec_temp.y);
	}

	Rectangle rect = new Rectangle();
	Vector2 rect_center = new Vector2();

	public void calculateCollisionRect(int entity1, int entity2) {
//		ColliderC collider = entityM.getComponent(entity1, ColliderC.class);
//		VelocityC velocity = entityM.getComponent(entity1, VelocityC.class);
//
//		NodeC node = entityM.getComponent(entity2, NodeC.class);
//		PositionC pos = entityM.getComponent(entity2, PositionC.class);
//
//		rect.setCenter(pos.position.x, pos.position.z);
//		rect.setHeight(2f);
//		rect.setWidth(2f);
//
//		temp.radius = collider.circle.radius;
//		temp.x = collider.circle.x + velocity.velocity.x * velocity.drag * Gdx.graphics.getDeltaTime();
//		temp.y = collider.circle.y + velocity.velocity.z * velocity.drag * Gdx.graphics.getDeltaTime();
//
//		if (!Intersector.overlaps(temp, rect))
//			return;
//
//		temp.x = collider.circle.x + velocity.velocity.x * velocity.drag * Gdx.graphics.getDeltaTime();
//		temp.y = 0;
//
//		if (Intersector.overlaps(temp, rect)) {
//			velocity.velocity.x = 0;
//		}
//
//		temp.x = 0;
//		temp.y = collider.circle.y + velocity.velocity.z * velocity.drag * Gdx.graphics.getDeltaTime();
//		
//		if (Intersector.overlaps(temp, rect)) {
//			velocity.velocity.z = 0;
//		}
//
//		if (!Intersector.overlaps(collider.circle, rect))
//			return;
//
//		rect.getCenter(rect_center);
//		
//		if (collider.circle.x > rect_center.x)
//			velocity.velocity.x = (rect.width / 2f + collider.circle.radius - collider.circle.x + rect_center.x);
//		else
//			velocity.velocity.x = -(rect.width / 2f + collider.circle.radius - collider.circle.x + rect_center.x);
//
//		if (collider.circle.y > rect_center.y)
//			velocity.velocity.z = (rect.height / 2f + collider.circle.radius - collider.circle.y + rect_center.y);
//		else
//			velocity.velocity.z = -(rect.height / 2f + collider.circle.radius - collider.circle.y + rect_center.y);

	}

	@Override
	public void render(int entity) {

	}

	@Override
	public void update(double dt) {
	}

	@Override
	public void render() {
	}


	@Override
	public void onEventRecieve(BaseEvent event) {
		// TODO Auto-generated method stub
		
	}

}
