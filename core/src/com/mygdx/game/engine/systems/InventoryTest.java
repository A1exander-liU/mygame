package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.InventoryComponent;
import com.mygdx.game.engine.components.InventoryItemComponent;
import com.mygdx.game.engine.components.InventorySlotComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Sprite;

import java.util.Objects;

public class InventoryTest extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    // current inventory design:

    // inventory component which has:
    // boolean to determine whether inventory is opened or not
    // array of entities (which are inventory slots)

    // inventory slot component has:
    // item occupied (the item in this slot)
    // quantity (amount of the item in this slot)

    // item component has:
    // Shared components:
    // name, sprite, quantity, rarity, description
    // individual components:
    // equipment, consumable, material

    // equipment:
    // weapon, armor, accessories, off-hands
    // various components for different stats

    // consumable:
    // hp, mana, clear statuses, buffs


    public InventoryTest(ComponentGrabber cg) {
        super(101);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        test();
    }

    @Override
    public void update(float delta) {
        InventoryComponent inventory = cg.getInventory(player);
        toggleInventory(inventory);
    }

    private void toggleInventory(InventoryComponent inventory) {
        if (!inventory.opened && Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            inventory.opened = true;
            System.out.println("opened inventory");
            getInventory(inventory);
        }
        else if (inventory.opened && Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            inventory.opened = false;
            System.out.println("closed inventory");
        }
    }

    private void getInventory(InventoryComponent inventory) {
        for (int i = 0; i < inventory.items.size; i++) {
            Entity slot = inventory.items.get(i);
            InventorySlotComponent slotComponent = cg.getInventorySlot(slot);
            System.out.println("----------");
            System.out.println("Slot " + (i + 1));
            System.out.println((slotComponent.itemOccupied == null) ? "empty": slotComponent.itemOccupied);
            System.out.println("Amount: " + slotComponent.quantity);
            System.out.println("----------");
        }
    }

    private void test() {
        InventoryComponent inventory = cg.getInventory(player);
        Entity testItem = new Entity();
        testItem.add(new InventoryItemComponent());
        testItem.add(new Name());
        testItem.add(new Sprite());
        cg.getName(testItem).name = "Test";
        cg.getSprite(testItem).texture = new Texture(Gdx.files.internal("testPlayer.png"));
        MyGame.engine.addEntity(testItem);
        addToInventory(testItem);
    }

    private void addToInventory(Entity entity) {
        InventoryComponent inventory = cg.getInventory(player);
        Name itemName = cg.getName(entity);
        for (int i = 0; i < inventory.items.size; i++) {
            Entity itemSlot = inventory.items.get(i);
            InventorySlotComponent slot = cg.getInventorySlot(itemSlot);

            Entity occupiedItem = slot.itemOccupied;
            // name can be null
            Name occupiedItemName = null;
            if (occupiedItem != null)
                occupiedItemName = cg.getName(occupiedItem);
            // if item already exists, add the quantity
            if (occupiedItemName != null && Objects.equals(occupiedItemName.name, itemName.name)) {
                slot.quantity++;
                return;
            }
            // add item to first empty slot
            else if (slot.itemOccupied == null) {
                slot.itemOccupied = entity;
                slot.quantity = 1; // need to add the relevant components later
                return;
            }
        }
    }
}
