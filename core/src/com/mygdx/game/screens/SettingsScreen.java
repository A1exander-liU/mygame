package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;


public class SettingsScreen implements Screen {
    public MyGame parent;
    Stage stage;
    Skin skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));

    private List<String> settingsOptions;
    private Label audioTitleLabel;
    private Label graphicsTitleLabel;
    private Label gameTitleLabel;
    private Label controlsTitleLabel;

    private Label musicVolumeLabel;
    private Label isMusicEnabledLabel;
    private Label soundVolumeLabel;
    private Label isSoundEnabledLabel;

    final Slider musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
    final Slider soundVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
    final CheckBox musicEnabled = new CheckBox(null, skin);
    final CheckBox soundEnabled = new CheckBox(null, skin);

    public SettingsScreen(MyGame parent) {
        this.parent = parent;

        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        stage.clear();

        FreeTypeFontGenerator generator  = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter);
        skin.add("pixel2D", font);
        skin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        skin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));

        Gdx.input.setInputProcessor(stage);
        final Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);

        settingsOptions = new List<>(skin);
        settingsOptions.setItems("Audio", "Graphics", "Game", "Controls", "Back");

        audioTitleLabel = new Label("Audio", skin, "pixel2D", Color.BLACK);
        graphicsTitleLabel = new Label("Graphics", skin, "pixel2D", Color.BLACK);
        gameTitleLabel = new Label("Game", skin, "pixel2D", Color.BLACK);
        controlsTitleLabel = new Label("Controls", skin, "pixel2D", Color.BLACK);

        musicVolumeLabel = new Label("Music Volume", skin, "pixel2D", Color.BLACK);
        soundVolumeLabel = new Label("Sound Volume", skin, "pixel2D", Color.BLACK);
        isMusicEnabledLabel = new Label("Music", skin, "pixel2D", Color.BLACK);
        isSoundEnabledLabel = new Label("Sound", skin, "pixel2D", Color.BLACK);


        table.defaults().uniform().space(10);

        final Table optionsTable = new Table();
        final Table selectedTab = new Table();
        optionsTable.setDebug(false);
        selectedTab.setDebug(false);

        selectedTab.defaults().space(10);
        optionsTable.defaults().space(10);
        optionsTable.setName("settingsSideMenu");
        selectedTab.setName("settingsStuff");

        optionsTable.add(settingsOptions).expand().fill().left();

        optionsTable.add(selectedTab).center().top().padTop(10).expand();
        table.add(optionsTable).spaceRight(0).left().pad(20).expand().fill();

        showAudioSettings(selectedTab);

        settingsOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String currentTab = settingsOptions.getSelected();
                System.out.println(currentTab);
                updateTab(currentTab, selectedTab);
            }
        });

        musicVolumeSlider.setValue(parent.getStoredPreferences().getMusicVolume());
        musicVolumeSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                parent.getStoredPreferences().setMusicVolume(musicVolumeSlider.getValue());
                return false;
            }
        });

        soundVolumeSlider.setValue(parent.getStoredPreferences().getSoundVolume());
        soundVolumeSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                parent.getStoredPreferences().setSoundVolume(soundVolumeSlider.getValue());
                return false;
            }
        });

        musicEnabled.setChecked(parent.getStoredPreferences().isMusicEnabled());
        musicEnabled.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicEnabled.isChecked();
                parent.getStoredPreferences().setIsMusicEnabled(enabled);
                return false;
            }
        });

       soundEnabled.setChecked(parent.getStoredPreferences().isSoundEnabled());
       soundEnabled.addListener(new EventListener() {
           @Override
           public boolean handle(Event event) {
               boolean enabled = soundEnabled.isChecked();
               parent.getStoredPreferences().setIsSoundEnabled(enabled);
               return false;
           }
       });
    }

    private void showAudioSettings(Table selectedTab) {
        selectedTab.clearChildren();
        selectedTab.add(audioTitleLabel).colspan(2).center();
        selectedTab.row();
        selectedTab.add(musicVolumeLabel).left();
        selectedTab.add(musicVolumeSlider);
        selectedTab.row();
        selectedTab.add(isMusicEnabledLabel).left();
        selectedTab.add(musicEnabled);
        selectedTab.row();
        selectedTab.add(soundVolumeLabel).left();
        selectedTab.add(soundVolumeSlider);
        selectedTab.row();
        selectedTab.add(isSoundEnabledLabel).left();
        selectedTab.add(soundEnabled);
    }

    private void showGraphicsSettings(Table selectedTab) {
        selectedTab.clearChildren();
        selectedTab.add(graphicsTitleLabel).colspan(2).center();
        selectedTab.row();
        selectedTab.add(musicVolumeLabel).left();
        selectedTab.add(musicVolumeSlider);
        selectedTab.row();
        selectedTab.add(isMusicEnabledLabel).left();
        selectedTab.add(musicEnabled);
        selectedTab.row();
        selectedTab.add(soundVolumeLabel).left();
        selectedTab.add(soundVolumeSlider);
        selectedTab.row();
        selectedTab.add(isSoundEnabledLabel).left();
        selectedTab.add(soundEnabled);
    }

    private void showGameSettings(Table selectedTab) {
        selectedTab.clearChildren();
        selectedTab.add(gameTitleLabel).colspan(2).center();
        selectedTab.row();
        selectedTab.add(musicVolumeLabel).left();
        selectedTab.add(musicVolumeSlider);
        selectedTab.row();
        selectedTab.add(isMusicEnabledLabel).left();
        selectedTab.add(musicEnabled);
        selectedTab.row();
        selectedTab.add(soundVolumeLabel).left();
        selectedTab.add(soundVolumeSlider);
        selectedTab.row();
        selectedTab.add(isSoundEnabledLabel).left();
        selectedTab.add(soundEnabled);
    }

    private void showControlsSettings(Table selectedTab) {
        selectedTab.clearChildren();
        selectedTab.add(controlsTitleLabel).colspan(2).center();
        selectedTab.row();
        selectedTab.add(musicVolumeLabel).left();
        selectedTab.add(musicVolumeSlider);
        selectedTab.row();
        selectedTab.add(isMusicEnabledLabel).left();
        selectedTab.add(musicEnabled);
        selectedTab.row();
        selectedTab.add(soundVolumeLabel).left();
        selectedTab.add(soundVolumeSlider);
        selectedTab.row();
        selectedTab.add(isSoundEnabledLabel).left();
        selectedTab.add(soundEnabled);
    }

    private void updateTab(String currentTab, Table selectedTab) {
        switch (currentTab) {
            case "Audio":
                showAudioSettings(selectedTab);
                break;
            case "Graphics":
                showGraphicsSettings(selectedTab);
                break;
            case "Game":
                showGameSettings(selectedTab);
                break;
            case "Controls":
                showControlsSettings(selectedTab);
                break;
            case "Back":
                parent.changeScreen(MyGame.MENU_SCREEN);
                break;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8824f, 0.7765f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
