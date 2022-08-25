package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
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

        Image sourceImage = new Image(sourceSlot.getItemImage().getDrawable());

        payload.setDragActor(sourceImage);

        // remove the item so when dragging the item sprite will not appear in the
        // sourceSlot
        sourceSlot.setOccupiedItem(null);

        // center the drag actor on the cursor
        dragAndDrop.setDragActorPosition((sourceImage.getWidth() / 2), -(sourceImage.getHeight() / 2));

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer,
                         DragAndDrop.Payload payload, DragAndDrop.Target target) {
       if (target == null) {
           System.out.println("invalid");
           sourceSlot.setOccupiedItem((Entity) payload.getObject());
       }
    }
}
