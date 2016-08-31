package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.gob.Action;
import de.mih.core.engine.ai.gob.Discontentment;
import de.mih.core.engine.ai.gob.Discontentment.goalNames;
import de.mih.core.engine.ai.gob.GobComponent;
import de.mih.core.engine.ai.gob.GobState;
import de.mih.core.engine.ai.gob.ItemComponent;
import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;

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
		game.getAssetManager().demo(); //For demo models
		
		Discontentment discTest = new Discontentment();
		discTest.addGoal(goalNames.HUNGER, -20);
		//discTest.addGoal(goalNames.SLEEP, 5);
		discTest.addGoal(goalNames.FUN, 10);
		
		Discontentment changesSleep = new Discontentment();
		changesSleep.addGoal(goalNames.FUN, 10);
		//changesSleep.addGoal(goalNames.FUN, 20);
		changesSleep.addGoal(goalNames.SLEEP, -20);
		
		Discontentment danceDisc = new Discontentment();
		danceDisc.addGoal(goalNames.FUN, -20);
		danceDisc.addGoal(goalNames.SLEEP, 10);
		//danceDisc.addGoal(goalNames.HUNGER, 1);
		
		Discontentment overTime = new Discontentment();
		overTime.addGoal(goalNames.HUNGER, 0.5);
		overTime.addGoal(goalNames.SLEEP, 0.5);
		overTime.addGoal(goalNames.FUN, 0.5);
		
		ItemComponent test = new ItemComponent();
		test.itemName = "test";
	//	test.usableActions.add(new Action("Eat", 5, discTest));
		test.usableActions.add(new Action("Sleep", 5, changesSleep));
	//	test.usableActions.add(new Action("Dance", 5, danceDisc));
		
		GobState test2 = new GobState();
		test2.disc.addGoal(goalNames.HUNGER, 60);
		test2.addItem(test);
		
		System.out.println(test2.disc);
		///System.out.println(discTest);
		//test.usableActions.get(0).apply(test2, overTime);
		//System.out.println(test2.disc);
		
		Discontentment eatSnack = new Discontentment();
		eatSnack.addGoal(goalNames.HUNGER, -20);
		eatSnack.addGoal(goalNames.SLEEP, 3);
		
		Discontentment danceOnFloor = new Discontentment();
		danceOnFloor.addGoal(goalNames.FUN, -30);
		danceOnFloor.addGoal(goalNames.HUNGER, 5);
		
		Discontentment sitOnChair = new Discontentment();
		sitOnChair.addGoal(goalNames.SLEEP, -20);
		sitOnChair.addGoal(goalNames.FUN, 8);
		
		
		ItemComponent snacking = new ItemComponent();
		snacking.itemName = "Snackbar";
		Action snackAction = new Action("Eat snack", 10, eatSnack);
		snackAction.addUsedItem(snacking);
		snacking.usableActions.add(snackAction);
		
		int snackBar = game.getEntityManager().createEntity();
		game.getEntityManager().addComponent(snackBar, new PositionC(new Vector3(12,0,50)), snacking, new VisualC("redbox"));
	
		ItemComponent dancing = new ItemComponent();
		dancing.itemName = "Dancefloor";
		Action danceAction = new Action("Dance, dance, dance!", 15, danceOnFloor);
		danceAction.addUsedItem(dancing);
		dancing.usableActions.add(danceAction);
		
		int danceFloor = game.getEntityManager().createEntity();
		game.getEntityManager().addComponent(danceFloor, new PositionC(new Vector3(12,0,58)), dancing, new VisualC("floor"));
		
		int needyBot = game.getBlueprintManager().createEntityFromBlueprint("robocop.json");
		game.getEntityManager().getComponent(needyBot, PositionC.class).setPos(8, 0, 54);
		
		GobComponent needyBotsGobC = new GobComponent(test2, overTime);
		needyBotsGobC.changePerTimeStep = overTime;
		game.getEntityManager().addComponent(needyBot, needyBotsGobC);
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
