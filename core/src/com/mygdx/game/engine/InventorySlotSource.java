package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.utils.InventorySlot;

import java.util.Objects;

public class InventorySlotSource extends DragAndDrop.Source {

    DragAndDrop dragAndDrop;
    Entity sourceItem;
    InventorySlot sourceSlot;

    public InventorySlotSource(InventorySlot actor, DragAndDrop dragAndDrop) {
        super(actor);
        this.dragAndDrop = dragAndDrop;
        sourceItem = actor.getOccupiedItem();
        sourceSlot = actor;
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
        if (sourceSlot.isEmpty())
            return null;
        DragAndDrop.Payload payload = new DragAndDrop.Payload();
        // the item in the slot that is being dragged
        payload.setObject(sourceItem);
        // each InventorySlot only has one child:
        // the Stack actor which holds the item sprite and label that shows
        // the item quantity
        payload.setDragActor(sourceSlot.getChildren().get(0));
        payload.setValidDragActor(sourceSlot.getChildren().get(0));
        payload.setInvalidDragActor(sourceSlot.getChildren().get(0));

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer,
                         DragAndDrop.Payload payload, DragAndDrop.Target target) {
        // grab the item back when drag stops
        Entity payLoadItem = (Entity) payload.getObject();
        if (target != null) {
            InventorySlot targetSlot = (InventorySlot) target.getActor();
            // get the item of the target (the area where drag actor was over)
            Entity targetItem = targetSlot.getOccupiedItem();
            // check if both the dragged item and the target slot item are the same
            if (Objects.equals(Mappers.name.get(payLoadItem).name, Mappers.name.get(targetItem).name)) {
                targetSlot.stack(sourceSlot);
            }
            // if the target slot is empty
            else if (targetSlot.isEmpty()) {
                // if the slot is empty, bring the item over
                targetSlot.setOccupiedItem(payLoadItem);
                // set to null since item was moved over to new slot
                sourceSlot.setOccupiedItem(null);
            // if the items are different, they will be swapped
            } else {
                sourceSlot.swap(targetSlot);
            }
        }
    }
}
