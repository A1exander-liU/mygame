package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.ItemFactory;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.InventorySlot;

public class ItemSplitDialog extends Dialog {

    InventorySlot inventorySlot;
    InventorySlot targetSlot;
    Entity item;
    Entity player;
    InventoryUi inventoryUi;
    int currentValue;

    public ItemSplitDialog(String title, Skin skin, InventorySlot inventorySlot, InventorySlot targetSlot) {
        super(title, skin);
        this.inventorySlot = inventorySlot;
        this.targetSlot = targetSlot;
        item = inventorySlot.getOccupiedItem();
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        inventoryUi = new InventoryUi();
        buildDialog();
    }

    private void buildDialog() {
        // get the slider value to update
        final Slider itemQuantitySlider = new Slider(0, Mappers.quantity.get(item).quantity, 1, false, getSkin());
        itemQuantitySlider.setName("quantitySlider");
        // set initial value to the full stack
        itemQuantitySlider.setValue(Mappers.quantity.get(inventorySlot.getOccupiedItem()).quantity);
        itemQuantitySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // get slider value
                currentValue = (int) itemQuantitySlider.getValue();
                // update the label quantity value
                ((Label) getContentTable().findActor("Quantity")).setText("Quantity: " + currentValue);
            }
        });

        // set initial text to full stack (since slider initial value is full stack)
        Label itemQuantityLabel = new Label("Quantity: " + Mappers.quantity.get(inventorySlot.getOccupiedItem()).quantity, getSkin());
        itemQuantityLabel.setName("Quantity");

        getTitleLabel().setAlignment(Align.center);

        getContentTable().defaults().space(5);
        getButtonTable().defaults().pad(5);

        getContentTable().add(itemQuantitySlider);
        getContentTable().row();
        getContentTable().add(itemQuantityLabel);

        button("Ok", "ok");
    }

    @Override
    public void result(Object object) {
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        if (object.equals("ok")) {
            // do the split
            Slider quantitySlider = getContentTable().findActor("quantitySlider");
            if (quantitySlider.getValue() != 0) {
                // make new entity and set it to the target slot
                Entity splitItem = GameScreen.itemFactory.makeMaterial(Mappers.name.get(inventorySlot.getOccupiedItem()).name, (int) quantitySlider.getValue());
                inventorySlots.get(inventorySlots.indexOf(targetSlot, true)).setOccupiedItem(splitItem);

                // update the sourceSlot item quantity
                int leftover = Mappers.quantity.get(inventorySlot.getOccupiedItem()).quantity - (int )quantitySlider.getValue();
                Mappers.quantity.get(inventorySlots.get(inventorySlots.indexOf(inventorySlot, true)).getOccupiedItem()).quantity = leftover;
            }
        }
    }
}
