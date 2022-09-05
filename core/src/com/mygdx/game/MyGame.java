package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.engine.utils.EntityToMapAdder;
import com.mygdx.game.jsonreaders.JsonEnemyFinder;
import com.mygdx.game.jsonreaders.JsonItemFinder;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.LoadingScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.SaveSlotScreen;
import com.mygdx.game.screens.SettingsScreen;
import com.mygdx.game.utils.map.GameMapProperties;

public class MyGame extends Game {
	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;
	SettingsScreen settingsScreen;
	LoadingScreen loadingScreen;
	SaveSlotScreen saveSlotScreen;

	public InputMultiplexer inputMultiplexer;

	private StoredPreferences storedPreferences;
	private SaveStates saveStates;

	public static final int MENU_SCREEN = 0;
	public static final int GAME_SCREEN = 1;
	public static final int SETTINGS_SCREEN = 2;
	public static final int LOADING_SCREEN = 3;
	public static final int SAVE_SLOT_SCREEN = 4;

	public SpriteBatch batch;
	public static Engine engine;
	public static GameMapProperties gameMapProperties;
	public JsonEnemyFinder jsonSearcher;
	public JsonItemFinder itemFinder;
	public EntityToMapAdder entityToMapAdder;

	public StoredPreferences getStoredPreferences() {
		return storedPreferences;
	}
	public SaveStates getSaveStates() {
		return saveStates;
	}

	@Override
	public void create() {
		inputMultiplexer = new InputMultiplexer();
		this.changeScreen(MyGame.MENU_SCREEN);

		storedPreferences = new StoredPreferences();
		saveStates = new SaveStates();
	}

	public void changeScreen(int screen) {
		switch (screen) {
			case MENU_SCREEN:
				if (mainMenuScreen == null)
					mainMenuScreen = new MainMenuScreen(this);
				this.setScreen(mainMenuScreen);
				break;
			case GAME_SCREEN:
				if (gameScreen == null)
					gameScreen = new GameScreen(this);
				this.setScreen(gameScreen);
				break;
			case SETTINGS_SCREEN:
				if (settingsScreen == null)
					settingsScreen = new SettingsScreen(this);
				this.setScreen(settingsScreen);
				break;
			case LOADING_SCREEN:
				if (loadingScreen == null)
					loadingScreen = new LoadingScreen(this);
				this.setScreen(loadingScreen);
				break;
			case SAVE_SLOT_SCREEN:
				if (saveSlotScreen == null)
					saveSlotScreen = new SaveSlotScreen(this);
				this.setScreen(saveSlotScreen);
				break;
		}
	}
}
