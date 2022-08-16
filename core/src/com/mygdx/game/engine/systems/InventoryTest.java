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

import org.w3c.dom.ls.LSOutput;

import java.util.Objects;
import java.util.Scanner;

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
    int currentSlot = 1;

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
            String dividers = currentSlot == i + 1 ? "**********" : "----------";
            InventorySlotComponent slotComponent = cg.getInventorySlot(slot);
            System.out.println(dividers);
            System.out.println("Slot " + (i + 1));
            System.out.println((slotComponent.itemOccupied == null) ? "empty": "Item: " + cg.getName(slotComponent.itemOccupied).name);
            System.out.println("Amount: " + slotComponent.quantity);
            System.out.println(dividers);
        }
    }

    private void test() {
        InventoryComponent inventory = cg.getInventory(player);
        Entity testItem = new Entity();
        testItem.add(new InventoryItemComponent());
        testItem.add(new Name());
        testItem.add(new Sprite());
        cg.getName(testItem).name = "Wood";
        MyGame.engine.addEntity(testItem);
        addToInventory(testItem);

        Entity diffItem = new Entity();
        diffItem.add(new InventoryItemComponent());
        diffItem.add(new Name());
        diffItem.add(new Sprite());
        cg.getName(diffItem).name = "Stone";
        MyGame.engine.addEntity(diffItem);
        addToInventory(diffItem);

        Entity wood = new Entity();
        wood.add(new InventoryItemComponent());
        wood.add(new Name());
        wood.add(new Sprite());
        cg.getName(wood).name = "Wood";
        MyGame.engine.addEntity(wood);
        addToInventory(wood);

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
