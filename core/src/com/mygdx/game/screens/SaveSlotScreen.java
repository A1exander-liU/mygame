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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.SaveStates;
import com.mygdx.game.utils.saves.SavedPlayer;
import com.mygdx.game.utils.ui.saving.DeleteSaveButton;
import com.mygdx.game.utils.ui.saving.StartSaveButton;

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
        table.setDebug(false);
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
        saveSlot1.setName("slotOne");
        saveSlot1.setBackground(gameUiSkin.getDrawable("player-hud-bg-01"));
        Label saveSlot1Title = new Label("Slot 1", gameUiSkin, "pixel2D", Color.BLACK);
        Table saveSlot2 = new Table(gameUiSkin);
        saveSlot2.setName("slotTwo");
        saveSlot2.setBackground(gameUiSkin.getDrawable("player-hud-bg-01"));
        Label saveSlot2Title = new Label("Slot 2", gameUiSkin, "pixel2D", Color.BLACK);
        Table saveSlot3 = new Table(gameUiSkin);
        saveSlot3.setName("slotThree");
        saveSlot3.setBackground(gameUiSkin.getDrawable("player-hud-bg-01"));
        Label saveSlot3Title = new Label("Slot 3", gameUiSkin, "pixel2D", Color.BLACK);

        TextButton deleteButton1 = new TextButton("x", gameUiSkin);
        deleteButton1.setLabel(new Label("x", gameUiSkin, "pixel2D", Color.BLACK));
        TextButton deleteButton2 = new TextButton("x", gameUiSkin);
        deleteButton2.setLabel(new Label("x", gameUiSkin, "pixel2D", Color.BLACK));
        TextButton deleteButton3 = new TextButton("x", gameUiSkin);
        deleteButton3.setLabel(new Label("x", gameUiSkin, "pixel2D", Color.BLACK));

        Label slotOnePlayerName = new Label("", gameUiSkin, "pixel2D", Color.BLACK);
        slotOnePlayerName.setName("slotOneName");
        Label slotTwoPlayerName = new Label("", gameUiSkin, "pixel2D", Color.BLACK);
        slotTwoPlayerName.setName("slotTwoName");
        Label slotThreePlayerName = new Label("", gameUiSkin, "pixel2D", Color.BLACK);
        slotThreePlayerName.setName("slotThreeName");

        saveSlot1.defaults().expand().space(5);
        saveSlot1.setDebug(false);
        saveSlot2.defaults().expand().space(5);
        saveSlot3.defaults().expand().space(5);

        saveSlot1.add(saveSlot1Title).top().padTop(10);
        saveSlot1.add(new DeleteSaveButton("", gameUiSkin, parent, SaveStates.SLOT_ONE)).top();
        if (!Objects.equals(parent.getSaveStates().getSlotSerializedData(SaveStates.SLOT_ONE), "")) {
            String savedData = parent.getSaveStates().getSlotSerializedData(SaveStates.SLOT_ONE);
            SavedPlayer savedPlayer = json.fromJson(SavedPlayer.class, savedData);
            slotOnePlayerName.setText(savedPlayer.name.name + "\nLv." + savedPlayer.levelComponent.level);
        }
        saveSlot1.row();
        saveSlot1.add(new StartSaveButton("", gameUiSkin, parent, SaveStates.SLOT_ONE)).colspan(2).center().bottom();
        saveSlot2.add(saveSlot2Title).top().padTop(10);
        saveSlot2.add(new DeleteSaveButton("", gameUiSkin, parent, SaveStates.SLOT_TWO)).top();
        saveSlot2.row();
        saveSlot2.add(new StartSaveButton("", gameUiSkin, parent, SaveStates.SLOT_TWO)).colspan(2).center().bottom();

        saveSlot3.add(saveSlot3Title).top().padTop(10);
        saveSlot3.add(new DeleteSaveButton("", gameUiSkin, parent, SaveStates.SLOT_THREE)).top();
        saveSlot3.row();
        saveSlot3.add(new StartSaveButton("", gameUiSkin, parent, SaveStates.SLOT_THREE)).colspan(2).center().bottom();

        // table
        Table backButtonTable = new Table();
        backButtonTable.add(backButton).expand().top().left().pad(5);

        table.defaults();

        table.add(backButtonTable).growX();
        table.row();
        table.add(saveSlot1).expand().center().width(stage.getWidth() / 4f).height(stage.getHeight() / 3f);
        table.add(saveSlot2).expand().center().width(stage.getWidth() / 4f).height(stage.getHeight() / 3f);
        table.add(saveSlot3).expand().center().width(stage.getWidth() / 4f).height(stage.getHeight() / 3f);

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

        if (Objects.equals(parent.getSaveStates().getSlotSerializedData(SaveStates.SLOT_ONE), "")) {
            Table saveSlot = stage.getRoot().findActor("slotOne");
            Cell<?> cell = saveSlot.getCells().get(1);
            cell.setActor(null);
        }

        if (Objects.equals(parent.getSaveStates().getSlotSerializedData(SaveStates.SLOT_TWO), "")) {
            Table saveSlot = stage.getRoot().findActor("slotTwo");
            Cell<?> cell = saveSlot.getCells().get(1);
            cell.setActor(null);
        }

        if (Objects.equals(parent.getSaveStates().getSlotSerializedData(SaveStates.SLOT_THREE), "")) {
            Table saveSlot = stage.getRoot().findActor("slotThree");
            Cell<?> cell = saveSlot.getCells().get(1);
            cell.setActor(null);
        }

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
