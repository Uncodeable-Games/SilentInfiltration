package de.mih.core.game.input.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.mih.core.game.input.ui.UserInterface.Border;

import java.util.ArrayList;

public class Button {

    public Rectangle rect = new Rectangle();

    Border border;
    public Vector2 fixedoffset = new Vector2();
    public Vector2 ratiooffset = new Vector2();

    Texture texture;

    boolean visible = true;

    ArrayList<ClickListener> clickListeners = new ArrayList<>();
    ArrayList<HoverListener> hoverListeners = new ArrayList<>();

    ArrayList<Button> children = new ArrayList<>();

    public Button(Border b, float fix_x, float fix_y, float ratio_x, float ratio_y, Texture t) {
        this.border = b;
        fixedoffset.x = fix_x;
        fixedoffset.y = fix_y;
        ratiooffset.x = ratio_x;
        ratiooffset.y = ratio_y;
        rect.width = t.getWidth();
        rect.height = t.getHeight();
        this.texture = t;
        calculatePosition();
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        visible = true;
    }

    public void addClicklistener(ClickListener l) {
        clickListeners.add(l);
    }

    public void addHoverListener(HoverListener l) {
        hoverListeners.add(l);
    }

    public void addChild(Button child){
        children.add(child);
    }

    public ArrayList<Button> getChildren(){
        return children;
    }

    public void calculatePosition() {
        switch (border) {
            case BOTTOM_LEFT: {
                rect.x = (Gdx.graphics.getWidth() - rect.width / 2f) * ratiooffset.x + fixedoffset.x;
                rect.y = (Gdx.graphics.getHeight() - rect.height / 2f) * ratiooffset.y + fixedoffset.y;
                break;
            }
            case BOTTOM_RIGHT: {
                rect.x = Gdx.graphics.getWidth() - texture.getWidth()
                        + (Gdx.graphics.getWidth() - rect.width / 2f) * ratiooffset.x + fixedoffset.x;
                rect.y = (Gdx.graphics.getHeight() - rect.height / 2f) * ratiooffset.y + fixedoffset.y;
                break;
            }
            case TOP_LEFT: {
                rect.x = (Gdx.graphics.getWidth() - rect.width / 2f) * ratiooffset.x + fixedoffset.x;
                rect.y = Gdx.graphics.getHeight() - texture.getHeight()
                        + (Gdx.graphics.getHeight() - rect.height / 2f) * ratiooffset.y + fixedoffset.y;
                break;
            }
            case TOP_RIGHT: {
                rect.x = Gdx.graphics.getWidth() - texture.getWidth()
                        + (Gdx.graphics.getWidth() - rect.width / 2f) * ratiooffset.x + fixedoffset.x;
                rect.y = Gdx.graphics.getHeight() - texture.getHeight()
                        + (Gdx.graphics.getHeight() - rect.height / 2f) * ratiooffset.y + fixedoffset.y;
                break;
            }
        }
    }
}
