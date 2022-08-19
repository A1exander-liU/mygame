package com.mygdx.game.engine.components.inventory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.InventorySlotSource;
import com.mygdx.game.engine.InventorySlotTarget;
import com.mygdx.game.utils.InventorySlot;

public class InventoryComponent implements Component {
    // how many unique items can be held
    public int capacity = 16;
    public Array<Entity> items = new Array<>(capacity);
    public Array<InventorySlot> inventorySlots = new Array<>(capacity);
    public boolean opened = false;
    public DragAndDrop dragAndDrop = new DragAndDrop();
    Skin skin = new Skin(Gdx.files.internal("Game_UI_Skin/Game_UI_Skin.json"));
    // each item will contain these components:
    // ALL: all items must have, ONE: have one of the listed
    // ALL:
    // InventoryItemComponent:
    // NameComponent:
    // RarityComponent:
    // DescriptionComponent:
    // SpriteComponent:

    // ONE:
    // EquipmentComponent: Will also have enum to determine the type
    // ConsumableComponent:
    // MaterialComponent:

    // StackableComponent: a flag to determine item is stackable
    // QuantityComponent: for items where multiple can be obtained at once
    // like getting 3 wood for chopping a tree

    public InventoryComponent() {
        for (int i = 0; i < capacity; i++) {
            Entity slot = new Entity();
            slot.add(new InventorySlotComponent());
            items.add(slot);
            MyGame.engine.addEntity(slot);
        }
        for (int i = 0; i < capacity; i++) {
            InventorySlot inventorySlot = new InventorySlot(skin);
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot, dragAndDrop));
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            inventorySlots.add(inventorySlot);
        }
    }

}
