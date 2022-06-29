package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;

public class MainMenuScreen implements Screen {
    private MyGame parent;
    private Stage stage;

    public MainMenuScreen(MyGame parent) {
        this.parent = parent;

        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);

        Skin gameUiSkin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));

        TextButton startGame = new TextButton("Start", gameUiSkin);
        TextButton settings = new TextButton("Settings", gameUiSkin);
        TextButton quit = new TextButton("Quit", gameUiSkin);

        /* Buttons extend table, you can add rows to buttons to display text
        * on different rows (make sure you use labels to display the text instead
        * and add the labels to the button: <buttonName>.add(<yourLabel>)) */

        // ChangeListener: triggered when item is interacted with
        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Start Game!");
                parent.changeScreen(MyGame.GAME_SCREEN);
            }
        });
        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Settings!");
                parent.changeScreen(MyGame.SETTINGS_SCREEN);
            }
        });
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                Gdx.app.exit();
            }
        });


        // apply default values: the values will be applied to every cell of this table
        /* use uniform() and fill() to make buttons stay even if some buttons have more text
        * than others */
        table.defaults().pad(5).growX();
        // use width() and height() on "cells" to change size
        // pad() adds up (if you pad 10 right of one cell and left for one cell, pad adds up to 20)
        // space() doesn't add up (if you space 10 right of one cell and left for one cell, it stays as 10)
        table.pad(80);
        table.add(startGame);
        table.row();
        table.add(settings);
        table.row();
        table.add(quit);
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
