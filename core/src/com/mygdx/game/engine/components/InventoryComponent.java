package com.mygdx.game.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class InventoryComponent implements Component {
    // how many unique items can be held
    public int capacity = 20;
    public Array<Entity> items = new Array<>(capacity);
}
