package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;

public class NewPlayerScreen implements Screen {
    MyGame parent;
    Stage stage;

    public NewPlayerScreen(MyGame parent) {
        this.parent = parent;
        this.stage = new Stage(new ScreenViewport());
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

        Label nameLabel = new Label("Enter your name", gameUiSkin, "pixel2D", Color.BLACK);
        TextField textField = new TextField("", gameUiSkin);

        table.defaults().pad(5);

        table.add(nameLabel);
        table.row();
        table.add(textField);
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
