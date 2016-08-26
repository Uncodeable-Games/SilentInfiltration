package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;

import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;

public class PlayingGameState extends GameState
{

	public PlayingGameState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	public Game game;

	@Override
	public void onEnter()
	{
		game = new Game();
		game.init("assets/maps/map1.xml");
		
		int robo = game.getBlueprintManager().createEntityFromBlueprint("robocop.json");
		game.getEntityManager().getComponent(robo, PositionC.class).setPos(8, 0, 53);

		game.getActivePlayer().setHero(robo);

	}

	// TODO: reorganize!
	@Override
	public void update()
	{
		game.update(Gdx.graphics.getDeltaTime());
		if (game.isGameOver)
		{
			gamestateManager.changeGameState("MAIN_MENU");
		}
	}

	@Override
	public void render()
	{
		game.render();
	}

	@Override
	public void onLeave()
	{
	}

	@Override
	public void resize(int width, int height)
	{
		game.getRenderManager().spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		game.getUI().resize(width, height);
	}
}
