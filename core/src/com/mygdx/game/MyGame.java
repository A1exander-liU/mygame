package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.LoadingScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.SettingsScreen;

public class MyGame extends Game {
	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;
	SettingsScreen settingsScreen;
	LoadingScreen loadingScreen;

	private StoredPreferences storedPreferences;

	public static final int MENU_SCREEN = 0;
	public static final int GAME_SCREEN = 1;
	public static final int SETTINGS_SCREEN = 2;
	public static final int LOADING_SCREEN = 3;

	public SpriteBatch batch;
	public Engine engine;

	public StoredPreferences getStoredPreferences() {
		return storedPreferences;
	}

	@Override
	public void create() {
		this.changeScreen(MyGame.MENU_SCREEN);

		storedPreferences = new StoredPreferences();
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
		}
	}
}
