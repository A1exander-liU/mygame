package com.mygdx.game.engine.components.inventory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.AcceptedEquipType;
import com.mygdx.game.engine.InventorySlotSource;
import com.mygdx.game.engine.InventorySlotTarget;
import com.mygdx.game.utils.InventorySlot;

public class InventoryComponent implements Component {
    // how many unique items can be held
    public int capacity = 16;
    public int equipSize = 8;
    public Array<Entity> items = new Array<>(capacity);
    public Array<InventorySlot> inventorySlots = new Array<>(capacity);
    public Array<InventorySlot> equipSlots = new Array<>(equipSize);
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
        // build all equip slots
        InventorySlot head = new InventorySlot(skin, AcceptedEquipType.HEAD);
        InventorySlot torso = new InventorySlot(skin, AcceptedEquipType.TORSO);
        InventorySlot leg = new InventorySlot(skin, AcceptedEquipType.LEG);
        InventorySlot feet = new InventorySlot(skin, AcceptedEquipType.FEET);
        InventorySlot main = new InventorySlot(skin, AcceptedEquipType.MAIN);
        InventorySlot off = new InventorySlot(skin, AcceptedEquipType.OFF);
        InventorySlot accessory1 = new InventorySlot(skin, AcceptedEquipType.ACCESSORY);
        InventorySlot accessory2 = new InventorySlot(skin, AcceptedEquipType.ACCESSORY);
        dragAndDrop.addSource(new InventorySlotSource(head, dragAndDrop));
        dragAndDrop.addSource(new InventorySlotSource(torso, dragAndDrop));
        dragAndDrop.addSource(new InventorySlotSource(leg, dragAndDrop));
        dragAndDrop.addSource(new InventorySlotSource(feet, dragAndDrop));
        dragAndDrop.addSource(new InventorySlotSource(main, dragAndDrop));
        dragAndDrop.addSource(new InventorySlotSource(off, dragAndDrop));
        dragAndDrop.addSource(new InventorySlotSource(accessory1, dragAndDrop));
        dragAndDrop.addSource(new InventorySlotSource(accessory2, dragAndDrop));
        dragAndDrop.addTarget(new InventorySlotTarget(head));
        dragAndDrop.addTarget(new InventorySlotTarget(torso));
        dragAndDrop.addTarget(new InventorySlotTarget(leg));
        dragAndDrop.addTarget(new InventorySlotTarget(feet));
        dragAndDrop.addTarget(new InventorySlotTarget(main));
        dragAndDrop.addTarget(new InventorySlotTarget(off));
        dragAndDrop.addTarget(new InventorySlotTarget(accessory1));
        dragAndDrop.addTarget(new InventorySlotTarget(accessory2));
        equipSlots.add(head);
        equipSlots.add(torso);
        equipSlots.add(leg);
        equipSlots.add(feet);
        equipSlots.add(main);
        equipSlots.add(off);
        equipSlots.add(accessory1);
        equipSlots.add(accessory2);
    }

}
