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
        targetItem = actor.getOccupiedItem();
        targetSlot = actor;
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        return true;
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        // info of the item being dragged
        Entity sourceItem = (Entity) payload.getObject();
        // the dragged actor
        InventorySlot sourceSlot = (InventorySlot) source.getActor();
        // if the item names are the same
        if (Objects.equals(Mappers.name.get(sourceItem).name, Mappers.name.get(targetItem).name)) {
            // stack the items
            targetSlot.stack(sourceSlot);
        // if the names are not the same
        } else if (!Objects.equals(Mappers.name.get(sourceItem).name, Mappers.name.get(targetItem).name)) {
            targetSlot.swap(sourceSlot);
        }
    }
}
