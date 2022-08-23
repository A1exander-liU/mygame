package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.utils.InventorySlot;

public class ItemInfoDialog extends Dialog {

    InventorySlot inventorySlot;
    Entity item;

    public ItemInfoDialog(String title, Skin skin, InventorySlot inventorySlot) {
        super(title, skin);
        getTitleLabel().setAlignment(Align.center);
        this.inventorySlot = inventorySlot;
        item = inventorySlot.getOccupiedItem();
        baseDialog();
        decideDialogToBuild();
    }

    public ItemInfoDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public ItemInfoDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);

    }

    @Override
    protected void result(Object object) {
        if (object.equals("close")) {
            hide();
        }
        else {
            cancel();
        }

    }

    private void baseDialog() {
        Label itemRarity = new Label("" + Mappers.rarity.get(item).rarity, getSkin(), "pixel2D", Color.BLACK);
        itemRarity.setAlignment(Align.center);
        Image itemSprite = new Image(new TextureRegionDrawable(Mappers.sprite.get(item).texture));
        itemSprite.setScaling(Scaling.contain);

        getContentTable().add(itemRarity).grow();
        getContentTable().row();
        getContentTable().add(itemSprite).grow();

        button("Close", "close");

        getContentTable().padTop(5);
        getButtonTable().pad(5);
    }

    private void decideDialogToBuild() {
        if (Mappers.inventoryItem.get(item).itemType == ItemType.MATERIAL) {
            addMaterialInfo();
        }
        else if (Mappers.inventoryItem.get(item).itemType == ItemType.CONSUMABLE) {

        }
        else if (Mappers.inventoryItem.get(item).itemType == ItemType.EQUIPMENT) {

        }
    }

    private void addMaterialInfo() {
        Label desc = new Label(Mappers.description.get(item).description, getSkin(), "pixel2D", Color.BLACK);

        getContentTable().row();
        getContentTable().add(desc);
    }
}
