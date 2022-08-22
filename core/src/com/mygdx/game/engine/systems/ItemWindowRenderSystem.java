package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.utils.InventorySlot;


public class ItemWindowRenderSystem extends EntitySystem {

    Entity player;
    Array<InventorySlot> inventorySlots;
    Array<InventorySlot> equipSlots;
    Skin skin;

    public ItemWindowRenderSystem() {
        super(101);
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        InventoryComponent inventoryComponent = Mappers.inventory.get(player);
        inventorySlots = inventoryComponent.inventorySlots;
        equipSlots = inventoryComponent.equipSlots;

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
    public void update(float delta) {
        for (int i = 0; i < inventorySlots.size; i++) {
            if (inventorySlots.get(i).isClicked()) {
                inventorySlots.get(i).setClicked(false);
                // add button to call dialog.hide to remove it
                Stage stage = inventorySlots.get(i).getStage();
                Dialog itemWindow = makeItemWindow(inventorySlots.get(i).getOccupiedItem());
                itemWindow.show(stage);
            }
        }
        for (int i = 0; i < equipSlots.size; i++) {
            if (equipSlots.get(i).isClicked()) {
                equipSlots.get(i).setClicked(false);
            }
        }
    }

    private Dialog makeItemWindow(Entity occupiedItem) {
        Dialog dialog = new Dialog("" + Mappers.name.get(occupiedItem).name, skin);
        dialog.setPosition(0, 0);
        Label desc = new Label(Mappers.description.get(occupiedItem).description, skin, "pixel2D", Color.BLACK);
        desc.setAlignment(Align.topLeft);
        desc.setWrap(true);
        dialog.text(desc);
        return dialog;
    }
}
