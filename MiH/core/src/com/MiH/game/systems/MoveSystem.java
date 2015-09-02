package com.MiH.game.systems;

import com.MiH.engine.ecs.BaseSystem;
import com.MiH.engine.ecs.EntityManager;
import com.MiH.engine.ecs.Event;
import com.MiH.engine.ecs.EventManager;
import com.MiH.engine.ecs.SystemManager;
import com.MiH.game.components.ColliderC;
import com.MiH.game.components.NodeC;
import com.MiH.game.components.PositionC;
import com.MiH.game.components.TilemapC;
import com.MiH.game.components.VelocityC;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("rawtypes")
public class MoveSystem extends BaseSystem {

	TilemapC map;

	public MoveSystem(SystemManager systemManager, EntityManager entityManager, EventManager eventManager,
			TilemapC tilemap) {
		super(systemManager, entityManager, eventManager);
		map = tilemap;
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, PositionC.class)
				&& entityManager.hasComponent(entityId, VelocityC.class);
	}

	double tmp;

	@Override
	public void update(double dt, int entity) {
		PositionC pos = entityManager.getComponent(entity, PositionC.class);
		VelocityC vel = entityManager.getComponent(entity, VelocityC.class);
		if (Math.abs(vel.velocity.x) < 0.5) {
			vel.velocity.x = 0;
		}
		if (Math.abs(vel.velocity.y) < 0.5) {
			vel.velocity.y = 0;
		}
		if (Math.abs(vel.velocity.z) < 0.5) {
			vel.velocity.z = 0;
		}

		if (entityManager.hasComponent(entity, ColliderC.class)) {
			ColliderC collider = entityManager.getComponent(entity, ColliderC.class);
			PositionC position = entityManager.getComponent(entity, PositionC.class);
			collider.circle.setPosition(position.position.x, position.position.z);
			for (int i = 0; i < entityManager.entityCount; i++) {
				if (entityManager.hasComponent(i, ColliderC.class)) {
					calculateCollisionCircle(entity, i);
					continue;
				}
				if (entityManager.hasComponent(i, NodeC.class)) {
					calculateCollisionRect(entity, i);
					continue;
				}

			}
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

	Vector2 vec_temp = new Vector2();
	Circle temp = new Circle();

	float distx_circrect = 0;
	float disty_circrect = 0;

	public void calculateCollisionCircle(int entity1, int entity2) {
		ColliderC collider_1 = entityManager.getComponent(entity1, ColliderC.class);
		VelocityC velocity_1 = entityManager.getComponent(entity1, VelocityC.class);

		ColliderC collider_2 = entityManager.getComponent(entity2, ColliderC.class);
		VelocityC velocity_2 = entityManager.getComponent(entity2, VelocityC.class);

		temp.radius = collider_1.circle.radius;
		temp.x = collider_1.circle.x + velocity_1.velocity.x * velocity_1.drag * Gdx.graphics.getDeltaTime();
		temp.y = collider_1.circle.y + velocity_1.velocity.z * velocity_1.drag * Gdx.graphics.getDeltaTime();

		if (!Intersector.overlaps(temp, collider_2.circle))
			return;
		vec_temp.x = temp.x - collider_2.circle.x;
		vec_temp.y = temp.y - collider_2.circle.y;
		vec_temp.setLength(((temp.radius + collider_2.circle.radius) - vec_temp.len()) * 4f);
		velocity_1.velocity.add(vec_temp.x, 0, vec_temp.y);
		velocity_2.velocity.sub(vec_temp.x, 0, vec_temp.y);
	}

	Rectangle rect = new Rectangle();

	public void calculateCollisionRect(int entity1, int entity2) {
		ColliderC collider = entityManager.getComponent(entity1, ColliderC.class);
		VelocityC velocity = entityManager.getComponent(entity1, VelocityC.class);

		NodeC node = entityManager.getComponent(entity2, NodeC.class);
		PositionC pos = entityManager.getComponent(entity2, PositionC.class);

		rect.setCenter(pos.position.x, pos.position.z);
		rect.setHeight(NodeC.TILE_SIZE);
		rect.setWidth(NodeC.TILE_SIZE);

		temp.radius = collider.circle.radius;
		temp.x = collider.circle.x + velocity.velocity.x * velocity.drag * Gdx.graphics.getDeltaTime();
		temp.y = collider.circle.y + velocity.velocity.z * velocity.drag * Gdx.graphics.getDeltaTime();

		if (!Intersector.overlaps(temp, rect))
			return;

	}

	@Override
	public void render(int entity) {

	}

	@Override
	public void receiveEvent(Event e, double dt) {
	}

	@Override
	public void update(double dt) {
	}

	@Override
	public void render() {
	}

}
