package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.utils.InventorySlot;

public class InventoryChangeListener extends ClickListener {

    Stage stage;
    Skin skin;
    
    private InventorySlot clickedItemSlot;

    public InventoryChangeListener() {
        stage = new Stage(new ScreenViewport());

        skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        BitmapFont newFont = generator.generateFont(parameter);
        skin.add("pixel2D", newFont);
        skin.addRegions(new TextureAtlas("Game_UI_Skin/Game_UI_Skin.atlas"));
        skin.load(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
        generator.dispose();
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if (((InventorySlot) event.getListenerActor()).getOccupiedItem() != null)
            clickedItemSlot = (InventorySlot) event.getListenerActor();
    }

    public InventorySlot getClickedItemSlot() {
        return clickedItemSlot;
    }

    public void setClickedItemSlot(InventorySlot inventorySlot) {
        clickedItemSlot = inventorySlot;
    }

    public void reset() {
        clickedItemSlot = null;
    }
}
