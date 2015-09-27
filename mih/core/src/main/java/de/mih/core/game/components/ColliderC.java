package de.mih.core.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.GameStates.GameStateManager;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.MiH;

public class ColliderC extends Component {

	public final static String name = "collider";

	public final static float COLLIDER_RADIUS = 0.5f;

	Rectangle collider = new Rectangle();
	Rectangle navcollider = new Rectangle();
	public ArrayList<NavPoint> navpoints = new ArrayList<NavPoint>();

	public ColliderC() {
		for (int i = 0; i < 4; i++) {
			navpoints.add(new NavPoint());
		}
	}

	public ColliderC(Rectangle collider) {
		this.collider = collider;
		for (int i = 0; i < 4; i++) {
			navpoints.add(new NavPoint());
		}
		calculateNavCollider();
	}

	public Rectangle getCollider() {
		return collider;
	}

	public void setCollider(Rectangle collider) {
		this.collider = collider;
		calculateNavCollider();
	}

	public Rectangle getNavCollider() {
		return navcollider;
	}

	public void setPos(float x, float y) {
		collider.x = x - collider.width / 2f;
		collider.y = y - collider.height / 2f;
		calculateNavCollider();
	}

	public void scale(float width, float height) {
		collider.height = height;
		collider.width = width;
		calculateNavCollider();
	}

	Vector2 tmp = new Vector2();
	NavPoint tmpnavp;

	public void calculateNavCollider() {
		navcollider.set(collider);
		// sin(45°) = 0.8509
		navcollider.setSize(collider.width + (2 * COLLIDER_RADIUS / 0.8509f),
				collider.height + (2 * COLLIDER_RADIUS / 0.8509f));
		collider.getCenter(tmp);
		navcollider.setCenter(tmp.x, tmp.y);
		for (int i = 0; i < 4; i++) {
			tmpnavp = navpoints.get(i);
			switch (i) {
			case (0): {
				tmpnavp.pos.x = navcollider.getX() - 0.05f;
				tmpnavp.pos.y = navcollider.getY() - 0.05f;
				break;
			}
			case (1): {
				tmpnavp.pos.x = navcollider.getX() - 0.05f;
				tmpnavp.pos.y = navcollider.getY() + navcollider.height + 0.05f;
				break;
			}
			case (2): {
				tmpnavp.pos.x = navcollider.getX() + navcollider.width + 0.05f;
				tmpnavp.pos.y = navcollider.getY() + navcollider.height + 0.05f;
				break;
			}
			case (3): {
				tmpnavp.pos.x = navcollider.getX() + navcollider.width + 0.05f;
				tmpnavp.pos.y = navcollider.getY() - 0.05f;
				break;
			}
			}
		}
		if (EntityManager.getInstance().hasComponent(entityID, PositionC.class)
				&& GameStateManager.getInstance().getCurrentGame().tilemap != null) {
			Vector3 pos = EntityManager.getInstance().getComponent(entityID, PositionC.class).getPos();
			Tile t = GameStateManager.getInstance().getCurrentGame().tilemap.getTileAt(
					GameStateManager.getInstance().getCurrentGame().tilemap.coordToIndex_x(pos.x),
					GameStateManager.getInstance().getCurrentGame().tilemap.coordToIndex_z(pos.z));
			Room r = t.getRoom();
			r.calculateVisibility();
		}
	}
	@Override
	public Component cpy() {
		return new ColliderC(this.collider);
	}

	@Override
	public void setField(String fieldName, String fieldValue) {
		// if()
		// System.out.println(fieldName + ": " + fieldValue);
		if (fieldName.equals("rectangle")) {
			String[] split = fieldValue.split(",");
			float width = Float.parseFloat(split[0]);
			float height = Float.parseFloat(split[1]);
			Rectangle rect = new Rectangle();
			rect.width = width;
			rect.height = height;
			this.collider = rect;
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
