package com.mygdx.game.utils.saves;

import com.mygdx.game.inventory.gameitem.AcceptedEquipType;
import com.mygdx.game.inventory.gameitem.ItemType;
import com.mygdx.game.utils.ui.InventorySlot;

public class SavedSlot {
    boolean isEquipSlot;
    AcceptedEquipType acceptedEquipType;
    ItemType acceptedItemType;
    boolean clicked;

    public SavedSlot() {}

    public SavedSlot(InventorySlot inventorySlot) {
//            skin = inventorySlot.getSkin();
        isEquipSlot = inventorySlot.getIsEquipSlot();
        acceptedEquipType = inventorySlot.getAcceptedEquipType();
        acceptedItemType = inventorySlot.getAcceptedType();
        clicked = false;
    }
}
