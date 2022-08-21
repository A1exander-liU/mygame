package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.utils.InventorySlot;

public class InventoryChangeListener extends ClickListener {

    Stage stage;
    Skin skin;

    private Dialog lastPopUp;

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
        InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
        Entity occupiedItem = inventorySlot.getOccupiedItem();
        stage = inventorySlot.getStage();
        // check if inventory slot holds an item and is a material type
        if (!inventorySlot.isEmpty() && Mappers.inventoryItem.get(occupiedItem).itemType == ItemType.MATERIAL) {
            System.out.println(Mappers.name.get(occupiedItem).name);
            Dialog itemInfo = new Dialog("" + Mappers.name.get(occupiedItem).name, skin);
            itemInfo.getTitleTable().center();
            itemInfo.text(new Label(Mappers.description.get(occupiedItem).description, skin, "pixel2D", Color.BLACK));
            itemInfo.show(stage);
        }
    }
}
