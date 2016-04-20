package de.mih.core.game.player;

import com.badlogic.gdx.graphics.Texture;
import de.mih.core.engine.ability.Ability;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.Game;
import de.mih.core.game.components.AbilityC;
import de.mih.core.game.input.ui.Button;
import de.mih.core.game.input.ui.UserInterface;

public class Player
{

	public enum PlayerType
	{
		Security,
		Attacker
	}
	
	private EntityManager entityM;
	
	private String name;
	private int    id;

	private boolean targeting = false;
	private Ability abilityBeingTargeted;

	private PlayerType playerType;

	private int hero = -1;
	
	public Player(String name, int id, PlayerType playerType)
	{
		this.name = name;
		this.id = id;
		this.playerType = playerType;

		entityM = Game.getCurrentGame().getEntityManager();
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public PlayerType getPlayerType()
	{
		return playerType;
	}

	public int getHero()
	{
		return hero;
	}

	public void setHero(int id)
	{
		this.hero = id;

		int i = 0;
		for (Integer abilid : entityM.getComponent(id, AbilityC.class).getAbilityIdList())
		{
			Ability ability = Game.getCurrentGame().getAbilityManager().getAbilityById(abilid);

			Button button = new Button(UserInterface.Border.BOTTOM_RIGHT, i, 0, 0, 0, Game.getCurrentGame().getAssetManager().assetManager.get(ability.getIconPath(), Texture.class));
			button.addClicklistener(() -> {
				ability.castNoTarget(id);
			});


			Game.getCurrentGame().getUI().addButton(button);
			i -= 52;
		}
	}

	public boolean isTargeting()
	{
		return targeting;
	}

	public void setTargeting(boolean targeting)
	{
		this.targeting = targeting;
	}

	public Ability getAbilityBeingTargeted()
	{
		return abilityBeingTargeted;
	}

	public void setAbilityBeingTargeted(Ability abilityBeingTargeted)
	{
		this.abilityBeingTargeted = abilityBeingTargeted;
	}
}
