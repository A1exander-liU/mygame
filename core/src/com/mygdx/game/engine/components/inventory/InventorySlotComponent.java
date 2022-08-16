package com.mygdx.game.engine.components.inventory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class InventorySlotComponent implements Component {
    public int quantity = 0;
    public Entity itemOccupied;
}
