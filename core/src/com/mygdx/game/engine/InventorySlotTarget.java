package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.utils.InventorySlot;

import java.util.Map;
import java.util.Objects;

public class InventorySlotTarget extends DragAndDrop.Target {

    Entity targetItem;
    InventorySlot targetSlot;

    public InventorySlotTarget(InventorySlot actor) {
        super(actor);
        targetSlot = actor;
        targetItem = actor.getOccupiedItem();
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
        targetItem = targetSlot.getOccupiedItem();
        if (targetSlot.isEmpty()) {
            System.out.println("assign");
            targetSlot.setOccupiedItem(sourceItem);
        }
        else if (Objects.equals(Mappers.name.get(sourceItem).name, Mappers.name.get(targetItem).name)) {
            System.out.println("stack");
            // stack the items
            targetSlot.stack(sourceSlot);
        // if the names are not the same
        } else if (!Objects.equals(Mappers.name.get(sourceItem).name, Mappers.name.get(targetItem).name)) {
            System.out.println("swap");
            targetSlot.swap(sourceSlot);
        }
    }
}
