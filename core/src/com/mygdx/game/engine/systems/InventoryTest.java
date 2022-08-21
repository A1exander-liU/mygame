package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.AcceptedEquipType;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.Rarity;
import com.mygdx.game.engine.components.EquipTypeComponent;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.engine.components.inventory.items.shared.DescriptionComponent;
import com.mygdx.game.engine.components.inventory.items.shared.InventoryItemComponent;
import com.mygdx.game.engine.components.inventory.InventorySlotComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.inventory.items.shared.QuantityComponent;
import com.mygdx.game.engine.components.inventory.items.shared.RarityComponent;
import com.mygdx.game.utils.InventorySlot;

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
        testItem.add(new InventoryItemComponent(ItemType.MATERIAL));
        testItem.add(new Name());
        testItem.add(new Sprite());
        testItem.add(new QuantityComponent());
        testItem.add(new RarityComponent());
        testItem.add(new DescriptionComponent());
        cg.getName(testItem).name = "Wood";
        cg.getSprite(testItem).texture = new Texture(Gdx.files.internal("testPlayer.png"));
        cg.getQuantity(testItem).quantity = 14;
        cg.getRarity(testItem).rarity = Rarity.COMMON;
        cg.getDescription(testItem).description = "Abundant material used to craft many items";
        MyGame.engine.addEntity(testItem);
        addToInventorySlot(testItem, 0);

        Entity diffItem = new Entity();
        diffItem.add(new InventoryItemComponent(ItemType.MATERIAL));
        diffItem.add(new Name());
        diffItem.add(new Sprite());
        diffItem.add(new QuantityComponent());
        diffItem.add(new RarityComponent());
        diffItem.add(new DescriptionComponent());
        cg.getName(diffItem).name = "Stone";
        cg.getSprite(diffItem).texture = new Texture(Gdx.files.internal("testPlayer.png"));
        cg.getQuantity(diffItem).quantity = 7;
        cg.getRarity(diffItem).rarity = Rarity.COMMON;
        cg.getDescription(diffItem).description = "Found all over the world";
        MyGame.engine.addEntity(diffItem);
        addToInventorySlot(diffItem, 1);

        Entity woodItem = new Entity();
        woodItem.add(new InventoryItemComponent(ItemType.MATERIAL));
        woodItem.add(new Name());
        woodItem.add(new Sprite());
        woodItem.add(new QuantityComponent());
        woodItem.add(new RarityComponent());
        woodItem.add(new DescriptionComponent());
        cg.getName(woodItem).name = "Wood";
        cg.getSprite(woodItem).texture = new Texture(Gdx.files.internal("testPlayer.png"));
        cg.getQuantity(woodItem).quantity = 8;
        cg.getRarity(woodItem).rarity = Rarity.COMMON;
        cg.getDescription(woodItem).description = "Abundant material used to craft many items";
        MyGame.engine.addEntity(woodItem);
        addToInventorySlot(woodItem, 2);

        Entity swordItem = new Entity();
        swordItem.add(new InventoryItemComponent(ItemType.EQUIPMENT));
        swordItem.add(new Name());
        swordItem.add(new Sprite());
        swordItem.add(new RarityComponent());
        swordItem.add(new DescriptionComponent());
        swordItem.add(new EquipTypeComponent(AcceptedEquipType.MAIN));
        cg.getName(swordItem).name = "Iron Sword";
        cg.getSprite(swordItem).texture = new Texture(Gdx.files.internal("testPlayer.png"));
        cg.getRarity(swordItem).rarity = Rarity.COMMON;
        cg.getDescription(swordItem).description = "Common and cheap weapon";
        MyGame.engine.addEntity(swordItem);
        addToInventorySlot(swordItem, 3);

        Entity swordItem2 = new Entity();
        swordItem2.add(new InventoryItemComponent(ItemType.EQUIPMENT));
        swordItem2.add(new Name());
        swordItem2.add(new Sprite());
        swordItem2.add(new RarityComponent());
        swordItem2.add(new DescriptionComponent());
        swordItem2.add(new EquipTypeComponent(AcceptedEquipType.MAIN));
        cg.getName(swordItem2).name = "Steel Sword";
        cg.getSprite(swordItem2).texture = new Texture(Gdx.files.internal("testPlayer.png"));
        cg.getRarity(swordItem2).rarity = Rarity.COMMON;
        cg.getDescription(swordItem2).description = "Common and cheap weapon";
        MyGame.engine.addEntity(swordItem2);
        addToInventorySlot(swordItem2, 4);
    }

    private void addToInventorySlot(Entity item, int slot) {
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        if (slot < 0 || slot >= inventorySlots.size)
            return;
        inventorySlots.get(slot).setOccupiedItem(item);
    }
}
