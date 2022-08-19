package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.utils.InventorySlot;

public class InventorySlotSource extends DragAndDrop.Source {

    DragAndDrop dragAndDrop;
    Entity sourceItem;

    public InventorySlotSource(InventorySlot actor, DragAndDrop dragAndDrop) {
        // the thing being dragged
        super(actor);
        this.dragAndDrop = dragAndDrop;
        sourceItem = actor.getOccupiedItem();
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
        DragAndDrop.Payload payload = new DragAndDrop.Payload();

        payload.setDragActor(getActor());
        dragAndDrop.setDragActorPosition(-x, -y + getActor().getHeight());

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer,
                         DragAndDrop.Payload payload, DragAndDrop.Target target) {
        if (target == null) {
            // sourceSlot is the container for the inventory item
            // actor is the drag source which is the stack in the InventorySlot
            InventorySlot sourceSlot = (InventorySlot) getActor().getParent();
            sourceSlot.add(payload.getDragActor());
        }
    }
}
