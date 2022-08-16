package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.ComponentGrabber;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.components.InventoryComponent;
import com.mygdx.game.engine.components.InventorySlotComponent;

public class InventoryTest extends EntitySystem {
    ComponentGrabber cg;
    Entity player;

    public InventoryTest(ComponentGrabber cg) {
        super(101);
        this.cg = cg;
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);

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
}
