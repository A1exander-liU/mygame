package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InventoryUi;
import com.mygdx.game.MyGame;
import com.mygdx.game.engine.components.inventory.InventoryComponent;
import com.mygdx.game.utils.InventorySlot;

import java.util.Map;
import java.util.Objects;

public class InventorySlotTarget extends DragAndDrop.Target {

    InventorySlot targetSlot;
    InventoryUi inventoryUi;

    public InventorySlotTarget(InventorySlot actor) {
        super(actor);
        targetSlot = actor;
        inventoryUi = new InventoryUi();
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        return true;
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        // the dragged actor
        InventorySlot sourceSlot = (InventorySlot) source.getActor();
        // info of the item being dragged
        Entity sourceItem = (Entity) payload.getObject();
        // if the item names are the same
        Entity targetItem = targetSlot.getOccupiedItem();
        // check if null (if null, target slot can accept any item)
        if (targetSlot.getAcceptedEquipType() == null) {
            // if target slot is empty
            if (targetSlot.isEmpty()) {
                System.out.println("assign");
                inventoryUi.setItem(sourceSlot, targetSlot);
//            targetSlot.setOccupiedItem(sourceItem);
            }
            else if (Objects.equals(Mappers.name.get(sourceItem).name, Mappers.name.get(targetItem).name)) {
                System.out.println("stack");
                // stack the items
                inventoryUi.stackItems(sourceSlot, targetSlot);
//            targetSlot.stack(sourceSlot);
            }
            // if the names are not the same
            else if (!Objects.equals(Mappers.name.get(sourceItem).name, Mappers.name.get(targetItem).name)) {
                System.out.println("swap");
                inventoryUi.swapItems(sourceSlot, targetSlot);
//            targetSlot.swap(sourceSlot);
            }
        }
        // means the target slot was an equip slot
        else {
            // check if the equipment was dropped on matching slot
            if (sourceSlot.getAcceptedEquipType() == targetSlot.getAcceptedEquipType()) {
                // check if equipment slot is empty
                if (targetSlot.isEmpty()) {
                    inventoryUi.equip(sourceSlot, targetSlot);
                }
            }
        }
    }
}
