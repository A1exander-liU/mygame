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
import com.mygdx.game.engine.ItemFactory;
import com.mygdx.game.engine.ItemType;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.Rarity;
import com.mygdx.game.engine.components.inventory.items.individual.EquipTypeComponent;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.engine.components.inventory.items.individual.StackableComponent;
import com.mygdx.game.engine.components.inventory.items.shared.DescriptionComponent;
import com.mygdx.game.engine.components.inventory.items.shared.InventoryItemComponent;
import com.mygdx.game.engine.components.inventory.InventorySlotComponent;
import com.mygdx.game.engine.components.Name;
import com.mygdx.game.engine.components.Sprite;
import com.mygdx.game.engine.components.inventory.items.shared.QuantityComponent;
import com.mygdx.game.engine.components.inventory.items.shared.RarityComponent;
import com.mygdx.game.utils.InventorySlot;

public class InventoryTest extends EntitySystem {
    ComponentGrabber cg;
    Entity player;
    ItemFactory itemFactory;
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

    public InventoryTest(ComponentGrabber cg, ItemFactory itemFactory) {
        super(99);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        this.itemFactory = itemFactory;
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

        Entity testItem = itemFactory.makeMaterial("Wood", 94);
        addToInventorySlot(testItem, 0);

        Entity diffItem = itemFactory.makeMaterial("Stone", 7);
        addToInventorySlot(diffItem, 1);

        Entity woodItem = itemFactory.makeMaterial("Wood", 8);
        addToInventorySlot(woodItem, 2);

        Entity swordItem = itemFactory.makeWeapon("Iron Sword");
        addToInventorySlot(swordItem, 3);

        Entity swordItem2 = itemFactory.makeWeapon("Steel Sword");
        addToInventorySlot(swordItem2, 4);

        Entity head = itemFactory.makeArmour("Scale Helmet", AcceptedEquipType.HEAD);
        addToInventorySlot(head, 5);

        Entity torso = itemFactory.makeArmour("Scale Armour", AcceptedEquipType.TORSO);
        addToInventorySlot(torso, 6);

        Entity leg = itemFactory.makeArmour("Scale Pants", AcceptedEquipType.LEG);
        addToInventorySlot(leg, 7);
        
        Entity feet = itemFactory.makeArmour("Scale Boots", AcceptedEquipType.FEET);
        addToInventorySlot(feet, 8);
    }

    private void addToInventorySlot(Entity item, int slot) {
        Array<InventorySlot> inventorySlots = Mappers.inventory.get(player).inventorySlots;
        if (slot < 0 || slot >= inventorySlots.size)
            return;
        inventorySlots.get(slot).setOccupiedItem(item);
    }
}
