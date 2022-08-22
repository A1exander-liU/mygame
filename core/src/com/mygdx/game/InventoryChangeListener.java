package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
//        stage = inventorySlot.getStage();
        // check if inventory slot holds an item and is a material type
        if (!inventorySlot.isEmpty() && Mappers.inventoryItem.get(occupiedItem).itemType == ItemType.MATERIAL) {
//            inventorySlot.setClicked(true);
            stage = inventorySlot.getStage();

            Window itemInfo = new Window("", skin);
            itemInfo.getTitleLabel().setText(Mappers.name.get(occupiedItem).name + " x" + Mappers.quantity.get(occupiedItem).quantity);

            Label itemRarity = new Label("" + Mappers.rarity.get(occupiedItem).rarity, skin, "pixel2D", Color.BLACK);
            itemRarity.setWrap(true);
            itemRarity.setAlignment(Align.left);

            Label itemDesc = new Label(Mappers.description.get(occupiedItem).description, skin, "pixel2D", Color.BLACK);
            itemDesc.setWrap(true);
            itemDesc.setAlignment(Align.left);

            Image itemSprite = new Image(new TextureRegionDrawable(Mappers.sprite.get(occupiedItem).texture));

            itemInfo.add(itemSprite).grow();
            itemInfo.row();
            itemInfo.add(itemRarity).grow();
            itemInfo.row();
            itemInfo.add(itemDesc).grow();

            stage.addActor(itemInfo);

//            System.out.println(Mappers.name.get(occupiedItem).name);
//            Dialog itemInfo = new Dialog("" + Mappers.name.get(occupiedItem).name, skin);
//            itemInfo.getTitleTable().center();
//            itemInfo.text(new Label(Mappers.description.get(occupiedItem).description, skin, "pixel2D", Color.BLACK));
//            itemInfo.setBackground(skin.getDrawable("window-bg"));
//            itemInfo.setPosition(0, 0);
//            itemInfo.show(stage);
        }
    }
}
