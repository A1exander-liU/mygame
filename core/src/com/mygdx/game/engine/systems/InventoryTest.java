package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Rarity;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.engine.components.inventory.items.shared.DescriptionComponent;
import com.mygdx.game.engine.components.inventory.items.shared.InventoryItemComponent;
import com.mygdx.game.engine.components.inventory.InventorySlotComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.inventory.items.shared.QuantityComponent;
import com.mygdx.game.engine.components.inventory.items.shared.RarityComponent;

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
    int currentSlot = 20;

    public InventoryTest(ComponentGrabber cg) {
        super(99);
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
            System.out.println((slotComponent.itemOccupied == null) ? "" : cg.getRarity(slotComponent.itemOccupied).rarity);
            System.out.println((slotComponent.itemOccupied == null) ? "empty": "Item: " + cg.getName(slotComponent.itemOccupied).name);
            System.out.println("Amount: " + slotComponent.quantity);
            System.out.println(dividers);
        }
    }

    private void test() {
        // items are defined from json too
        //

        Entity testItem = new Entity();
        testItem.add(new InventoryItemComponent());
        testItem.add(new Name());
        testItem.add(new Sprite());
        testItem.add(new QuantityComponent());
        testItem.add(new RarityComponent());
        testItem.add(new DescriptionComponent());
        cg.getName(testItem).name = "Wood";
        cg.getQuantity(testItem).quantity = 10;
        cg.getRarity(testItem).rarity = Rarity.COMMON;
        cg.getDescription(testItem).description = "Abundant material used to craft many items";
        MyGame.engine.addEntity(testItem);
        addToInventory(testItem);

        Entity diffItem = new Entity();
        diffItem.add(new InventoryItemComponent());
        diffItem.add(new Name());
        diffItem.add(new Sprite());
        diffItem.add(new QuantityComponent());
        diffItem.add(new RarityComponent());
        diffItem.add(new DescriptionComponent());
        cg.getName(diffItem).name = "Stone";
        cg.getQuantity(diffItem).quantity = 7;
        cg.getRarity(diffItem).rarity = Rarity.COMMON;
        cg.getDescription(diffItem).description = "Found all over the world";
        MyGame.engine.addEntity(diffItem);
        addToInventory(diffItem);

        Entity wood = new Entity();
        wood.add(new InventoryItemComponent());
        wood.add(new Name());
        wood.add(new Sprite());
        wood.add(new QuantityComponent());
        wood.add(new RarityComponent());
        wood.add(new DescriptionComponent());
        cg.getName(wood).name = "Wood";
        cg.getQuantity(wood).quantity = 4;
        cg.getRarity(wood).rarity = Rarity.COMMON;
        cg.getDescription(wood).description = "Abundant item used to craft many items";
        MyGame.engine.addEntity(wood);
        addToInventory(wood);
    }

    private void addToInventory(Entity entity) {
        InventoryComponent inventory = cg.getInventory(player);
        Name itemName = cg.getName(entity);
        QuantityComponent itemQuantity = cg.getQuantity(entity);
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
                cg.getQuantity(slot.itemOccupied).quantity += itemQuantity.quantity;
                MyGame.engine.removeEntity(entity);
                return;
            }
            // add item to first empty slot
            else if (slot.itemOccupied == null) {
                slot.itemOccupied = entity;
                slot.quantity = itemQuantity.quantity; // need to add the relevant components later
                return;
            }
        }
    }

    private void showMoreInfo() {

    }
}
