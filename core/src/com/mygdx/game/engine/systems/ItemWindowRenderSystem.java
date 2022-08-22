package com.mygdx.game.engine.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.Families;
import com.mygdx.game.engine.Mappers;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.utils.InventorySlot;


public class ItemWindowRenderSystem extends EntitySystem {

    Entity player;
    Array<InventorySlot> inventorySlots;
    Array<InventorySlot> equipSlots;

    public ItemWindowRenderSystem() {
        player = MyGame.engine.getEntitiesFor(Families.player).get(0);
        InventoryComponent inventoryComponent = Mappers.inventory.get(player);
        inventorySlots = inventoryComponent.inventorySlots;
        equipSlots = inventoryComponent.equipSlots;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < inventorySlots.size; i++) {

        }
        for (int i = 0; i < equipSlots.size; i++) {
            
        }
    }
}
