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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGame;

public class SaveSlotScreen implements Screen {
    MyGame parent;
    Stage stage;

    public SaveSlotScreen(MyGame parent) {
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

        Skin gameUiSkin = new Skin();

        FreeTypeFontGenerator generator  = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter);
        gameUiSkin.add("pixel2D", font);
        gameUiSkin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        gameUiSkin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));

        TextButton backButton = new TextButton("Back", gameUiSkin);
        backButton.setLabel(new Label("Back", gameUiSkin, "pixel2D", Color.BLACK));
        backButton.getLabel().setAlignment(Align.center);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8824f, 0.7765f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
