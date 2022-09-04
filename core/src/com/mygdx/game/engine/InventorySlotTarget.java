package com.mygdx.game.engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.InventoryUi;
import com.mygdx.game.engine.utils.Mappers;
import com.mygdx.game.utils.ui.ItemSplitDialog;
import com.mygdx.game.utils.ui.InventorySlot;

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
        // now need to display a tooltip window when clicking an item
        // when dragging stackable item, have window appear to split amount:
        // only appears if quantity is greater than 1
        // will be slider with 1 to quantity
        // defaults slider value at max (the quantity)

        // the dragged actor
        InventorySlot sourceSlot = (InventorySlot) source.getActor();
        sourceSlot.setOccupiedItem((Entity) payload.getObject());
        // info of the item being dragged
        Entity sourceItem = (Entity) payload.getObject();
        // if the item names are the same
        Entity targetItem = targetSlot.getOccupiedItem();
        if (sourceSlot == targetSlot) return;
        // check if null (if null, target slot can accept any item)
        if (targetSlot.getAcceptedType() == null && sourceSlot.getAcceptedType() == null) {
            // if target slot is empty
            if (targetSlot.isEmpty()) {
                // only open split dialog if item i stackable
                if (Mappers.stackable.get(sourceItem) != null) {
                    System.out.println("stackable");
                    ItemSplitDialog splitDialog = new ItemSplitDialog(Mappers.name.get(sourceItem).name, sourceSlot.getSkin(), sourceSlot, targetSlot);
                    splitDialog.show(sourceSlot.getStage());
                    splitDialog.setWidth(200);
                } else {
                    inventoryUi.setItem(sourceSlot, targetSlot);
                }
                System.out.println("assign");
            }

            else if (Objects.equals(Mappers.name.get(sourceItem).name, Mappers.name.get(targetItem).name)) {
                System.out.println("stack");
                // stack the items
                inventoryUi.stackItems(sourceSlot, targetSlot);
            }
            // if the names are not the same
            else if (!Objects.equals(Mappers.name.get(sourceItem).name, Mappers.name.get(targetItem).name)) {
                System.out.println("swap");
                inventoryUi.swapItems(sourceSlot, targetSlot);
            }
        }
        // means the target slot was an equip slot
        else {
            // check if the equipment was dropped on matching slot
            if (Mappers.inventoryItem.get(sourceItem).acceptedItemType == targetSlot.getAcceptedType()) {
                // check if equipment slot is empty
                if (targetSlot.isEmpty()) {
                    System.out.println("equip item");
                    inventoryUi.equip(sourceSlot, targetSlot);
                }
                // check if an item is already equipped in the slot
                else if (!targetSlot.isEmpty()) {
                    System.out.println("swap equipment");
                    inventoryUi.swapEquipment(sourceSlot, targetSlot);
                }
            }
            // unequipping item
            else if (targetSlot.getAcceptedType() == null){
                inventoryUi.unequip(sourceSlot, targetSlot);
            }
        }
    }
}
