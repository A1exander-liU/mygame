package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.SaveStates;
import com.mygdx.game.engine.utils.entities.PlayerEntity;
import com.mygdx.game.utils.saves.SaveData;
import com.mygdx.game.utils.ui.StartSaveButton;

import java.util.Objects;

public class SaveSlotScreen implements Screen {
    MyGame parent;
    Stage stage;
    Json json;

    public SaveSlotScreen(MyGame parent) {
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
        json = new Json();
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        Skin gameUiSkin = new Skin();

        FreeTypeFontGenerator generator  = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter);
        gameUiSkin.add("pixel2D", font);
        gameUiSkin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        gameUiSkin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));

        TextButton backButton = new TextButton("Back", gameUiSkin);
        backButton.setLabel(new Label("Back", gameUiSkin, "pixel2D", Color.BLACK));
        backButton.getLabel().setAlignment(Align.center);

        Table saveSlot1 = new Table(gameUiSkin);
        saveSlot1.setBackground(gameUiSkin.getDrawable("window-bg"));
        Label saveSlot1Title = new Label("Slot 1", gameUiSkin, "pixel2D", Color.BLACK);
        Table saveSlot2 = new Table(gameUiSkin);
        saveSlot2.setBackground(gameUiSkin.getDrawable("window-bg"));
        Label saveSlot2Title = new Label("Slot 2", gameUiSkin, "pixel2D", Color.BLACK);
        Table saveSlot3 = new Table(gameUiSkin);
        saveSlot3.setBackground(gameUiSkin.getDrawable("window-bg"));
        Label saveSlot3Title = new Label("Slot 3", gameUiSkin, "pixel2D", Color.BLACK);

        TextButton deleteButton1 = new TextButton("x", gameUiSkin);
        deleteButton1.setLabel(new Label("x", gameUiSkin, "pixel2D", Color.BLACK));
        TextButton deleteButton2 = new TextButton("x", gameUiSkin);
        deleteButton2.setLabel(new Label("x", gameUiSkin, "pixel2D", Color.BLACK));
        TextButton deleteButton3 = new TextButton("x", gameUiSkin);
        deleteButton3.setLabel(new Label("x", gameUiSkin, "pixel2D", Color.BLACK));

        saveSlot1.add(saveSlot1Title);
        saveSlot1.row();
        saveSlot1.add(new StartSaveButton("", gameUiSkin, parent, SaveStates.SLOT_ONE));

        saveSlot2.add(saveSlot2Title);
        saveSlot2.row();
        saveSlot2.add(new StartSaveButton("", gameUiSkin, parent, SaveStates.SLOT_TWO));

        saveSlot3.add(saveSlot3Title);
        saveSlot3.row();
        saveSlot3.add(new StartSaveButton("", gameUiSkin, parent, SaveStates.SLOT_THREE));

        // table
        Table backButtonTable = new Table();
        backButtonTable.add(backButton).expand().top().left();

        table.defaults();

        table.add(backButtonTable).growX();
        table.row();
        table.add(saveSlot1).expand().center();
        table.add(saveSlot2).expand().center();
        table.add(saveSlot3).expand().center();

        // listeners
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Menu Screen!");
                parent.changeScreen(MyGame.MENU_SCREEN);
            }
        });
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

    }
}
