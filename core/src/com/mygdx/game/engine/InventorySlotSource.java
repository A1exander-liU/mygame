package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.utils.InventorySlot;

import java.util.Objects;

public class InventorySlotSource extends DragAndDrop.Source {

    DragAndDrop dragAndDrop;
    InventorySlot sourceSlot;

    public InventorySlotSource(InventorySlot actor, DragAndDrop dragAndDrop) {
        super(actor);
        this.dragAndDrop = dragAndDrop;
        sourceSlot = actor;
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
        if (sourceSlot.isEmpty())
            return null;
        DragAndDrop.Payload payload = new DragAndDrop.Payload();
        // the item in the slot that is being dragged
        payload.setObject(sourceSlot.getOccupiedItem());
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
       if (target == null) {
           System.out.println("invalid");
//           sourceSlot.add(payload.getDragActor());
       }
    }
}