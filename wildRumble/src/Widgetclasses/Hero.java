package Widgetclasses;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Hero extends Units {

	public Hero(String s, String image_path) {
		super(s, image_path);
	}

	public void input() {

		int speed = 1;

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			speed = 3;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if (cord_y - col > 0) {
				setPos(cord_x, cord_y - 2 * speed);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			if (cord_y + col < Display.getHeight()) {
				setPos(cord_x, cord_y + 2 * speed);
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (cord_x - col > 0) {
				setPos(cord_x - 2 * speed, cord_y);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (cord_x + col < Display.getWidth()) {
				setPos(cord_x + 2 * speed, cord_y);

			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			image_size.x++;
			image_size.y++;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			image_size.x--;
			image_size.y--;
		}

		angle = (int) Math.toDegrees(Math.atan2(Mouse.getY() - cord_y,
				Mouse.getX() - cord_x)) - 90;

	}

}
