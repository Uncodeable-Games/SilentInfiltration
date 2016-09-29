package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.engine.levedit.levedit;

public class MainMenuGameState extends GameState
{

	public MainMenuGameState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	Skin        skin;
	Stage       stage;
	SpriteBatch batch;

	@Override
	public void onEnter()
	{
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// A skin can be loaded via JSON or defined programmatically, either is
		// fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region,
		// etc as a drawable, tinted drawable, etc.
		skin = new Skin();

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		skin.add("default", new BitmapFont());

		// Configure a TextButtonStyle and name it "default". Skin resources are
		// stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		final TextButton button = new TextButton("Start", skin);
		table.add(button).row();

		button.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				MainMenuGameState.this.gamestateManager.changeGameState("PLAYING");
			}
		});
		
		final TextButton lobbyStart = new TextButton("Lobby test", skin);
		lobbyStart.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				MainMenuGameState.this.gamestateManager.changeGameState("LOBBY");
			}
			
		});
		table.add(lobbyStart).row();

        final TextButton bt_levedit = new TextButton("Leveleditor", skin);
        bt_levedit.addListener(new ChangeListener(){

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                new levedit().startlevedit();
            }

        });
        table.add(bt_levedit).row();
		
		final TextButton button2 = new TextButton("Options", skin);
		table.add(button2).row();
		
		final TextButton button3 = new TextButton("Exit", skin);
		table.add(button3).row();
		
		button3.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				//	MiH.getInstance().
				Gdx.app.exit();
			}
		});
	}

	@Override
	public void update()
	{
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void onLeave()
	{
	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub

	}
}
