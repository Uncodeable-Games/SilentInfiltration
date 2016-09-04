package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.game.ai.gob.Action;
import de.mih.core.game.ai.gob.Discontentment;
import de.mih.core.game.ai.gob.Discontentment.goalNames;
import de.mih.core.game.ai.gob.GobComponent;
import de.mih.core.game.ai.gob.GobState;
import de.mih.core.game.ai.gob.ItemComponent;
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

		game.getAssetManager().demo(); //For demo models
		

		//Festlegung der Discontentment werte
		Discontentment changesSleep = new Discontentment();
		changesSleep.addGoal(goalNames.FUN, 10);
		changesSleep.addGoal(goalNames.SLEEP, -20);
	
		Discontentment idleDisc = new Discontentment(); 

		Discontentment overTime = new Discontentment();
		overTime.addGoal(goalNames.HUNGER, 0.5);
		overTime.addGoal(goalNames.SLEEP, 0.5);
		overTime.addGoal(goalNames.FUN, 0.5);
		
		Discontentment eatSnack = new Discontentment();
		eatSnack.addGoal(goalNames.HUNGER, -20);
		eatSnack.addGoal(goalNames.SLEEP, 3);
		
		Discontentment danceOnFloor = new Discontentment();
		danceOnFloor.addGoal(goalNames.FUN, -30);
		danceOnFloor.addGoal(goalNames.HUNGER, 5);
		
		//Worlditem für immer mögliche Aktionen
		ItemComponent worldItem = new ItemComponent();
		worldItem.itemName = "World item";
		worldItem.usableActions.add(new Action("Sleep", 5, changesSleep));
		worldItem.usableActions.add(new Action("Idle", 5, idleDisc)); //Idle soll unnötiges rumlaufen Verhindern
		//Es gibt Situationen in denen die Discontentment Werte zu Gering sind, dass sich andere Aktionen lohnen,
		// und da MoveTo günstiger ist als anderen Aktionen wird es sonst ausgewählt

		
		//Definition einer Snackbar Entity
		ItemComponent snacking = new ItemComponent();
		snacking.itemName = "Snackbar";
		Action snackAction = new Action("Eat snack", 10, eatSnack);
		snacking.usableActions.add(snackAction);
		
		int snackBar = game.getEntityManager().createEntity();
		game.getEntityManager().addComponent(snackBar, new PositionC(new Vector3(12,0,50)), snacking, new VisualC("redbox"));
	
		//Definition einer Dancefloor Entity
		ItemComponent dancing = new ItemComponent();
		dancing.itemName = "Dancefloor";
		Action danceAction = new Action("Dance, dance, dance!", 15, danceOnFloor);
		dancing.usableActions.add(danceAction);
		
		int danceFloor = game.getEntityManager().createEntity();
		game.getEntityManager().addComponent(danceFloor, new PositionC(new Vector3(12,0,58)), dancing, new VisualC("floor"));
		
		
		//Definition des "NeedyBot" der eine Bedürfniskomponente hat
		int needyBot = game.getBlueprintManager().createEntityFromBlueprint("robocop.json");
		game.getEntityManager().getComponent(needyBot, PositionC.class).setPos(8, 0, 54);
		
		GobState startState = new GobState();
		startState.disc.addGoal(goalNames.HUNGER, 60);
		startState.addItem(worldItem);
		
		GobComponent needyBotsGobC = new GobComponent(startState, overTime);
		needyBotsGobC.changePerTimeStep = overTime;
		game.getEntityManager().addComponent(needyBot, needyBotsGobC);
		
		//Ende der Defnition, ab jetzt läuft alles über die Systeme, 
		// Im Rahmen des Projektes ist nur das Behaviour System von Interesse
		System.out.println(startState.disc);

		for(ItemComponent item : startState.getItems())
		{
			System.out.println(item);
		}
	}

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
